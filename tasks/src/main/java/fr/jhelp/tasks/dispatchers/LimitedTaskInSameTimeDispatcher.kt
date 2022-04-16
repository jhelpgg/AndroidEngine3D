/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.tasks.dispatchers

import fr.jhelp.lists.Queue
import fr.jhelp.tasks.Mutex
import fr.jhelp.utilities.bounds
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

internal class LimitedTaskInSameTimeDispatcher(maximumTaskInSameTime: Int = 8) :
    CoroutineDispatcher()
{
    companion object
    {
        private val global = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        private fun execute(task: () -> Unit)
        {
            LimitedTaskInSameTimeDispatcher.global.launch {
                task()
            }
        }
    }

    private var numberFreeTask = maximumTaskInSameTime.bounds(2, 16)
    private val waitingTasks = Queue<Runnable>()
    private val mutex = Mutex()

    override fun dispatch(context: CoroutineContext, block: Runnable)
    {
        this.mutex {
            this.waitingTasks.enqueue(block)

            if (this.numberFreeTask > 0)
            {
                this.numberFreeTask--
                LimitedTaskInSameTimeDispatcher.execute(this::nextTask)
            }
        }
    }

    private fun nextTask()
    {
        var task: Runnable? = null

        do
        {
            this.mutex {
                if (this.waitingTasks.empty)
                {
                    task = null
                }
                else
                {
                    task = this.waitingTasks.dequeue()
                }
            }

            if (task != null)
            {
                try
                {
                    task!!.run()
                }
                catch (_: Exception)
                {
                }
            }
        }
        while (task != null)

        this.mutex { this.numberFreeTask++ }
    }
}
