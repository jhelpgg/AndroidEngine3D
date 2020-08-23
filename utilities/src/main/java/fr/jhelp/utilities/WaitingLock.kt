/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.utilities

import java.util.concurrent.atomic.AtomicBoolean

class WaitingLock
{
    private val lock = Object()
    private val waiting = AtomicBoolean(false)
    private val freed = AtomicBoolean(false)

    fun lock()
    {
        synchronized(this.lock)
        {
            if (!this.freed.getAndSet(false))
            {
                this.waiting.set(true)
                this.lock.wait()
                this.waiting.set(false)
            }
        }
    }

    fun unlock()
    {
        synchronized(this.lock)
        {
            if (this.waiting.get())
            {
                this.lock.notifyAll()
            }
            else
            {
                this.freed.set(true)
            }
        }
    }
}