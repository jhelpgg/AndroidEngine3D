/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.tasks.chain

import java.util.Enumeration

/**
 * [Emitter] based on enumeration.
 *
 * It will emit all enumeration elements once
 */
class EmitterEnumeration<T : Any>(private val enumeration: Enumeration<T>) : Emitter<T>()
{
    override fun next(): T?
    {
        if (this.enumeration.hasMoreElements())
        {
            return this.enumeration.nextElement()
        }

        return null
    }
}