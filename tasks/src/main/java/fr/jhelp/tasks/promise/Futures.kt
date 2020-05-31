/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.tasks.promise

import fr.jhelp.tasks.IndependentThread
import java.util.concurrent.atomic.AtomicInteger

/**
 * Simplify a FutureResult<FutureResult&lt;R&gt;> to a FutureResult&lt;R&gt;
 */
fun <R : Any> FutureResult<FutureResult<R>>.unwrap(): FutureResult<R>
{
    val promise = Promise<R>()
    promise.register { reason -> this.cancel(reason) }

    this.then { future ->
        try
        {
            promise.result(future()())
        }
        catch (exception: Exception)
        {
            promise.error(exception)
        }
    }

    return promise.future
}

/**
 * Create a future that wait all futures are finished
 */
fun List<FutureResult<*>>.join(): FutureResult<List<FutureResult<*>>>
{
    val promise = Promise<List<FutureResult<*>>>()
    val counter = AtomicInteger(this.size)
    val continuation =
        { _: FutureResult<*> ->
            if (counter.decrementAndGet() == 0)
            {
                promise.result(this)
            }
        }

    promise.register { reason ->
        for (future in this)
        {
            future.cancel(reason)
        }
    }

    for (future in this)
    {
        future.then(IndependentThread, continuation)
    }

    return promise.future
}