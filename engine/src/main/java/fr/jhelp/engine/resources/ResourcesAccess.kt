package fr.jhelp.engine.resources

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import fr.jhelp.engine.scene.Texture
import fr.jhelp.engine.scene.defaultTexture
import fr.jhelp.engine.scene.texture
import java.lang.ref.WeakReference

object ResourcesAccess
{
    private lateinit var resources: WeakReference<Resources>

    internal fun initialize(resources: Resources)
    {
        this.resources = WeakReference(resources)
    }

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

    fun obtainTexture(@DrawableRes drawableID: Int, sealed: Boolean = true): Texture =
        this.resources.get()?.let { resources -> texture(resources, drawableID, sealed) }
        ?: defaultTexture()

    fun obtainBitmap(@DrawableRes drawableID: Int): Bitmap =
        this.resources.get()?.let { resources ->
            val options = BitmapFactory.Options()
            options.inScaled = false
            BitmapFactory.decodeResource(resources, drawableID, options)
        }
        ?: defaultBitmap()

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