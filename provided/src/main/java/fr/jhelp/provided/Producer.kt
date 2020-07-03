/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.provided

import java.util.concurrent.atomic.AtomicBoolean

internal class Producer<T : Any>(private val single: Boolean, private val producer: () -> T)
{
    private val created = AtomicBoolean(false)
    private lateinit var value: T

    fun value() =
        if (this.single)
        {
            if (!this.created.getAndSet(true))
            {
                this.value = this.producer()
                this.value
            }
            else
            {
                this.value
            }
        }
        else
        {
            this.producer()
        }
}