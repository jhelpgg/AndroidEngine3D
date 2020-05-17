package fr.jhelp.utilities.extensions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF

fun Canvas.bitmap(bitmap: Bitmap, x: Float, y: Float)
{
    this.drawBitmap(bitmap, null, RectF(x, y, x + bitmap.width, y + bitmap.height), null)
}

fun Canvas.center(bitmap: Bitmap, x: Float, y: Float)
{
    this.bitmap(bitmap, x - bitmap.width / 2f, y - bitmap.height / 2f)
}
