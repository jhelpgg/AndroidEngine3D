/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.images.transition

import android.graphics.Bitmap
import fr.jhelp.images.alpha
import fr.jhelp.images.blue
import fr.jhelp.images.color
import fr.jhelp.images.green
import fr.jhelp.images.red

/**
 * Transition between images one melt to become other
 */
class BitmapMeltTransition : BitmapTransition
{
    constructor(bitmapStart: Bitmap, bitmapEnd: Bitmap) :
            super(bitmapStart, bitmapEnd)

    internal constructor(bitmapStart: Bitmap, bitmapEnd: Bitmap, bitmapResult: Bitmap) :
            super(bitmapStart, bitmapEnd, bitmapResult)

    override fun computeTransition(width: Int, height: Int, numberPixels: Int, factor: Float,
                                   pixelsStart: IntArray, pixelsEnd: IntArray,
                                   pixelsResult: IntArray)
    {
        val rotcaf = 1f - factor
        var colorStart: Int
        var colorEnd: Int
        var alpha: Int
        var red: Int
        var green: Int
        var blue: Int

        for (pixel in 0 until numberPixels)
        {
            colorStart = pixelsStart[pixel]
            colorEnd = pixelsEnd[pixel]
            alpha = (colorStart.alpha * rotcaf + colorEnd.alpha * factor).toInt()
            red = (colorStart.red * rotcaf + colorEnd.red * factor).toInt()
            green = (colorStart.green * rotcaf + colorEnd.green * factor).toInt()
            blue = (colorStart.blue * rotcaf + colorEnd.blue * factor).toInt()
            pixelsResult[pixel] = color(alpha, red, green, blue)
        }
    }
}