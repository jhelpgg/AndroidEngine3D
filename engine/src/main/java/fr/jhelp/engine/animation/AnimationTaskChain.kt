package fr.jhelp.engine.animation

import fr.jhelp.tasks.chain.TaskChain
import java.util.concurrent.atomic.AtomicBoolean

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