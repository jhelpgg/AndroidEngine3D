/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.animations.values

import fr.jhelp.animations.interpoolation.Interpolation
import fr.jhelp.animations.interpoolation.LinearInterpolation
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

abstract class AnimatedValue<V : Any, A : AnimationValue<V>>(value: V,
                                                             animationCreator: (reference: AtomicReference<V>) -> A)
{
    private val playing = AtomicBoolean(false)
    private val reference = AtomicReference<V>(value)
    val value: V
        get()
        {
            if (this.playing.get())
            {
                this.playing.set(this.animation.animate())
            }

            return this.reference.get()
        }
    private val animation = animationCreator(this.reference)
    val animating get() = this.playing.get()

    fun stop()
    {
        this.playing.set(false)
        this.animation.clear()
    }

    fun start()
    {
        this.playing.set(true)
        this.animation.start()
    }

    /**
     * Stop any animation and fix immediately the value
     */
    fun setValue(value: V)
    {
        this.stop()
        this.reference.set(value)
    }

    fun setValueIn(value: V, milliseconds: Int,
                   interpolation: Interpolation = LinearInterpolation,
                   stopAnimationIfPlaying: Boolean = true)
    {
        val stopAndRestartAnimation = stopAnimationIfPlaying && this.animating

        if (stopAndRestartAnimation)
        {
            this.stop()
        }

        this.animation.time(milliseconds, value, interpolation)

        if (stopAndRestartAnimation)
        {
            this.start()
        }
    }
}