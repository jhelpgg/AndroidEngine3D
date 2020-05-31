/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.tasks.taskQueue

import fr.jhelp.lists.Queue
import fr.jhelp.tasks.parallel
import java.util.concurrent.atomic.AtomicBoolean

private const val EXIT_TIME = 34_567L

/**
 * Queue of tasks do one after other.
 *
 * Order and thread safe are guarantee.
 *
 * TaskQueue enqueue keys.
 * When the turn comes, it use given creators with the the key to have the task and its parameter to execute
 */
class TaskQueue<K, P, R>(private val taskCreator: (K) -> (P) -> R,
                         private val parameterProvider: (K) -> P)
{
    private val taskQueue = Queue<K>()
    private val lock = Object()
    private val running = AtomicBoolean(false)
    private val waiting = AtomicBoolean(false)

    /**
     * Enqueue task by its key.
     *
     * It will be play at its turn
     */
    fun enqueue(key: K)
    {
        synchronized(this.taskQueue)
        {
            this.taskQueue.enqueue(key)
        }

        this.wakeup()
    }

    private fun wakeup()
    {
        synchronized(this.lock)
        {
            when
            {
                !this.running.getAndSet(true) -> parallel(this::run)
                this.waiting.get()            -> this.lock.notify()
            }

            Unit
        }
    }

    private fun run()
    {
        var key: K?
        var waitBeforeExit = true

        do
        {
            synchronized(this.taskQueue)
            {
                if (this.taskQueue.empty)
                {
                    key = null
                }
                else
                {
                    key = this.taskQueue.dequeue()
                    waitBeforeExit = true
                }
            }

            if (key != null)
            {
                val keyNotNull = key!!
                val task = this.taskCreator(keyNotNull)
                val parameter = this.parameterProvider(keyNotNull)

                try
                {
                    task(parameter)
                }
                catch (_: Exception)
                {
                }
            }
            else if (waitBeforeExit)
            {
                synchronized(this.lock)
                {
                    this.waiting.set(true)
                    this.lock.wait(EXIT_TIME)
                    this.waiting.set(false)
                }

                synchronized(this.taskQueue)
                {
                    waitBeforeExit = this.taskQueue.notEmpty
                }
            }
        }
        while (waitBeforeExit)

        this.running.set(false)
    }
}