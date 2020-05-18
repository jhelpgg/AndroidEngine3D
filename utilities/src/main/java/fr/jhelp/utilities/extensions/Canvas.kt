/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.utilities.extensions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF

/**
 * Draw a bitmap on place its up left corner at given coordinates
 */
fun Canvas.bitmap(bitmap: Bitmap, x: Float, y: Float)
{
    this.drawBitmap(bitmap, null, RectF(x, y, x + bitmap.width, y + bitmap.height), null)
}

/**
 * Draw a bitmap on place its center at given coordinates
 */
fun Canvas.center(bitmap: Bitmap, x: Float, y: Float)
{
    this.bitmap(bitmap, x - bitmap.width / 2f, y - bitmap.height / 2f)
}
