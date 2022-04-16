/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.utilities.extensions

import kotlin.math.max

/**
 * String representation of an array
 *
 * @param header The header : Text used before start show elements. Default value is `[`
 * @param separator The separator : Text used between elements. Default value is `, ` (Their a space after the comma)
 * @param footer The footer : Text used after all elements. Default value is `]`
 * @param limitSize Number of maximum elements shows. If array have more elements, a mark of more is used. By default we show the entire array
 * @param moreElements Text to show their more elements. This text is shows if the number of array element is bigger than the number of maximum to show. Default value is `...`
 * @param moreAtEnd Choose if the mark of more elements is shows at end or in the middle. In middle, it will print first and last elements. By default the mak of more elements is shows at end
 * @return The computed String representation
 */
fun FloatArray.string(header: String = "[", separator: String = ", ", footer: String = "]",
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