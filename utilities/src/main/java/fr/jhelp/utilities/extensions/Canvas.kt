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
