/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.images

import android.graphics.Bitmap
import fr.jhelp.utilities.bounds
import fr.jhelp.utilities.minimum
import kotlin.math.max
import kotlin.math.min

const val ALPHA_MASK = 0xFF000000.toInt()
const val COLOR_MASK = 0x00FFFFFF.toInt()

val Int.alpha get() = this ushr 24

val Int.red get() = (this shr 16) and 0xFF

val Int.green get() = (this shr 8) and 0xFF

val Int.blue get() = this and 0xFF

fun color(alpha: Int, red: Int, green: Int, blue: Int) =
    (alpha shl 24) or (red shl 16) or (green shl 8) or blue

fun limitPart(integer: Int) = integer.bounds(0, 255)

/**
 * Compute blue part of color from YUV
 *
 * B = Y + 1.7790 * (U - 128)
 *
 * @param y Y
 * @param u U
 * @param v V
 * @return Blue part
 */
fun computeBlue(y: Double, u: Double, v: Double) =
    limitPart((y + 1.7721604 * (u - 128) + 0.0009902 * (v - 128)).toInt())

/**
 * Compute green part of color from YUV

 * G = Y - 0.3455 * (U - 128) - (0.7169 * (V - 128))
 *
 * @param y Y
 * @param u U
 * @param v V
 * @return Green part
 */
fun computeGreen(y: Double, u: Double, v: Double) =
    limitPart((y - 0.3436954 * (u - 128) - 0.7141690 * (v - 128)).toInt())

/**
 * Compute red part of color from YUV

 * R = Y + 1.4075 * (V - 128)
 *
 * @param y Y
 * @param u U
 * @param v V
 * @return Red part
 */
fun computeRed(y: Double, u: Double, v: Double) =
    limitPart((y - 0.0009267 * (u - 128) + 1.4016868 * (v - 128)).toInt())

/**
 * Compute U of a color

 * U = R * -0.168736 + G * -0.331264 + B * 0.500000 + 128
 *
 * @param red   Red part
 * @param green Green part
 * @param blue  Blue part
 * @return U
 */
fun computeU(red: Int, green: Int, blue: Int) = -0.169 * red - 0.331 * green + 0.500 * blue + 128.0

/**
 * Compute V of a color

 * V = R * 0.500000 + G * -0.418688 + B * -0.081312 + 128
 *
 * @param red   Red part
 * @param green Green part
 * @param blue  Blue part
 * @return V
 */
fun computeV(red: Int, green: Int, blue: Int) = 0.500 * red - 0.419 * green - 0.081 * blue + 128.0

/**
 * Compute Y of a color

 * Y = R * 0.299000 + G * 0.587000 + B * 0.114000
 *
 * @param red   Red part
 * @param green Green part
 * @param blue  Blue part
 * @return Y
 */
fun computeY(red: Int, green: Int, blue: Int) = red * 0.299 + green * 0.587 + blue * 0.114

fun Bitmap.clear(color: Int)
{
    this.pixelsOperation { pixels ->
        for (index in pixels.indices)
        {
            pixels[index] = color
        }
    }
}

fun Bitmap.grey()
{
    this.pixelsOperation { pixels ->
        var color: Int
        var y: Int

        for (index in pixels.indices)
        {
            color = pixels[index]
            y = limitPart(computeY(color.red, color.green, color.blue).toInt())
            pixels[index] = (color and ALPHA_MASK) or (y shl 16) or (y shl 8) or y
        }
    }
}

fun Bitmap.tint(color: Int)
{
    this.grey()

    this.pixelsOperation { pixels ->
        val red = color.red
        val green = color.green
        val blue = color.blue
        var col: Int
        var grey: Int

        for (index in pixels.indices)
        {
            col = pixels[index]
            grey = col.blue
            pixels[index] = (col and ALPHA_MASK) or
                    (((red * grey) shr 8) shl 16) or
                    (((green * grey) shr 8) shl 8) or
                    ((blue * grey) shr 8)
        }
    }
}

fun Bitmap.mask(mask: Bitmap, x: Int, y: Int, width: Int, height: Int)
{
    val sourceWidth = this.width
    val sourceHeight = this.height
    val maskWidth = mask.width
    val maskHeight = mask.height
    var xx = x
    var yy = y
    var w = width
    var h = height

    if (xx < 0)
    {
        w += xx
        xx = 0
    }

    if (yy < 0)
    {
        h += yy
        yy = 0
    }

    w = minimum(w, width - xx, sourceWidth - xx, maskWidth - xx)
    h = minimum(h, height - yy, sourceHeight - yy, maskHeight - yy)

    if (w <= 0 || h <= 0)
    {
        return
    }

    this.pixelsOperation { pixels ->
        mask.pixelsOperation { alphas ->
            var lineSource = xx + yy * sourceWidth
            var lineMask = 0
            var pixelSource: Int
            var pixelsMask: Int

            for (yyy in 0 until h)
            {
                pixelSource = lineSource
                pixelsMask = lineMask

                for (xxx in 0 until w)
                {
                    pixels[pixelSource] = (alphas[pixelsMask] and ALPHA_MASK) or
                            (pixels[pixelSource] and COLOR_MASK)
                    pixelSource++
                    pixelsMask++
                }

                lineSource += sourceWidth
                lineMask += maskWidth
            }
        }
    }
}

fun Bitmap.shift(x: Int, y: Int)
{
    var index = x + y * this.width

    this.pixelsOperation { pixels ->
        val size = pixels.size
        index %= size

        if (index < 0)
        {
            index += size
        }

        val temp = IntArray(size)
        System.arraycopy(pixels, 0, temp, 0, size)

        for (pix in 0 until size)
        {
            pixels[pix] = temp[index]
            index = (index + 1) % size
        }
    }
}

fun Bitmap.copy(bitmap: Bitmap)
{
    if (this.width != bitmap.width || this.height != bitmap.height)
    {
        throw IllegalArgumentException("We can only copy with an image of same size")
    }

    this.pixelsOperation { destination ->
        bitmap.pixelsOperation { source ->
            System.arraycopy(source, 0, destination, 0, source.size)
        }
    }
}

/**
 * Change image contrast by using the middle of the minimum and maximum
 * @param factor Factor to apply to the contrast
 */
fun Bitmap.contrast(factor: Double)
{
    this.pixelsOperation { pixels ->
        var y: Double
        var index = pixels.size - 1
        var color = pixels[index]
        var red = color.red
        var green = color.green
        var blue = color.blue
        var yMax = computeY(red, green, blue)
        var yMin = yMax

        index--
        while (index >= 0)
        {
            color = pixels[index]
            red = color.red
            green = color.green
            blue = color.blue

            y = computeY(red, green, blue)

            yMin = min(yMin, y)
            yMax = max(yMax, y)

            index--
        }

        val yMil = (yMin + yMax) / 2
        var u: Double
        var v: Double

        pixels.indices.forEach {
            color = pixels[it]
            red = color.red
            green = color.green
            blue = color.blue

            y = computeY(red, green, blue)
            u = computeU(red, green, blue)
            v = computeV(red, green, blue)

            y = yMil + factor * (y - yMil)

            pixels[it] = (color and ALPHA_MASK
                    or (computeRed(y, u, v) shl 16)
                    or (computeGreen(y, u, v) shl 8)
                    or computeBlue(y, u, v))
        }
    }
}

fun Bitmap.multiply(bitmap: Bitmap)
{
    if (this.width != bitmap.width || this.height != bitmap.height)
    {
        throw IllegalArgumentException("We can only multiply with an image of same size")
    }

    this.pixelsOperation { thisPixels ->
        bitmap.pixelsOperation { imagePixels ->
            var colorThis: Int
            var colorImage: Int

            thisPixels.indices.forEach {
                colorThis = thisPixels[it]
                colorImage = imagePixels[it]

                thisPixels[it] = colorThis and ALPHA_MASK or
                        (((colorThis.red * colorImage.red) / 255) shl 16) or
                        (((colorThis.green * colorImage.green) / 255) shl 8) or
                        ((colorThis.blue * colorImage.blue) / 255)
            }
        }
    }
}

fun Bitmap.add(bitmap: Bitmap)
{
    if (this.width != bitmap.width || this.height != bitmap.height)
    {
        throw IllegalArgumentException("We can only add with an image of same size")
    }

    this.pixelsOperation { thisPixels ->
        bitmap.pixelsOperation { imagePixels ->
            var colorThis: Int
            var colorImage: Int

            thisPixels.indices.forEach {
                colorThis = thisPixels[it]
                colorImage = imagePixels[it]

                thisPixels[it] = colorThis and ALPHA_MASK or
                        (limitPart(colorThis.red + colorImage.red) shl 16) or
                        (limitPart(colorThis.green + colorImage.green) shl 8) or
                        limitPart(colorThis.blue + colorImage.blue)
            }
        }
    }
}

fun Bitmap.darker(darkerFactor: Int)
{
    val factor = darkerFactor.bounds(0, 255)

    this.pixelsOperation { pixels ->
        var color: Int

        pixels.indices.forEach {
            color = pixels[it]
            pixels[it] = color and ALPHA_MASK or
                    (limitPart(color.red - factor) shl 16) or
                    (limitPart(color.green - factor) shl 8) or
                    limitPart(color.blue - factor)
        }
    }
}

fun Bitmap.invertColors()
{
    this.pixelsOperation { pixels ->
        var color: Int
        pixels.indices.forEach {
            color = pixels[it]

            pixels[it] = color and ALPHA_MASK or
                    ((255 - color.red) shl 16) or
                    ((255 - color.green) shl 8) or
                    (255 - color.blue)
        }
    }
}

inline fun Bitmap.pixelsOperation(operation: (IntArray) -> Unit)
{
    val pixels = IntArray(this.width * this.height)
    this.getPixels(pixels, 0, this.width, 0, 0, this.width, this.height)
    operation(pixels)
    this.setPixels(pixels, 0, this.width, 0, 0, this.width, this.height)
}

fun createBumped(source: Bitmap, bump: Bitmap,
                 contrast: Double = 0.75, dark: Int = 12, shiftX: Int = 1, shiftY: Int = 1): Bitmap
{
    var contrast = contrast
    val width = source.width
    val height = source.height

    if (width != bump.width || height != bump.height)
    {
        throw IllegalArgumentException("Images must have the same size")
    }

    if (contrast < 0.5)
    {
        contrast *= 2.0
    }
    else
    {
        contrast = contrast * 18 - 8
    }

    val bumped = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val temp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    bumped.copy(bump)
    bumped.grey()
    bumped.contrast(contrast)

    temp.copy(bumped)
    temp.multiply(source)
    temp.darker(dark)

    bumped.invertColors()
    bumped.multiply(source)
    bumped.darker(dark)
    bumped.shift(shiftX, shiftY)
    bumped.add(temp)

    temp.recycle()
    return bumped
}

internal fun mixParts(under: Int, over: Int, alpha: Int, ahpla: Int) =
    (under * ahpla + over * alpha) shr 8

internal fun mix(under: Int, over: Int): Int
{
    val alpha = over ushr 24
    val ahpla = 256 - alpha
    return (min(255, (under ushr 24) + alpha) shl 24) or
            (mixParts((under shr 16) and 0xFF, (over shr 16) and 0xFF,
                      alpha, ahpla) shl 16) or
            (mixParts((under shr 8) and 0xFF, (over shr 8) and 0xFF, alpha, ahpla) shl 8) or
            mixParts(under and 0xFF, over and 0xFF, alpha, ahpla)
}

internal fun drawPixels(source: IntArray, xSource: Int, ySource: Int, widthSource: Int,
                        destination: IntArray, xDestination: Int, yDestination: Int,
                        widthDestination: Int,
                        width: Int, height: Int)
{
    var lineSource = xSource + ySource * widthSource
    var lineDestination = xDestination + yDestination * widthDestination
    var pixelSource: Int
    var pixelDestination: Int

    for (y in 0 until height)
    {
        pixelSource = lineSource
        pixelDestination = lineDestination

        for (x in 0 until width)
        {
            destination[pixelDestination] =
                mix(destination[pixelDestination], source[pixelSource])
            pixelSource++
            pixelDestination++
        }

        lineSource += widthSource
        lineDestination += widthDestination
    }
}
