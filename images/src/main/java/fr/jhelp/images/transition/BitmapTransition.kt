/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.images.transition

import android.graphics.Bitmap
import fr.jhelp.tasks.delay
import fr.jhelp.utilities.bounds
import kotlin.math.max

/**
 * Generic transition between images one become other
 */
abstract class BitmapTransition
{
    /**Image result width*/
    val width: Int

    /**Image result height*/
    val height: Int

    /**Image result*/
    val bitmapResult: Bitmap

    protected val numberPixels: Int

    private val pixelsStart: IntArray
    private val pixelsEnd: IntArray
    private val pixelsResult: IntArray

    constructor(bitmapStart: Bitmap, bitmapEnd: Bitmap)
    {
        val startWidth = bitmapStart.width
        val startHeight = bitmapStart.height
        val endWidth = bitmapEnd.width
        val endHeight = bitmapEnd.height
        this.width = max(startWidth, endWidth)
        this.height = max(startHeight, endHeight)
        this.numberPixels = this.width * this.height
        this.pixelsStart = IntArray(this.numberPixels)
        this.pixelsEnd = IntArray(this.numberPixels)
        this.pixelsResult = IntArray(this.numberPixels)
        this.bitmapResult = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)

        if (startWidth == this.width && startHeight == this.height)
        {
            bitmapStart.getPixels(this.pixelsStart, 0, this.width, 0, 0, this.width, this.height)
        }
        else
        {
            val scaled = Bitmap.createScaledBitmap(bitmapStart, this.width, this.height, false)
            scaled.getPixels(this.pixelsStart, 0, this.width, 0, 0, this.width, this.height)
            scaled.recycle()
        }

        if (endWidth == this.width && endHeight == this.height)
        {
            bitmapEnd.getPixels(this.pixelsEnd, 0, this.width, 0, 0, this.width, this.height)
        }
        else
        {
            val scaled = Bitmap.createScaledBitmap(bitmapEnd, this.width, this.height, false)
            scaled.getPixels(this.pixelsEnd, 0, this.width, 0, 0, this.width, this.height)
            scaled.recycle()
        }

        {
            this.initialize()
            this.updatePercent(0f)
        }.delay(1024)
    }

    internal constructor(bitmapStart: Bitmap, bitmapEnd: Bitmap, bitmapResult: Bitmap)
    {
        this.width = bitmapResult.width
        this.height = bitmapResult.height
        this.numberPixels = this.width * this.height
        this.bitmapResult = bitmapResult
        this.pixelsStart = IntArray(this.numberPixels)
        this.pixelsEnd = IntArray(this.numberPixels)
        this.pixelsResult = IntArray(this.numberPixels)

        if (bitmapStart.width == this.width && bitmapStart.height == this.height)
        {
            bitmapStart.getPixels(this.pixelsStart, 0, this.width, 0, 0, this.width, this.height)
        }
        else
        {
            val scaled = Bitmap.createScaledBitmap(bitmapStart, this.width, this.height, false)
            scaled.getPixels(this.pixelsStart, 0, this.width, 0, 0, this.width, this.height)
            scaled.recycle()
        }

        if (bitmapEnd.width == this.width && bitmapEnd.height == this.height)
        {
            bitmapEnd.getPixels(this.pixelsEnd, 0, this.width, 0, 0, this.width, this.height)
        }
        else
        {
            val scaled = Bitmap.createScaledBitmap(bitmapEnd, this.width, this.height, false)
            scaled.getPixels(this.pixelsEnd, 0, this.width, 0, 0, this.width, this.height)
            scaled.recycle()
        }

        {
            this.initialize()
            this.updatePercent(0f)
        }.delay(1024)
    }

    /**
     * Called on initialization
     * Doe nothing by default
     */
    open fun initialize()
    {
        // Does nothing by default
    }

    /**
     * Update image with given percent progress
     */
    fun updatePercent(percent: Float)
    {
        val factor = percent.bounds(0f, 1f)

        when (factor)
        {
            0f   ->
                System.arraycopy(this.pixelsStart, 0, this.pixelsResult, 0, this.numberPixels)
            1f   ->
                System.arraycopy(this.pixelsEnd, 0, this.pixelsResult, 0, this.numberPixels)
            else ->
                this.computeTransition(this.width, this.height, this.numberPixels,
                                       factor,
                                       this.pixelsStart, this.pixelsEnd, this.pixelsResult)
        }

        this.bitmapResult.setPixels(this.pixelsResult, 0, this.width, 0, 0, this.width, this.height)
    }

    /**
     * Inbdicates how to do a transition for a specific implementation
     * @param width Images width
     * @param height Images height
     * @param factor Progression percentage
     * @param pixelsStart Pixels of start image
     * @param pixelsEnd Pixels of end image
     * @param pixelsResult Pixels where write the transition result
     */
    protected abstract fun computeTransition(width: Int, height: Int, numberPixels: Int,
                                             factor: Float,
                                             pixelsStart: IntArray, pixelsEnd: IntArray,
                                             pixelsResult: IntArray)
}