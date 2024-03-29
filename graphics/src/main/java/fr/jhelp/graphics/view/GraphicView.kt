/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.graphics.view

import android.content.Context
import android.graphics.Canvas
import android.os.SystemClock
import android.util.AttributeSet
import android.view.View
import fr.jhelp.tasks.delay
import fr.jhelp.tasks.promise.FutureResult
import fr.jhelp.utilities.WaitingLock
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.max

abstract class GraphicView(context: Context, attributes: AttributeSet?, defaultStyleAttributes: Int,
                           defaultStyleResources: Int) :
    View(context, attributes, defaultStyleAttributes, defaultStyleResources)
{
    private val onAnimation = AtomicBoolean(false)
    private lateinit var futureAnimation: FutureResult<Unit>
    private val animationLock = WaitingLock()
    var animated: Boolean = true
        private set

    constructor(context: Context, attributes: AttributeSet?, defaultStyleAttributes: Int) :
            this(context, attributes, defaultStyleAttributes, 0)

    constructor(context: Context, attributes: AttributeSet?) :
            this(context, attributes, 0, 0)

    constructor(context: Context) :
            this(context, null, 0, 0)

    fun setAnimated(animated: Boolean)
    {
        if (this.animated != animated)
        {
            this.animated = animated

            if (this.animated)
            {
                this.startAnimation()
            }
            else
            {
                this.stopAnimation()
            }
        }
    }

    protected fun refreshView()
    {
        if (this.animated)
        {
            this.startAnimation()
        }
        else
        {
            this.updateValues()
            this.postInvalidate()
        }
    }

    private fun startAnimation()
    {
        if (!this.onAnimation.getAndSet(true))
        {
            this.animationStarted()
            this.futureAnimation = this::playAnimation.delay(256L)
            this.futureAnimation.onCancel { this.animationLock.unlock() }
        }
    }

    private fun stopAnimation()
    {
        if (this.onAnimation.getAndSet(false))
        {
            this.animationStopped()
            this.futureAnimation.cancel("stop")
            this.postInvalidate()
        }
    }

    final override fun onDraw(canvas: Canvas)
    {
        super.onDraw(canvas)
        this.drawing(canvas)

        if (this.onAnimation.get())
        {
            this.animationLock.unlock()
        }
    }

    private fun playAnimation()
    {
        if (this.updateValues())
        {
            val time = SystemClock.elapsedRealtime()
            this.postInvalidate()
            this.animationLock.lock()

            if (this.onAnimation.get())
            {
                this.futureAnimation =
                    this::playAnimation.delay(max(1L, 32L - (SystemClock.elapsedRealtime() - time)))
                this.futureAnimation.onCancel { this.animationLock.unlock() }
            }
        }
        else
        {
            this.onAnimation.set(false)
            this.postInvalidate()
        }
    }

    protected abstract fun animationStarted()
    protected abstract fun animationStopped()
    protected abstract fun updateValues(): Boolean
    protected abstract fun drawing(canvas: Canvas)

    override fun onAttachedToWindow()
    {
        super.onAttachedToWindow()

        if (this.animated)
        {
            this.startAnimation()
        }
    }

    override fun onDetachedFromWindow()
    {
        if (this.onAnimation.getAndSet(false))
        {
            this.futureAnimation.cancel("detached")
        }

        super.onDetachedFromWindow()
    }

}