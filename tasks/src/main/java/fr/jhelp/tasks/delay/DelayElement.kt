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