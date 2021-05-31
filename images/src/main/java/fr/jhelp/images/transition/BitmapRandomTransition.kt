/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.images.transition

import android.graphics.Bitmap

/**
 * Transition between images one become other by choose pixels randomly
 */
class BitmapRandomTransition : BitmapTransition
{
    constructor(bitmapStart: Bitmap, bitmapEnd: Bitmap) :
            super(bitmapStart, bitmapEnd)

    internal constructor(bitmapStart: Bitmap, bitmapEnd: Bitmap, bitmapResult: Bitmap) :
            super(bitmapStart, bitmapEnd, bitmapResult)

    private val orderPixels = IntArray(this.numberPixels) { index -> index }

    override fun initialize()
    {
        this.orderPixels.shuffle()
    }

    override fun computeTransition(width: Int, height: Int, numberPixels: Int, factor: Float,
                                   pixelsStart: IntArray, pixelsEnd: IntArray,
                                   pixelsResult: IntArray)
    {
        System.arraycopy(pixelsStart, 0, pixelsResult, 0, numberPixels)
        val number = (numberPixels * factor).toInt()
        var pixel: Int

        for (index in 0 until number)
        {
            pixel = this.orderPixels[index]
            pixelsResult[pixel] = pixelsEnd[pixel]
        }
    }
}