/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.tasks.delay

import fr.jhelp.tasks.ThreadType

internal class DelayElement(val action: () -> Unit, val time: Long, val threadType: ThreadType) :
    Comparable<DelayElement>
{
    override fun compareTo(other: DelayElement): Int
    {
        val difference = this.time - other.time

        return when
        {
            difference > 0 -> 1
            difference < 0 -> -1
            else           -> 0
        }
    }
}