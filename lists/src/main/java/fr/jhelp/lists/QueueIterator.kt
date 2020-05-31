/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.lists

internal class QueueIterator<T>(private var current: QueueElement<T>?) : Iterator<T>
{
    override fun hasNext() = this.current != null

    @Throws(NoSuchElementException::class)
    override fun next(): T
    {
        val current = this.current
                      ?: throw NoSuchElementException("No more element to iterate")
        this.current = current.next
        return current.element
    }
}