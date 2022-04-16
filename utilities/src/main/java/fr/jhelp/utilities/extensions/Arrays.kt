/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.utilities.extensions

import kotlin.math.max

fun <T> Array<T>.string(header: String = "[", separator: String = ", ", footer: String = "]",
                        limitSize: Int = this.size,
                        moreElements: String = "...", moreAtEnd: Boolean = true): String
{
    val (firstPart, secondPart) =
        when
        {
            this.isEmpty()         -> Pair(0, 0)
            limitSize >= this.size -> Pair(this.size, 0)
            moreAtEnd              -> Pair(max(1, limitSize), 0)
            this.size == 1         -> Pair(1, 0)
            else                   ->
            {
                val limit = max(2, limitSize)
                val first = (limit + 1) / 2
                Pair(first, limit - first)
            }
        }

    val stringBuilder = StringBuilder()
    stringBuilder.append(header)

    if (firstPart > 0)
    {
        stringBuilder.append(this[0])

        for (index in 1 until firstPart)
        {
            stringBuilder.append(separator)
            stringBuilder.append(this[index])
        }
    }

    if (secondPart > 0 || firstPart < this.size)
    {
        stringBuilder.append(separator)
        stringBuilder.append(moreElements)

        for (index in this.size - secondPart until this.size)
        {
            stringBuilder.append(separator)
            stringBuilder.append(this[index])
        }
    }

    stringBuilder.append(footer)
    return stringBuilder.toString()
}