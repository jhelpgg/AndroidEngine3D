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

/**
 * Generic animated value
 *
 * Since its an "animated" value, value may changed each time ist is requested wit [value]
 *
 * Animation can be [start] or [stop]
 *
 * Can fix the value with [setValue]. It stop the animation
 *
 * [setValueIn] permits to decide the animated value after a period of time after next start is called
 */
abstract class AnimatedValue<V : Any, A : AnimationValue<V>>(value: V,
                                                             animationCreator: (reference: AtomicReference<V>) -> A)
{
    private val playing = AtomicBoolean(false)
    private val reference = AtomicReference<V>(value)

    /**
     * Current value
     */
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

    /**
     * Indicates if there animation running
     */
    val animating get() = this.playing.get()

    /**
     * Stop current animation
     */
    fun stop()
    {
        this.playing.set(false)
        this.animation.clear()
    }

    /**
     * Start the animation
     */
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

    /**
     * Program a value at desired number of milliseconds after the animation started.
     * If animation currently running, their three scenario:
     * * `stopAnimationIfPlaying` is `true` : The animation stopped, value it set, animation restart from the beginning
     * * `stopAnimationIfPlaying` is `false`and the number of milliseconds is after the number of animation playing, then the value will be set à good time
     * * `stopAnimationIfPlaying` is `false and its too late, nothing happen
     */
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