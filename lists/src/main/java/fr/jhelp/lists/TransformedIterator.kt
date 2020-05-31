/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.lists

/**
 * Iterator that transform elements exposed to outside
 * @param iterator Iterable reference
 * @param action Action to apply for transform elements
 */
class TransformedIterator<P, R>(private val iterator: Iterator<P>, private val action: (P) -> R) :
    Iterator<R>
{
    override fun hasNext() = this.iterator.hasNext()

    override fun next() = this.action(this.iterator.next())
}