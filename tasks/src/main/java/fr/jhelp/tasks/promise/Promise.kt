/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.tasks.promise

import fr.jhelp.tasks.parallel

/**
 * When a task compute something in background,, it makes a promise do have a result in future.
 *
 * This object represents the prmise. Generally, the promise is created privately by the task
 * who do the computing and share the associated [FutureResult] to every process interest by the result
 */
class Promise<R : Any>
{
    /**Future associated to the promise. Give it to any one want follow the process result*/
    val future = FutureResult<R>(this)

    /**Current cancel status*/
    var canceled = false
        private set
    private lateinit var cancelReason: String
    private val listeners = ArrayList<(String) -> Unit>()
    private var resolvedd = false

    /**
     * For internal use : signal that cancel happen
     */
    internal fun cancel(reason: String)
    {
        this.resolvedd = true
        this.canceled = true
        this.cancelReason = reason

        synchronized(this.listeners)
        {
            this.listeners.forEach { listener -> listener(reason) }
        }
    }

    /**
     * Publish the result
     */
    fun result(result: R)
    {
        this.resolvedd = true
        this.future.result(result)
    }

    /**
     * Publish a task error, result will never arrive
     */
    fun error(error: Exception)
    {
        this.resolvedd = true
        this.future.error(error)
    }

    /**
     * register to cancel event
     */
    fun register(cancelListener: (String) -> Unit)
    {
        if (this.canceled)
        {
             { cancelListener(this.cancelReason) }.parallel()
            return
        }

        if (!this.resolvedd)
        {
            synchronized(this.listeners)
            {
                this.listeners.add(cancelListener)
            }
        }
    }
}