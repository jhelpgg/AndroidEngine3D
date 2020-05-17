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