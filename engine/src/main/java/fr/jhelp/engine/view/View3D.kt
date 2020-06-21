/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.engine.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.RectF
import android.opengl.GLSurfaceView
import android.os.SystemClock
import android.util.AttributeSet
import android.view.MotionEvent
import fr.jhelp.engine.resources.ResourcesAccess
import fr.jhelp.engine.scene.Node3D
import fr.jhelp.engine.scene.Point3D
import fr.jhelp.tasks.delay
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.max

/**
 * View with OpenGL inside.
 *
 * It shows the 3D
 */
class View3D(context: Context, attributes: AttributeSet? = null) :
    GLSurfaceView(context, attributes)
{
    private val alive = AtomicBoolean(true)
    private val renderer = View3DRenderer(this::refreshDone)
    private var startRefreshTime = 0L

    /**
     * Last touch X position
     */
    private var touchX = 0f

    /**
     * Last touch Y position
     */
    private var touchY = 0f

    val bounds3D get() = RectF(this.renderer.bound3D)
    val boundsView get() = RectF(this.renderer.boundView)

    /**
     * Scene draw on the view
     */
    val scene3D = this.renderer.scene3D

    /**
     * Current manipulated node
     */
    var manipulateNode: Node3D = this.scene3D.root

    init
    {
        ResourcesAccess.initialize(context.resources)
        this.setRenderer(this.renderer)
        // Render the view only when there is a change
        this.renderMode = RENDERMODE_WHEN_DIRTY
        delay(1024, this::refreshScene)
    }

    override fun onDetachedFromWindow()
    {
        this.alive.set(false)
        super.onDetachedFromWindow()
    }

    /**
     * Manipulate the scene root
     */
    fun manipulateRoo()
    {
        this.manipulateNode = this.scene3D.root
    }

    /**
     * Convert view coordinates to 3D coordinates.
     *
     * The `Z` value is fixed by the caller
     */
    fun screenCoordinateTo3D(xScreen: Float, yScreen: Float, zFix: Float): Point3D
    {
        val bound3D = this.renderer.bound3D
        val boundView = this.renderer.boundView
        val width3D: Float = bound3D.right - bound3D.left
        val height3D: Float = bound3D.bottom - bound3D.top
        val widthView: Float = boundView.right - boundView.left
        val heightView: Float = boundView.bottom - boundView.top
        return Point3D(bound3D.left + (width3D * xScreen) / widthView,
                       bound3D.top + (height3D * yScreen) / heightView,
                       zFix)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean
    {
        val x = event.x
        val y = event.y

        // Just form something happen now, TODO change it :
        if (event.action == MotionEvent.ACTION_MOVE)
        {
            this.manipulateNode.position.angleY += (x - this.touchX) * 0.25f
            this.manipulateNode.position.angleX += (y - this.touchY) * 0.25f
        }

        // TODO manage touche events for user interaction, depends on action mode
        // Example : angleY += (x - this.touchX)  | angleX += (y - this.touchY) => Fro a rotation mode
        // Can be also a virtual joystick
        // Object click detection
        // ...

        this.touchX = x
        this.touchY = y
        return true
    }

    private fun refreshScene()
    {
        if (!this.alive.get())
        {
            return
        }

        this.startRefreshTime = SystemClock.elapsedRealtime()
        this.requestRender()
    }

    private fun refreshDone()
    {
        if (!this.alive.get())
        {
            return
        }

        val timeLeft = max(1L, 32L - SystemClock.elapsedRealtime() + this.startRefreshTime)
        delay(timeLeft, this::refreshScene)
    }
}