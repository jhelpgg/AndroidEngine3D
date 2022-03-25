/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.animations

import android.os.SystemClock
import fr.jhelp.utilities.bounds

/**
 * A generic animation
 */
abstract class Animation(fps: Int)
{
    val fps = fps.bounds(1, 100)
    private var statTime = 0L

    /**
     * Called when animation about to start.
     *
     * Does nothing by default
     */
    protected open fun initialize() = Unit

    /**
     * Play animation for given frame.
     *
     * @return `true` if the animation continue. `false` if animation is finished
     */
    protected abstract fun animate(frame: Float): Boolean

    /**
     * Called when animation finished
     *
     * Does nothing by default
     */
    open fun finished() = Unit

    /**
     * Start the animation
     */
    fun start()
    {
        this.statTime = SystemClock.uptimeMillis()
        this.initialize()
        this.animate(0f)
    }

    /**
     * Animate the animation, it computes the value depends the last time [start] was called
     *
     *  @return `true` if the animation continue. `false` if animation is finished
     */
    fun animate(): Boolean =
        this.animate(((SystemClock.uptimeMillis() - this.statTime) * this.fps) / 1000f)

    /**
     * Transform time to number of frame, depends on animation FPS
     */
    fun millisecondsToFrame(milliseconds: Int): Int = (milliseconds * this.fps) / 1000
}