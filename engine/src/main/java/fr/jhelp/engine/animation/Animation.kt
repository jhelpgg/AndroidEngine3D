package fr.jhelp.engine.animation

import android.os.SystemClock
import fr.jhelp.engine.OpenGLThread
import fr.jhelp.utilities.bounds
import kotlin.math.max
import kotlin.math.min

abstract class Animation(fps: Int)
{
    val fps = fps.bounds(1, 100)
    private var statTime = 0L

    protected open fun initialize() = Unit

    @OpenGLThread
    protected abstract fun animate(frame: Float): Boolean

    open fun finished() = Unit

    internal fun start()
    {
        this.statTime = SystemClock.uptimeMillis()
        this.initialize()
        this.animate(0f)
    }

    @OpenGLThread
    internal fun animate(): Boolean =
        this.animate(((SystemClock.uptimeMillis() - this.statTime) * this.fps) / 1000f)

    fun millisecondsToFrame(milliseconds: Int): Int = (milliseconds * this.fps) / 1000
}