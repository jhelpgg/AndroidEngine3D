/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.tasks.promise

import fr.jhelp.tasks.IndependentThread
import fr.jhelp.tasks.ThreadType
import fr.jhelp.tasks.parallel
import fr.jhelp.utilities.ALWAYS_TRUE
import java.util.concurrent.CancellationException
import java.util.concurrent.atomic.AtomicReference

/**
 * Future result of something
 *
 * The result is said complete when computing finished.
 * That is to say when task succeed to compute OR an error happen while computing OR task canceled
 *
 * Its possible to chain an other task to do when task complete, by example say :
 * * When result succeed , use the result to do this other task
 * * When result complete, do something
 * * If result fail, then do this
 * * ...
 */
class FutureResult<R : Any> internal constructor(private val promise: Promise<R>)
{
    private val status = AtomicReference<FutureResultStatus>(FutureResultStatus.COMPUTING)
    private val lock = Object()
    private lateinit var result: R
    private lateinit var cancelReason: String
    private lateinit var error: Exception
    private val listeners = ArrayList<Pair<(FutureResult<R>) -> Unit, ThreadType>>()

    internal fun result(result: R)
    {
        synchronized(this.lock)
        {
            if (this.status.compareAndSet(FutureResultStatus.COMPUTING, FutureResultStatus.SUCCEED))
            {
                this.result = result
                this.lock.notifyAll()
            }
        }

        this.fireListeners()
    }

    internal fun error(error: Exception)
    {
        synchronized(this.lock)
        {
            if (this.status.compareAndSet(FutureResultStatus.COMPUTING, FutureResultStatus.FAILED))
            {
                this.error = error
                this.lock.notifyAll()
            }
        }

        this.fireListeners()
    }

    private fun fireListeners()
    {
        synchronized(this.listeners) {
            for (pair in this.listeners)
            {
                pair.second(this, pair.first)
            }

            this.listeners.clear()
            this.listeners.trimToSize()
        }
    }

    private fun <R1 : Any> andListener(continuation: (R) -> R1, promise: Promise<R1>,
                                       condition: (R) -> Boolean = ALWAYS_TRUE) =
        { future: FutureResult<R> ->
            when (future.status())
            {
                FutureResultStatus.SUCCEED  ->
                    try
                    {
                        val result = future()

                        if (condition(result))
                        {
                            promise.result(continuation(result))
                        }
                        else
                        {
                            promise.error(Exception("Not matching condition"))
                        }
                    }
                    catch (exception: Exception)
                    {
                        promise.error(exception)
                    }
                FutureResultStatus.FAILED   ->
                    promise.error(future.error)
                FutureResultStatus.CANCELED ->
                    promise.error(CancellationException(this.cancelReason))
                else                        -> Unit
            }
        }

    private fun <R1 : Any> thenListener(continuation: (FutureResult<R>) -> R1,
                                        promise: Promise<R1>) =
        { future: FutureResult<R> ->
            try
            {
                promise.result(continuation(future))
            }
            catch (exception: Exception)
            {
                promise.error(exception)
            }
        }

    private fun errorListener(errorListener: (Exception) -> Unit, threadType: ThreadType) =
        { future: FutureResult<R> ->
            when (future.status())
            {
                FutureResultStatus.FAILED   ->
                    threadType(future.error, errorListener)
                FutureResultStatus.CANCELED ->
                    threadType(CancellationException(this.cancelReason), errorListener)
                else                        -> Unit
            }
        }

    private fun cancelListener(cancelListener: (String) -> Unit, threadType: ThreadType) =
        { future: FutureResult<R> ->
            if (future.status() == FutureResultStatus.CANCELED)
            {
                threadType(this.cancelReason, cancelListener)
            }
        }

    /**
     * Current future status.
     */
    fun status() = this.status.get()

    /**
     * Register listener called when result is known. If already know, listener is callback immediately
     *
     * @param listener Listener to register
     * @param threadType : Thread type where execute the listener
     */
    fun register(threadType: ThreadType = IndependentThread, listener: (FutureResult<R>) -> Unit)
    {
        synchronized(this.lock)
        {
            if (this.status.get() != FutureResultStatus.COMPUTING)
            {
                threadType(this, listener)
                return@register
            }

            synchronized(this.listeners)
            {
                this.listeners.add(Pair(listener, threadType))
            }
        }
    }

    /**
     * Block caller thread until result iis known, an error happen or cancel is triggered
     * If one of those events already happen, thread is not blocked.
     *
     * @return The result
     * @throws Exception if computation failed or cancel happen
     */
    operator fun invoke(): R
    {
        synchronized(this.lock)
        {
            if (this.status.get() == FutureResultStatus.COMPUTING)
            {
                this.lock.wait()
            }
        }

        when (this.status.get())
        {
            FutureResultStatus.SUCCEED  -> return this.result
            FutureResultStatus.FAILED   -> throw this.error
            FutureResultStatus.CANCELED -> throw CancellationException(this.cancelReason)
            else                        ->
                throw RuntimeException("Should no goes here : ${this.status.get()}")
        }
    }

    /**
     *  Block caller thread until result iis known, an error happen or cancel is triggered
     * If one of those events already happen, thread is not blocked.
     *
     * @return Future complete status
     */
    fun waitComplete(): FutureResultStatus
    {
        synchronized(this.lock)
        {
            if (this.status.get() == FutureResultStatus.COMPUTING)
            {
                this.lock.wait()
            }
        }

        return this.status.get()
    }

    /**
     * Current error.
     *
     * If future still computing, there no actually an error, but may will have on the end
     */
    fun error() =
        synchronized(this.lock)
        {
            if (this.status.get() == FutureResultStatus.FAILED) this.error else null
        }

    /**
     * Cancellation reason.
     *
     * Have meaning only if future is canceled
     *
     * If future still computing, there no actually a cancel reason, but may will have on the end
     */
    fun cancelReason() =
        synchronized(this.lock)
        {
            if (this.status.get() == FutureResultStatus.CANCELED) this.cancelReason else null
        }

    /**
     * Try to cancel task associated to the thread
     *
     * @return `true` if cancel request is propagate to current task.
     * Else it means the future is already complete, so too late for cancel
     */
    fun cancel(reason: String): Boolean
    {
        val canceled =
            synchronized(this.lock)
            {
                if (this.status.compareAndSet(FutureResultStatus.COMPUTING,
                                              FutureResultStatus.CANCELED))
                {
                    this.cancelReason = reason
                    parallel(reason, this.promise::cancel)
                    true
                }
                else
                {
                    false
                }
            }

        if (canceled)
        {
            this.fireListeners()
        }

        return canceled
    }

    /**
     * Do task, in specified thread type, when result complete and if succeed to compute
     *
     * @return Future result represents the combination of the result follow by the task
     */
    fun <R1 : Any> and(threadType: ThreadType = IndependentThread,
                       continuation: (R) -> R1): FutureResult<R1>
    {
        val promise = Promise<R1>()
        promise.register { reason -> this.cancel(reason) }
        this.register(threadType, this.andListener(continuation, promise))
        return promise.future
    }

    /**
     * Do task, in [IndependentThread], when result complete and if succeed to compute and result match given condition
     */
    fun <R1 : Any> andIf(condition: (R) -> Boolean, continuation: (R) -> R1) =
        this.andIf(IndependentThread, condition, continuation)

    /**
     * Do task, in specified thread type, when result complete and if succeed to compute and result match given condition
     *
     * @return Future result represents the combination of the result follow by the task
     */
    fun <R1 : Any> andIf(threadType: ThreadType,
                         condition: (R) -> Boolean,
                         continuation: (R) -> R1): FutureResult<R1>
    {
        val promise = Promise<R1>()
        promise.register { reason -> this.cancel(reason) }
        this.register(threadType, this.andListener(continuation, promise, condition))
        return promise.future
    }

    /**
     * Do task, in specified thread type, when result complete
     *
     * @return Future result represents the combination of the result follow by the task
     */
    fun <R1 : Any> then(threadType: ThreadType = IndependentThread,
                        continuation: (FutureResult<R>) -> R1): FutureResult<R1>
    {
        val promise = Promise<R1>()
        promise.register { reason -> this.cancel(reason) }
        this.register(threadType, this.thenListener(continuation, promise))
        return promise.future
    }

    /**
     * Do task, in specified thread type, when result complete and if succeed to compute
     *
     * @return Future result represents the combination of the result follow by the task
     */
    fun <R1 : Any> andUnwrap(threadType: ThreadType = IndependentThread,
                             continuation: (R) -> FutureResult<R1>): FutureResult<R1> =
        this.and(threadType, continuation).unwrap()

    /**
     * Do task, in [IndependentThread], when result complete and if succeed to compute and result match given condition
     *
     * @return Future result represents the combination of the result follow by the task
     */
    fun <R1 : Any> andIfUnwrap(condition: (R) -> Boolean, continuation: (R) -> FutureResult<R1>) =
        this.andIfUnwrap(IndependentThread, condition, continuation)

    /**
     * Do task, in specified thread type, when result complete and if succeed to compute and result match given condition
     *
     * @return Future result represents the combination of the result follow by the task
     */
    fun <R1 : Any> andIfUnwrap(threadType: ThreadType,
                               condition: (R) -> Boolean,
                               continuation: (R) -> FutureResult<R1>): FutureResult<R1> =
        this.andIf(threadType, condition, continuation).unwrap()

    /**
     * Do task, in specified thread type, when result complete
     *
     * @return Future result represents the combination of the result follow by the task
     */
    fun <R1 : Any> thenUnwrap(threadType: ThreadType = IndependentThread,
                              continuation: (FutureResult<R>) -> FutureResult<R1>): FutureResult<R1> =
        this.then(threadType, continuation).unwrap()

    /**
     * Do task, in specified thread type, when result failed
     *
     * @return This future result. Convenient for chaining
     */
    fun onError(threadType: ThreadType = IndependentThread,
                errorListener: (Exception) -> Unit): FutureResult<R>
    {
        this.register(IndependentThread, this.errorListener(errorListener, threadType))
        return this
    }

    /**
     * Do task, in specified thread type, when result canceled
     *
     * @return This future result. Convenient for chaining
     */
    fun onCancel(threadType: ThreadType = IndependentThread,
                 cancelListener: (String) -> Unit): FutureResult<R>
    {
        this.register(IndependentThread, this.cancelListener(cancelListener, threadType))
        return this
    }

    /**
     * String representation
     */
    override fun toString() =
        synchronized(this.lock)
        {
            when (this.status.get())
            {
                FutureResultStatus.SUCCEED   -> "Succeed : ${this.result}"
                FutureResultStatus.FAILED    -> "Error : ${this.error}"
                FutureResultStatus.CANCELED  -> "Canceled because : ${this.cancelReason}"
                FutureResultStatus.COMPUTING -> "Computing ..."
                else                         -> "Why am I there ? : ${this.status.get()}"
            }
        }
}