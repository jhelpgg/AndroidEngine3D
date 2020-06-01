/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.multitools.images

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Path
import fr.jhelp.images.COLOR_BLACK
import fr.jhelp.images.clear
import fr.jhelp.images.copy
import fr.jhelp.images.mask
import fr.jhelp.multitools.R

class MaskedImageActivity : TwoImagesCombinationActivity(R.string.maskImageTitle)
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

    private val mask1Image: Bitmap =
        this.createBitmap { bitmap, canvas, paint ->
            bitmap.clear(0)
            paint.style = Paint.Style.FILL
            paint.color = COLOR_BLACK
            canvas.drawCircle(256f, 256f, 128f, paint)
        }
    private val mask2Image: Bitmap =
        this.createBitmap { bitmap, canvas, paint ->
            bitmap.clear(0)
            paint.style = Paint.Style.FILL
            paint.color = COLOR_BLACK
            canvas.drawRect(32f, 32f, 300f, 250f, paint)
            canvas.drawOval(64f, 256f, 500f, 400f, paint)
        }
    private val mask3Image: Bitmap =
        this.createBitmap { bitmap, canvas, paint ->
            bitmap.clear(0)
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 64f
            paint.color = COLOR_BLACK
            canvas.drawCircle(256f, 256f, 192f, paint)
        }
    private val mask4Image: Bitmap =
        this.createBitmap { bitmap, canvas, paint ->
            bitmap.clear(0)
            paint.style = Paint.Style.FILL
            paint.color = COLOR_BLACK
            val path = Path()
            path.moveTo(200f, 12f)
            path.lineTo(300f, 256f)
            path.lineTo(200f, 500f)
            path.lineTo(100f, 256f)
            path.close()
            canvas.drawPath(path, paint)
        }

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
            0    -> this.mask1Image
            1    -> this.mask2Image
            2    -> this.mask3Image
            3    -> this.mask4Image
            else -> this.mask1Image
        }

    override fun doOperation(first: Bitmap, second: Bitmap): Bitmap
    {
        val result = this.createBitmap { bitmap, canvas, paint -> }
        result.copy(first)
        result.mask(second, 0, 0, 512, 512)
        return result
    }
}