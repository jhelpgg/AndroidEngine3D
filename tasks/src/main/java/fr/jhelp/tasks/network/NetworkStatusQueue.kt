package fr.jhelp.tasks.network

import fr.jhelp.lists.Queue
import fr.jhelp.tasks.Cancelable
import fr.jhelp.tasks.cancellable
import fr.jhelp.tasks.parallel
import java.util.concurrent.atomic.AtomicBoolean

internal object NetworkStatusQueue
{
    private val alive = AtomicBoolean(false)
    private val tasks = Queue<() -> Unit>()

    init
    {
        NetworkStatusCallback.availableObservable.observe(this::networkAvailable)
    }

    fun add(task: () -> Unit): Cancelable
    {
        synchronized(this.tasks)
        {
            this.tasks.enqueue(task)
        }

        this.wakeup(false)

        return {
            synchronized(this.tasks)
            {
                this.tasks.removeIf { task == it }
            }
        }.cancellable()
    }

    private fun wakeup(networkStatus: Boolean)
    {
        if ((networkStatus || NetworkStatusCallback.availableObservable.value())
            && !this.alive.getAndSet(true))
        {
            synchronized(this.tasks)
            {
                if (this.tasks.notEmpty)
                {
                    parallel(this::run)
                }
            }
        }
    }

    private fun run()
    {
        while (this.alive.get())
        {
            val task =
                synchronized(this.tasks)
                {
                    if (this.tasks.empty) null
                    else this.tasks.dequeue()
                }

            if (task != null)
            {
                try
                {
                    task()
                }
                catch (_: Exception)
                {
                    // If exception happen and connection was lost,
                    // the failure is probably due network lost while doing the task.
                    // In this case we will retry do the action next time network comes back
                    if (!NetworkStatusCallback.availableObservable.value())
                    {
                        synchronized(this.tasks)
                        {
                            this.tasks.ahead(task)
                        }
                    }
                }
            }
            else
            {
                this.alive.set(false)
            }
        }
    }

    private fun networkAvailable(available: Boolean)
    {
        if (available)
        {
            this.wakeup(true)
        }
        else
        {
            this.alive.set(false)
        }
    }

    internal fun stop()
    {
        synchronized(this.tasks)
        {
            this.tasks.clear()
        }

        this.alive.set(false)
    }
}