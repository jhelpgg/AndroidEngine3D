/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.multitools.images

import android.graphics.Bitmap
import fr.jhelp.images.copy
import fr.jhelp.images.multiply
import fr.jhelp.multitools.R

class MultiplyImageActivity : TwoImagesCombinationActivity(R.string.multiplyImageTitle)
{
    private val firstImage: Bitmap by lazy {
        this.obtainBitmapFromResources(R.drawable.body_costume)
    }
    private val secondImage: Bitmap by lazy { this.obtainBitmapFromResources(R.drawable.dice) }
    private val thirdImage: Bitmap by lazy { this.obtainBitmapFromResources(R.drawable.eye_green) }
    private val fourthImage: Bitmap by lazy { this.obtainBitmapFromResources(R.drawable.eye_blue) }

    override fun firstImageChoice(index: Int): Bitmap =
        when (index)
        {
            0 -> this.firstImage
            1 -> this.secondImage
            2 -> this.thirdImage
            3 -> this.fourthImage
            else -> this.firstImage
        }

    override fun secondImageChoice(index: Int): Bitmap =
        this.firstImageChoice(index)

    override fun doOperation(first: Bitmap, second: Bitmap): Bitmap
    {
        val result = this.createBitmap { bitmap, canvas, paint ->  }
        result.copy(first)
        result.multiply(second)
        return result
    }
}