package fr.jhelp.tasks.promise

import fr.jhelp.tasks.parallel

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
            parallel { cancelListener(this.cancelReason) }
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