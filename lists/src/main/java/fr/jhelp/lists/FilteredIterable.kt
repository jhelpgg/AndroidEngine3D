/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.lists

/**
 * Iterable that filter elements exposed to outside
 * @param iterable Iterable reference
 * @param filter Filter applied on element to know if they are visible or not
 */
class FilteredIterable<T : Any>(private val iterable: Iterable<T>,
                                private val filter: (T) -> Boolean) : Iterable<T>
{
    override fun iterator(): Iterator<T> =
        FilteredIterator(this.iterable.iterator(), this.filter)
}