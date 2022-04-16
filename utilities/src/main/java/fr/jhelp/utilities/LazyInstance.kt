/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.utilities

import java.util.concurrent.atomic.AtomicBoolean

/**
 * Lazy instance ale to do something if created an not create one if not exists
 */
class LazyInstance<T : Any>(private val creator: () -> T)
{
    private lateinit var instance: T
    private val created = AtomicBoolean(false)

    /**
     * Create the instance if necessary and call method with it
     */
    operator fun <R> invoke(task: T.() -> R): R
    {
        if (!this.created.getAndSet(true))
        {
            this.instance = this.creator()
        }

        return task(this.instance)
    }

    /**
     * Da action only if instance was created by [invoke].
     *
     * If [invoke] was never called before, action is not played
     */
    fun ifCreated(task: T.() -> Unit)
    {
        if (this.created.get())
        {
            task(this.instance)
        }
    }
}