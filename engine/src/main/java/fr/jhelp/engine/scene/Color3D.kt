/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.engine.scene

import fr.jhelp.engine.tools.floatBuffer
import fr.jhelp.images.COLOR_AMBER_0300
import fr.jhelp.images.COLOR_AMBER_0500
import fr.jhelp.images.COLOR_AMBER_0700
import fr.jhelp.images.COLOR_BLACK
import fr.jhelp.images.COLOR_BLUE_0300
import fr.jhelp.images.COLOR_BLUE_0500
import fr.jhelp.images.COLOR_BLUE_0700
import fr.jhelp.images.COLOR_BLUE_GREY_0300
import fr.jhelp.images.COLOR_BLUE_GREY_0500
import fr.jhelp.images.COLOR_BLUE_GREY_0700
import fr.jhelp.images.COLOR_BROWN_0300
import fr.jhelp.images.COLOR_BROWN_0500
import fr.jhelp.images.COLOR_BROWN_0700
import fr.jhelp.images.COLOR_CYAN_0300
import fr.jhelp.images.COLOR_CYAN_0500
import fr.jhelp.images.COLOR_CYAN_0700
import fr.jhelp.images.COLOR_GREEN_0300
import fr.jhelp.images.COLOR_GREEN_0500
import fr.jhelp.images.COLOR_GREEN_0700
import fr.jhelp.images.COLOR_GREY_0300
import fr.jhelp.images.COLOR_GREY_0500
import fr.jhelp.images.COLOR_GREY_0700
import fr.jhelp.images.COLOR_INDIGO_0300
import fr.jhelp.images.COLOR_INDIGO_0500
import fr.jhelp.images.COLOR_INDIGO_0700
import fr.jhelp.images.COLOR_LIME_0300
import fr.jhelp.images.COLOR_LIME_0500
import fr.jhelp.images.COLOR_LIME_0700
import fr.jhelp.images.COLOR_ORANGE_0300
import fr.jhelp.images.COLOR_ORANGE_0500
import fr.jhelp.images.COLOR_ORANGE_0700
import fr.jhelp.images.COLOR_PINK_0300
import fr.jhelp.images.COLOR_PINK_0500
import fr.jhelp.images.COLOR_PINK_0700
import fr.jhelp.images.COLOR_PURPLE_0300
import fr.jhelp.images.COLOR_PURPLE_0500
import fr.jhelp.images.COLOR_PURPLE_0700
import fr.jhelp.images.COLOR_RED_0300
import fr.jhelp.images.COLOR_RED_0500
import fr.jhelp.images.COLOR_RED_0700
import fr.jhelp.images.COLOR_TEAL_0300
import fr.jhelp.images.COLOR_TEAL_0500
import fr.jhelp.images.COLOR_TEAL_0700
import fr.jhelp.images.COLOR_WHITE
import fr.jhelp.images.COLOR_YELLOW_0300
import fr.jhelp.images.COLOR_YELLOW_0500
import fr.jhelp.images.COLOR_YELLOW_0700
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

    init
    {
        this.floatBuffer.put(this.red)
        this.floatBuffer.put(this.green)
        this.floatBuffer.put(this.blue)
        this.floatBuffer.put(this.alpha)
    }

    fun floatBuffer(): FloatBuffer
    {
        this.floatBuffer.rewind()
        return this.floatBuffer
    }
}

val WHITE = Color3D(COLOR_WHITE)
val LIGHT_GREY = Color3D(COLOR_GREY_0300)
val GREY = Color3D(COLOR_GREY_0500)
val DARK_GREY = Color3D(COLOR_GREY_0700)
val BLACK = Color3D(COLOR_BLACK)

val LIGHT_RED = Color3D(COLOR_RED_0300)
val RED = Color3D(COLOR_RED_0500)
val DARK_RED = Color3D(COLOR_RED_0700)

val LIGHT_GREEN = Color3D(COLOR_GREEN_0300)
val GREEN = Color3D(COLOR_GREEN_0500)
val DARK_GREEN = Color3D(COLOR_GREEN_0700)

val LIGHT_BLUE = Color3D(COLOR_BLUE_0300)
val BLUE = Color3D(COLOR_BLUE_0500)
val DARK_BLUE = Color3D(COLOR_BLUE_0700)

val LIGHT_YELLOW = Color3D(COLOR_YELLOW_0300)
val YELLOW = Color3D(COLOR_YELLOW_0500)
val DARK_YELLOW = Color3D(COLOR_YELLOW_0700)

val LIGHT_ORANGE = Color3D(COLOR_ORANGE_0300)
val ORANGE = Color3D(COLOR_ORANGE_0500)
val DARK_ORANGE = Color3D(COLOR_ORANGE_0700)

val LIGHT_PINK = Color3D(COLOR_PINK_0300)
val PINK = Color3D(COLOR_PINK_0500)
val DARK_PINK = Color3D(COLOR_PINK_0700)

val LIGHT_PURPLE = Color3D(COLOR_PURPLE_0300)
val PURPLE = Color3D(COLOR_PURPLE_0500)
val DARK_PURPLE = Color3D(COLOR_PURPLE_0700)

val LIGHT_CYAN = Color3D(COLOR_CYAN_0300)
val CYAN = Color3D(COLOR_CYAN_0500)
val DARK_CYAN = Color3D(COLOR_CYAN_0700)

val LIGHT_INDIGO = Color3D(COLOR_INDIGO_0300)
val INDIGO = Color3D(COLOR_INDIGO_0500)
val DARK_INDIGO = Color3D(COLOR_INDIGO_0700)

val LIGHT_BLUE_GREY = Color3D(COLOR_BLUE_GREY_0300)
val BLUE_GREY = Color3D(COLOR_BLUE_GREY_0500)
val DARK_BLUE_GREY = Color3D(COLOR_BLUE_GREY_0700)

val LIGHT_AMBER = Color3D(COLOR_AMBER_0300)
val AMBER = Color3D(COLOR_AMBER_0500)
val DARK_AMBER = Color3D(COLOR_AMBER_0700)

val LIGHT_BROWN = Color3D(COLOR_BROWN_0300)
val BROWN = Color3D(COLOR_BROWN_0500)
val DARK_BROWN = Color3D(COLOR_BROWN_0700)

val LIGHT_LIME = Color3D(COLOR_LIME_0300)
val LIME = Color3D(COLOR_LIME_0500)
val DARK_LIME = Color3D(COLOR_LIME_0700)

val LIGHT_TEAL = Color3D(COLOR_TEAL_0300)
val TEAL = Color3D(COLOR_TEAL_0500)
val DARK_TEAL = Color3D(COLOR_TEAL_0700)
