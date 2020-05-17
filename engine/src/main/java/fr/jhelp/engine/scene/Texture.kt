package fr.jhelp.engine.scene

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.DrawableRes
import fr.jhelp.engine.resources.ResourcesAccess
import fr.jhelp.engine.tools.byteBuffer
import fr.jhelp.engine.tools.intBuffer
import fr.jhelp.utilities.log2
import java.io.InputStream
import java.nio.ByteBuffer
import java.util.concurrent.atomic.AtomicBoolean
import javax.microedition.khronos.opengles.GL10
import kotlin.math.max
import kotlin.math.min

fun defaultTexture(): Texture =
    texture(ResourcesAccess.defaultBitmap())

fun texture(resources: Resources, @DrawableRes resourceID: Int, sealed: Boolean = true): Texture
{
    val options = BitmapFactory.Options()
    options.inScaled = false
    return BitmapFactory.decodeResource(resources, resourceID, options)
               ?.let { bitmap -> texture(bitmap, sealed) } ?: defaultTexture()
}

fun texture(@TextureSize width: Int, @TextureSize height: Int): Texture
{
    val goodWidth = 1 shl min(9, log2(max(1, width)))
    val goodHeight = 1 shl min(9, log2(max(1, height)))
    return Texture(Bitmap.createBitmap(goodWidth, goodHeight, Bitmap.Config.ARGB_8888), false)
}

fun texture(inputStream: InputStream, sealed: Boolean = true): Texture
{
    val options = BitmapFactory.Options()
    options.inScaled = false
    options.inJustDecodeBounds = true
    BitmapFactory.decodeStream(inputStream, null, options)
    val width: Int = options.outWidth
    val log2Width: Int = log2(width)
    val height: Int = options.outHeight
    val log2Height: Int = log2(height)
    val log2 = max(log2Width, log2Height)
    options.inJustDecodeBounds = false
    options.inSampleSize = 1

    if (log2 > 9)
    {
        options.inSampleSize = 1 shl (log2 - 9)
    }

    return BitmapFactory.decodeStream(inputStream, null, options)
               ?.let { bitmap -> texture(bitmap, sealed) }
           ?: defaultTexture()
}

fun texture(bitmap: Bitmap, sealed: Boolean = true): Texture
{
    val width = bitmap.width
    val height = bitmap.height
    val goodWidth = 1 shl min(9, log2(width))
    val goodHeight = 1 shl min(9, log2(height))

    return if (width != goodWidth || height != goodHeight)
    {
        val resized = Bitmap.createScaledBitmap(bitmap, goodWidth, goodHeight, false)
        bitmap.recycle()
        Texture(resized, sealed)
    }
    else
    {
        Texture(bitmap, sealed)
    }
}

class Texture internal constructor(bitmap: Bitmap, sealed: Boolean) : Object()
{
    private val intBuffer = intBuffer(1)
    private val sealed = AtomicBoolean(false)
    private var pixels: ByteBuffer? = null
    private var bitmap: Bitmap? = null
    val width: Int
    val height: Int
    private var videoMemoryId = -1
    private var dirty = true
    private var canvas: Canvas? = null
    private var paint: Paint? = null

    init
    {
        this.sealed.set(sealed)
        this.width = bitmap.width
        this.height = bitmap.height
        this.pixels = byteBuffer((this.width * this.height) shl 2)

        if (sealed)
        {
            bitmap.copyPixelsToBuffer(this.pixels)
            this.pixels!!.position(0)
            bitmap.recycle()
        }
        else
        {
            if (bitmap.isMutable)
            {
                this.bitmap = bitmap
            }
            else
            {
                val width = bitmap.width
                val height = bitmap.height
                this.bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                val pixels = IntArray(width * height)
                bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
                this.bitmap!!.setPixels(pixels, 0, width, 0, 0, width, height)
                bitmap.recycle()
            }
        }

        System.gc()
    }

    fun sealed() =
        this.sealed.get()

    fun bitmap(): Bitmap? =
        if (this.sealed.get()) null
        else this.bitmap

    fun canvas(): Canvas?
    {
        if (this.sealed.get())
        {
            return null
        }

        if (this.canvas == null)
        {
            this.canvas = Canvas(this.bitmap!!)
        }

        return this.canvas
    }

    fun paint(): Paint?
    {
        if (this.sealed.get())
        {
            return null
        }

        if (this.paint == null)
        {
            this.paint = Paint()
        }

        return this.paint
    }

    fun refresh()
    {
        if (!this.sealed.get())
        {
            this.dirty = true
        }
    }

    fun seal()
    {
        if (!this.sealed.getAndSet(true))
        {
            this.bitmap!!.copyPixelsToBuffer(this.pixels)
            this.pixels!!.position(0)
            this.bitmap!!.recycle()
            this.bitmap = null
            this.canvas = null
            this.paint = null
            System.gc()
            this.dirty = true
        }
    }

    internal fun bind(gl: GL10)
    {
        if (this.videoMemoryId < 0)
        {
            this.intBuffer.rewind()
            this.intBuffer.put(1)
            this.intBuffer.rewind()
            gl.glGenTextures(1, this.intBuffer)
            this.intBuffer.rewind()
            this.videoMemoryId = this.intBuffer.get()
        }

        val pixels = this.pixels

        if (this.dirty && pixels != null)
        {
            if (!this.sealed.get())
            {
                pixels.clear()
                this.bitmap!!.copyPixelsToBuffer(pixels)
            }

            pixels.position(0)

            // Push pixels in video memory
            gl.glBindTexture(GL10.GL_TEXTURE_2D, this.videoMemoryId)
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR)
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR)
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT)
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT)
            gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, this.width, this.height, 0,
                            GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, pixels)

            if (this.sealed.get())
            {
                pixels.clear()
                this.pixels = null
            }
        }

        gl.glBindTexture(GL10.GL_TEXTURE_2D, this.videoMemoryId)
        this.dirty = false
    }

    override fun finalize()
    {
        this.bitmap?.recycle()
        DeleteTexture.freeTexture(this.videoMemoryId)
        super.finalize()
    }
}