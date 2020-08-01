/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.tasks.delay

import android.os.SystemClock
import fr.jhelp.tasks.ThreadType
import fr.jhelp.tasks.parallel
import fr.jhelp.tasks.promise.FutureResult
import fr.jhelp.tasks.promise.Promise
import java.util.PriorityQueue
import java.util.concurrent.atomic.AtomicBoolean

private val EXIT_TIME = 34_567L

/**
 * Delayed task manager
 */
internal object Delay
{
    private val queue = PriorityQueue<DelayElement>()
    private val lock = Object()
    private val running = AtomicBoolean(false)
    private val waiting = AtomicBoolean(false)

    fun <R : Any> delay(threadType: ThreadType, milliseconds: Long,
                        action: () -> R): FutureResult<R>
    {
        val promise = Promise<R>()
        return this.add(promise,
                        DelayElement(
                            {
                                if (!promise.canceled)
                                {
                                    try
                                    {
                                        promise.result(action())
                                    }
                                    catch (exception: Exception)
                                    {
                                        promise.error(exception)
                                    }
                                }
                            },
                            SystemClock.elapsedRealtime() + milliseconds,
                            threadType))
    }

    fun <P, R : Any> delay(threadType: ThreadType, milliseconds: Long, parameter: P,
                           action: (P) -> R): FutureResult<R>
    {
        val promise = Promise<R>()
        return this.add(promise, DelayElement(
            {
                if (!promise.canceled)
                {
                    try
                    {
                        promise.result(action(parameter))
                    }
                    catch (exception: Exception)
                    {
                        promise.error(exception)
                    }
                }
            },
            SystemClock.elapsedRealtime() + milliseconds,
            threadType))
    }

    fun <P1, P2, R : Any> delay(threadType: ThreadType, milliseconds: Long,
                                parameter1: P1, parameter2: P2,
                                action: (P1, P2) -> R): FutureResult<R>
    {
        val promise = Promise<R>()
        return this.add(promise,
                        DelayElement(
                            {
                                if (!promise.canceled)
                                {
                                    try
                                    {
                                        promise.result(action(parameter1, parameter2))
                                    }
                                    catch (exception: Exception)
                                    {
                                        promise.error(exception)
                                    }
                                }
                            },
                            SystemClock.elapsedRealtime() + milliseconds,
                            threadType))
    }

    fun <P1, P2, P3, R : Any> delay(threadType: ThreadType, milliseconds: Long,
                                    parameter1: P1, parameter2: P2, parameter3: P3,
                                    action: (P1, P2, P3) -> R): FutureResult<R>
    {
        val promise = Promise<R>()
        return this.add(promise,
                        DelayElement(
                            {
                                if (!promise.canceled)
                                {
                                    try
                                    {
                                        promise.result(action(parameter1, parameter2, parameter3))
                                    }
                                    catch (exception: Exception)
                                    {
                                        promise.error(exception)
                                    }
                                }
                            },
                            SystemClock.elapsedRealtime() + milliseconds,
                            threadType))
    }

    private fun <R : Any> add(promise: Promise<R>, delayElement: DelayElement): FutureResult<R>
    {
        promise.register {
            synchronized(this.queue)
            {
                this.queue.remove(delayElement)
            }
        }

        synchronized(this.queue)
        {
            this.queue.offer(delayElement)
        }

        this.wakeup()
        return promise.future
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
        var delayElement: DelayElement?
        var waitBeforeExit = true

        do
        {
            synchronized(this.queue)
            {
                if (this.queue.isEmpty())
                {
                    delayElement = null
                }
                else
                {
                    delayElement = this.queue.peek()
                    waitBeforeExit = true
                }
            }


            if (delayElement != null)
            {
                val element = delayElement as DelayElement
                val timeLeft = element.time - SystemClock.elapsedRealtime()

                if (timeLeft <= 0)
                {
                    synchronized(this.queue)
                    {
                        this.queue.remove(element)
                    }

                    element.threadType(element.action)
                }
                else
                {
                    synchronized(this.lock)
                    {
                        this.waiting.set(true)
                        this.lock.wait(timeLeft)
                        this.waiting.set(false)
                    }
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

                synchronized(this.queue)
                {
                    waitBeforeExit = this.queue.isNotEmpty()
                }
            }
        }
        while (waitBeforeExit)

        this.running.set(false)
    }
}