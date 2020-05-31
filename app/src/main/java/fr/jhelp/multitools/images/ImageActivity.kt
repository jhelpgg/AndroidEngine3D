/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.multitools.images

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import fr.jhelp.images.COLOR_WHITE
import fr.jhelp.images.center
import fr.jhelp.images.clear
import fr.jhelp.multitools.R
import fr.jhelp.tasks.MainThread
import fr.jhelp.tasks.launch
import fr.jhelp.utilities.log

abstract class ImageActivity(@StringRes val titleId: Int) : FragmentActivity(),
    View.OnLayoutChangeListener
{
    private lateinit var imageView: ImageView
    private lateinit var bitmap: Bitmap
    private lateinit var canvas: Canvas
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_image)
        this.findViewById<TextView>(R.id.title).setText(this.titleId)

        this.obtainFragmentButtons()?.let { fragment ->
            log { "Transaction start" }
            val transaction = this.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.buttonsFragment, fragment)
            transaction.commit()
            log { "Transaction end" }
        }

        this.imageView = this.findViewById(R.id.image)
        this.imageView.addOnLayoutChangeListener(this)
    }

    open fun obtainFragmentButtons(): Fragment? = null

    override fun onLayoutChange(v: View, left: Int, top: Int, right: Int, bottom: Int,
                                oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int)
    {

        launch(v.width, v.height, this::initializeImage)
            .and(MainThread) { this.imageView.setImageBitmap(this.bitmap) }
            .and { this.doImageOperation(this.bitmap, this.canvas, this.paint) }
            .and(MainThread) { this.imageView.setImageBitmap(this.bitmap) }
    }

    fun obtainBitmap(@DrawableRes drawableID: Int): Bitmap
    {
        val options = BitmapFactory.Options()
        options.inScaled = false
        return BitmapFactory.decodeResource(this.resources, drawableID, options)
    }

    abstract fun doImageOperation(bitmap: Bitmap, canvas: Canvas, paint: Paint)

    fun refreshImage()
    {
        launch {
            this.bitmap.clear(0)
            this.canvas.center(this.obtainBitmap(R.drawable.body_costume),
                               this.bitmap.width / 2f, this.bitmap.height / 2f)
            this.doImageOperation(this.bitmap, this.canvas, this.paint)
        }.and(MainThread) { this.imageView.setImageBitmap(this.bitmap) }
    }

    private fun initializeImage(width: Int, height: Int)
    {
        this.bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        this.canvas = Canvas(this.bitmap)
        this.canvas.center(this.obtainBitmap(R.drawable.body_costume), width / 2f, height / 2f)
    }
}