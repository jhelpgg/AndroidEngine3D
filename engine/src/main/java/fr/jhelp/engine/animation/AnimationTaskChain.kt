/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.engine.animation

import fr.jhelp.tasks.chain.TaskChain
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Animation that start a task chain
 */
class AnimationTaskChain<P : Any, R : Any>(private val parameter: P,
                                           private val taskChain: TaskChain<P, R>) : Animation(25)
{
    private val played = AtomicBoolean(false)

    override fun initialize()
    {
        this.played.set(false)
    }

    override fun animate(frame: Float): Boolean
    {
        if (!this.played.getAndSet(true))
        {
            this.taskChain.emit(this.parameter)
        }

        return false
    }
}