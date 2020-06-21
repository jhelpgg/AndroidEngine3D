/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.engine.scene

import javax.microedition.khronos.opengles.GL10

/**
 * Material composed of alpha, diffuse and [Texture]
 */
class Material
{
    var alpha = 1f
    var diffuse = GREY
    var texture: Texture? = null

    internal fun render(gl: GL10)
    {
        gl.glDisable(GL10.GL_TEXTURE_2D)
        gl.glColor4f(this.diffuse.red, this.diffuse.green, this.diffuse.blue, this.alpha)
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, this.diffuse.floatBuffer())

        this.texture?.let { text ->
            gl.glEnable(GL10.GL_TEXTURE_2D)
            text.bind(gl)
        }
    }
}