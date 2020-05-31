/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.lists

/**
 * Iterable that transform elements exposed to outside
 * @param iterable Iterable reference
 * @param action Action to apply for transform elements
 */
class TransformedIterable<P, R>(private val iterable: Iterable<P>, private val action: (P) -> R) :
    Iterable<R>
{
    override fun iterator() =
        TransformedIterator(this.iterable.iterator(), this.action)
}