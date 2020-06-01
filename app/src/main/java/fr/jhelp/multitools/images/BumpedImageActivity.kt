/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.multitools.images

import android.graphics.Bitmap
import fr.jhelp.images.createBumped
import fr.jhelp.multitools.R

class BumpedImageActivity : TwoImagesCombinationActivity(R.string.bumpImageTitle)
{
    private val firstImage: Bitmap by lazy {
        this.obtainBitmapFromResources(R.drawable.body_costume)
    }
    private val secondImage: Bitmap by lazy { this.obtainBitmapFromResources(R.drawable.floor) }
    private val thirdImage: Bitmap by lazy {
        this.obtainBitmapFromResources(R.drawable.default_screen)
    }
    private val fourthImage: Bitmap by lazy {
        this.obtainBitmapFromResources(R.drawable.emerald_bk)
    }
    private val bump1Image: Bitmap by lazy { this.obtainBitmapFromResources(R.drawable.bump1) }
    private val bump2Image: Bitmap by lazy { this.obtainBitmapFromResources(R.drawable.bump2) }
    private val bump3Image: Bitmap by lazy { this.obtainBitmapFromResources(R.drawable.bump3) }
    private val bump4Image: Bitmap by lazy { this.obtainBitmapFromResources(R.drawable.bump4) }

    override fun firstImageChoice(index: Int): Bitmap =
        when (index)
        {
            0    -> this.firstImage
            1    -> this.secondImage
            2    -> this.thirdImage
            3    -> this.fourthImage
            else -> this.firstImage
        }

    override fun secondImageChoice(index: Int): Bitmap =
        when (index)
        {
            0    -> this.bump1Image
            1    -> this.bump2Image
            2    -> this.bump3Image
            3    -> this.bump4Image
            else -> this.bump1Image
        }

    override fun doOperation(first: Bitmap, second: Bitmap): Bitmap =
        createBumped(first, second)
}