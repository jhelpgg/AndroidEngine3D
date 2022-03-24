/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.animations

import fr.jhelp.tasks.ThreadType
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Launch function as animation
 */
fun animationTask(task: () -> Unit) =
    AnimationTask(Unit) { task() }

/**
 * Launch function as animation
 */
fun animationTask(threadType: ThreadType, task: () -> Unit) =
    AnimationTask(threadType, Unit) { task() }

/**
 * Create animation that launch a task
 */
class AnimationTask<P>(private val threadType: ThreadType, private val parameter: P,
                       private val task: (P) -> Unit) : Animation(25)
{
    constructor(parameter: P, task: (P) -> Unit) : this(ThreadType.SHORT, parameter, task)

    private val played = AtomicBoolean(false)

    override fun initialize()
    {
        this.played.set(false)
    }

    override fun animate(frame: Float): Boolean
    {
        if (!this.played.getAndSet(true))
        {
            this.threadType.parallel(this.parameter, this.task)
        }

        return false
    }
}