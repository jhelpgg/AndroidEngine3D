/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.lists

/**
 * Transform each element to corresponding one throw the given action.
 *
 * It is faster and take less memory than [Iterable.map]
 *
 * @param action Action that transform elements
 * @return Iterator with transformed elements
 */
fun <P, R> Iterator<P>.transform(action: (P) -> R): Iterator<R> =
    TransformedIterator(this, action)

/**
 * Transform each element to corresponding one throw the given action.
 *
 * It is faster and take less memory than [Iterable.map]
 *
 * @param action Action that transform elements
 * @return Iterable with transformed elements
 */
fun <P, R> Iterable<P>.transform(action: (P) -> R): Iterable<R> =
    TransformedIterable(this, action)

/**
 * Show only elements that match given filter.
 *
 * It is faster and take less memory than [Iterable.filter]
 *
 * @param filter Filter to apply on element
 * @return Iterator with filtered elements
 */
fun <T : Any> Iterator<T>.smartFilter(filter: (T) -> Boolean): Iterator<T> =
    FilteredIterator(this, filter)

/**
 * Show only elements that match given filter.
 *
 * It is faster and take less memory than [Iterable.filter]
 *
 * @param filter Filter to apply on element
 * @return Iterable with filtered elements
 */
fun <T : Any> Iterable<T>.smartFilter(filter: (T) -> Boolean): Iterable<T> =
    FilteredIterable(this, filter)
