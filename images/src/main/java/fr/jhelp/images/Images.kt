package fr.jhelp.images

import android.graphics.Bitmap
import fr.jhelp.utilities.bounds
import fr.jhelp.utilities.minimum
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

inline fun Bitmap.pixelsOperation(operation: (IntArray) -> Unit)
{
    val pixels = IntArray(this.width * this.height)
    this.getPixels(pixels, 0, this.width, 0, 0, this.width, this.height)
    operation(pixels)
    this.setPixels(pixels, 0, this.width, 0, 0, this.width, this.height)
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
