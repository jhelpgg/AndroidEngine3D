/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.engine.resources

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import fr.jhelp.engine.scene.Texture
import fr.jhelp.engine.scene.defaultTexture
import fr.jhelp.engine.scene.texture
import java.lang.ref.WeakReference

/**
 * Acces to resources.
 *
 * Work only if [fr.jhelp.engine.view.View3D] is on activity
 */
object ResourcesAccess
{
    private lateinit var resources: WeakReference<Resources>

    internal fun initialize(resources: Resources)
    {
        this.resources = WeakReference(resources)
    }

    /**
     * Create default Bitmap
     */
    fun defaultBitmap(): Bitmap
    {
        val bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888)
        val pixels = IntArray(4)
        pixels[0] = 0xFFFFFFFF.toInt()
        pixels[1] = 0xFF000000.toInt()
        pixels[2] = 0xFF000000.toInt()
        pixels[3] = 0xFFFFFFFF.toInt()
        bitmap.setPixels(pixels, 0, 2, 0, 0, 2, 2)
        return bitmap
    }

    /**
     * Obtain texture from drawable resources
     */
    fun obtainTexture(@DrawableRes drawableID: Int, sealed: Boolean = true): Texture =
        this.resources.get()?.let { resources -> texture(resources, drawableID, sealed) }
        ?: defaultTexture()

    /**
     * Obtain bitmap from drawable resources
     */
    fun obtainBitmap(@DrawableRes drawableID: Int): Bitmap =
        this.resources.get()?.let { resources ->
            val options = BitmapFactory.Options()
            options.inScaled = false
            BitmapFactory.decodeResource(resources, drawableID, options)
        }
        ?: defaultBitmap()

    /**
     * Obtain bitmap from drawable resources and resize it to fit the requested dimensions
     */
    fun obtainBitmap(@DrawableRes drawableID: Int, width: Int, height: Int): Bitmap
    {
        val bitmap = this.obtainBitmap(drawableID)

        if (bitmap.width != width || bitmap.height != height)
        {
            val scaled = Bitmap.createScaledBitmap(bitmap, width, height, false)
            bitmap.recycle()
            return scaled
        }

        return bitmap
    }
}