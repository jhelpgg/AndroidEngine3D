/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.lists

class NaturalComparator<C : Comparable<C>> : Comparator<C>
{
    override fun compare(comparable1: C, comparable2: C): Int =
        comparable1.compareTo(comparable2)
}