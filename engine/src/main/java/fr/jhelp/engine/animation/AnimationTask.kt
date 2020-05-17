package fr.jhelp.engine.animation

import fr.jhelp.tasks.IndependentThread
import fr.jhelp.tasks.ThreadType
import java.util.concurrent.atomic.AtomicBoolean

fun animationTask(task: () -> Unit) =
    AnimationTask(Unit) { task() }

fun animationTask(threadType: ThreadType, task: () -> Unit) =
    AnimationTask(threadType, Unit) { task() }

class AnimationTask<P>(private val threadType: ThreadType, private val parameter: P,
                       private val task: (P) -> Unit) : Animation(25)
{
    constructor(parameter: P, task: (P) -> Unit) : this(IndependentThread, parameter, task)

    private val played = AtomicBoolean(false)

    override fun initialize()
    {
        this.played.set(false)
    }

    override fun animate(frame: Float): Boolean
    {
        if (!this.played.getAndSet(true))
        {
            this.threadType(this.parameter, this.task)
        }

        return false
    }
}