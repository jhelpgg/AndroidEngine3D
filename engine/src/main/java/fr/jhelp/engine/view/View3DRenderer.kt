/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.engine.view

import android.graphics.RectF
import android.opengl.GLSurfaceView
import fr.jhelp.engine.scene.Scene3D
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Renderer used to initialize OpenGL and update it
 */
internal class View3DRenderer(private val refreshDone: () -> Unit) : GLSurfaceView.Renderer
{
    val boundView = RectF()
    val bound3D = RectF()
    val scene3D = Scene3D()

    override fun onDrawFrame(gl: GL10)
    {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT)
        gl.glMatrixMode(GL10.GL_MODELVIEW)
        this.scene3D.render(gl)
        this.refreshDone()
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int)
    {
        gl.glViewport(0, 0, width, height)

        val ratio = width.toFloat() / height
        gl.glMatrixMode(GL10.GL_PROJECTION)
        gl.glMatrixMode(GL10.GL_MODELVIEW)
        gl.glLoadIdentity()
        gl.glFrustumf(-ratio, ratio, -1f, 1f, 1f, 10f)

        this.bound3D.left = -ratio
        this.bound3D.right = ratio
        this.bound3D.top = 1f
        this.bound3D.bottom = -1f

        this.boundView.left = 0f
        this.boundView.right = width.toFloat()
        this.boundView.top = 0f
        this.boundView.bottom = height.toFloat()

    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?)
    {
        gl.glDisable(GL10.GL_DITHER)
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST)
        gl.glClearColor(1f, 1f, 1f, 1f)
        gl.glEnable(GL10.GL_DEPTH_TEST)
        gl.glEnable(GL10.GL_ALPHA_TEST)
        // Set alpha precision
        gl.glAlphaFunc(GL10.GL_GREATER, 0.01f)
        // Way to compute alpha
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA)

        // We accept blending
        gl.glEnable(GL10.GL_BLEND)
        gl.glEnable(GL10.GL_COLOR_MATERIAL)

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY)
    }
}