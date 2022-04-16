/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package android.os

import java.util.Objects

internal class HandlerMessage(val time: Long, val handler: Handler, val message: Message) :
    Comparable<HandlerMessage>
{
    override fun equals(other: Any?): Boolean
    {
        if (this === other)
        {
            return true
        }

        if (null == other || other !is HandlerMessage)
        {
            return false
        }

        return this.time == other.time && this.message == other.message
    }

    override fun hashCode(): Int = Objects.hash(this.time)

    override operator fun compareTo(other: HandlerMessage): Int
    {
        val comparison = this.time - other.time

        return when
        {
            comparison < 0L -> -1
            comparison > 0L -> 1
            else            -> 0
        }
    }
}