/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.multitools.images

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.fragment.app.Fragment
import fr.jhelp.images.shift
import fr.jhelp.multitools.R

class ShiftImageActivity : ImageActivity(R.string.shiftImageTitle)
{
    override fun doImageOperation(bitmap: Bitmap, canvas: Canvas, paint: Paint)
    {
        val (x, y) = shiftImageCoordinates.value
        bitmap.shift(x, y)
    }

    override fun obtainFragmentButtons(): Fragment?
    {
        shiftImageCoordinates.observedBy { this.refreshImage() }
        return ShitImageButtonsFragment()
    }
}