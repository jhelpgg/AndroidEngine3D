/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.utilities.text

/**
 * Remove all white characters
 */
fun String.removeWhitCharacters(): String
{
    val array = this.toCharArray()
    val size = array.size
    val result = CharArray(size)
    var writeIndex = 0

    for (character in array)
    {
        if (character > ' ')
        {
            result[writeIndex] = character
            writeIndex++
        }
    }

    if (writeIndex == 0)
    {
        return ""
    }

    return String(result, 0, writeIndex)
}

/**
 * Compare ignore case, and if same, compare with take care case
 */
fun String.compareAlphabet(string: String): Int
{
    val comparision = this.compareTo(string, true)

    if (comparision != 0)
    {
        return comparision
    }

    return this.compareTo(string, false)
}

/**Empty interval*/
val EMPTY_CHARACTERS_INTERVAL = BasicCharactersInterval('B', 'A')

/**
 * Create a [BasicCharactersInterval] with given parameters
 */
fun interval(minimum: Char, maximum: Char = minimum) =
    when
    {
        minimum > maximum -> EMPTY_CHARACTERS_INTERVAL
        else              -> BasicCharactersInterval(minimum, maximum)
    }

/**
 * Create interval from the pair
 */
val Pair<Char, Char>.interval
    get() =
        interval(this.first, this.second)