package fr.jhelp.engine.scene

import fr.jhelp.engine.tools.floatBuffer
import fr.jhelp.images.alpha
import fr.jhelp.images.blue
import fr.jhelp.images.green
import fr.jhelp.images.red
import java.nio.FloatBuffer

data class Color3D(val red: Float, val green: Float, val blue: Float, val alpha: Float = 1f)
{
    private val floatBuffer = floatBuffer(4)

    constructor(grey: Float, alpha: Float = 1f) : this(grey, grey, grey, alpha)
    constructor(color: Int) : this(color.red / 255f, color.green / 255f, color.blue / 255f,
                                   color.alpha / 255f)

    fun floatBuffer(): FloatBuffer
    {
        this.floatBuffer.rewind()
        this.floatBuffer.put(this.red)
        this.floatBuffer.put(this.green)
        this.floatBuffer.put(this.blue)
        this.floatBuffer.put(this.alpha)
        this.floatBuffer.rewind()
        return this.floatBuffer
    }
}

val WHITE = Color3D(1f)
val BLACK = Color3D(0f)
val GREY = Color3D(0.5f)
val LIGHT_GREY = Color3D(0.75f)
val DARK_GREY = Color3D(0.25f)

val RED = Color3D(1f, 0f, 0f)
val GREEN = Color3D(0f, 1f, 0f)
val BLUE = Color3D(0f, 0f, 1f)

// TODO other basic colors