/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.utilities

/**
 * Check if argument(s) satisfy a condition
 * @param condition Condition to satisfy
 * @param messageIfFail Message to add in exception to give more information why argument(s) are wrong
 * @throws IllegalArgumentException If condition not meet
 */
@Throws(IllegalArgumentException::class)
fun checkArgument(condition: Boolean, messageIfFail: String)
{
    if (!condition)
    {
        throw IllegalArgumentException(messageIfFail)
    }
}

/**
 * Check if an index inside given bounds
 * @throws IndexOutOfBoundsException If index not inside given bounds
 */
@Throws(IndexOutOfBoundsException::class)
fun checkIndexBound(index: Int, minimum: Int, maximum: Int, messageIfOutOfBounds: String)
{
    if (index < minimum || index > maximum)
    {
        throw IndexOutOfBoundsException(
            "Index $index not inside [$minimum, $maximum] : $messageIfOutOfBounds")
    }
}