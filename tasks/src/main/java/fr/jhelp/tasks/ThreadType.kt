/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.tasks

import fr.jhelp.tasks.dispatchers.LimitedTaskInSameTimeDispatcher
import fr.jhelp.tasks.dispatchers.NetworkDispatcher
import fr.jhelp.tasks.promise.FutureResult
import fr.jhelp.tasks.promise.Promise
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.CancellationException
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.CoroutineContext
import kotlin.math.max

/**
 * Type of thread.
 *
 * Task are executed in specific thread type
 */
enum class ThreadType(private val coroutineScope: CoroutineScope,
                      private val coroutineContext: CoroutineContext)
{
    UI(MainScope(), Dispatchers.Main),
    IO(CoroutineScope(LimitedTaskInSameTimeDispatcher(4)), Dispatchers.IO),
    SHORT(CoroutineScope(LimitedTaskInSameTimeDispatcher(8)), Dispatchers.Default),
    HEAVY(CoroutineScope(LimitedTaskInSameTimeDispatcher(4)), Dispatchers.Default),
    NETWORK(CoroutineScope(NetworkDispatcher), Dispatchers.Default)
    ;

    fun <R : Any> parallel(task: () -> R): FutureResult<R>
    {
        val promise = Promise<R>()
        val cancelled = AtomicBoolean(false)

        val job = this.coroutineScope.launch {
            withContext(this.coroutineContext)
            {
                if (cancelled.get())
                {
                    return@withContext
                }

                try
                {
                    promise.result(task())
                }
                catch (exception: Exception)
                {
                    promise.error(exception)
                }
            }
        }

        promise.register { reason ->
            cancelled.set(true)
            job.cancel(CancellationException(reason))
        }
        return promise.future
    }

    fun <P, R : Any> parallel(parameter: P, task: (P) -> R): FutureResult<R>
    {
        val promise = Promise<R>()
        val cancelled = AtomicBoolean(false)

        val job = this.coroutineScope.launch {
            withContext(this.coroutineContext)
            {
                if (cancelled.get())
                {
                    return@withContext
                }

                try
                {
                    promise.result(task(parameter))
                }
                catch (exception: Exception)
                {
                    promise.error(exception)
                }
            }
        }

        promise.register { reason ->
            cancelled.set(true)
            job.cancel(CancellationException(reason))
        }
        return promise.future
    }

    fun <R : Any> delay(milliseconds: Long, task: () -> R): FutureResult<R>
    {
        val promise = Promise<R>()
        val cancelled = AtomicBoolean(false)

        val job = this.coroutineScope.launch {

            delay(max(1L, milliseconds))

            withContext(this.coroutineContext)
            {
                if (cancelled.get())
                {
                    return@withContext
                }

                try
                {
                    promise.result(task())
                }
                catch (exception: Exception)
                {
                    promise.error(exception)
                }
            }
        }

        promise.register { reason ->
            cancelled.set(true)
            job.cancel(CancellationException(reason))
        }
        return promise.future
    }

    fun <P, R : Any> delay(milliseconds: Long, parameter: P, task: (P) -> R): FutureResult<R>
    {
        val promise = Promise<R>()
        val cancelled = AtomicBoolean(false)

        val job = this.coroutineScope.launch {

            delay(max(1L, milliseconds))

            withContext(this.coroutineContext)
            {
                if (cancelled.get())
                {
                    return@withContext
                }

                try
                {
                    promise.result(task(parameter))
                }
                catch (exception: Exception)
                {
                    promise.error(exception)
                }
            }
        }

        promise.register { reason ->
            cancelled.set(true)
            job.cancel(CancellationException(reason))
        }
        return promise.future
    }
}
