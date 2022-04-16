/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.tasks.chain

import fr.jhelp.lists.Queue
import java.util.concurrent.atomic.AtomicBoolean

class EmitterSource<T : Any> : Emitter<T>()
{
    private val atLeastOneThreadWait = AtomicBoolean(false)
    private val finished = AtomicBoolean(false)
    private val waitingQueue = Queue<T>()
    private val lock = Object()

    override fun next(): T?
    {
        synchronized(this.lock)
        {
            if (this.waitingQueue.empty && !this.finished.get())
            {
                this.atLeastOneThreadWait.set(true)
                this.lock.wait()
            }
        }

        if (this.waitingQueue.empty)
        {
            return null
        }

        return this.waitingQueue.dequeue()
    }

    fun enqueue(value: T)
    {
        if (this.finished.get())
        {
            return
        }

        synchronized(this.lock)
        {
            this.waitingQueue.enqueue(value)

            if (this.atLeastOneThreadWait.compareAndSet(true, false))
            {
                this.lock.notifyAll()
            }
        }
    }

    fun finish()
    {
        this.finished.set(true)

        synchronized(this.lock)
        {
            if (this.atLeastOneThreadWait.compareAndSet(true, false))
            {
                this.lock.notifyAll()
            }
        }
    }
}