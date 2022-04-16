/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.utilities.regex

import java.util.concurrent.atomic.AtomicInteger

/**Next regex element ID*/
internal val NEXT_ID = AtomicInteger(0)

/**
 * Element of regex
 * @param pattern Pattern used for create regex string
 * @param regexType Regex type
 * @param element1 Optional regex first parameter
 * @param element2 Optional regex second parameter
 * @param number1 Optional first number parameter
 * @param number2 Optional second number parameter
 */
internal abstract class RegexElement(val pattern: String, val regexType: RegexType,
                                     val element1: RegexElement? = null, val element2: RegexElement? = null,
                                     val number1: Int = -1, val number2: Int = -1)
{
    /**Element ID*/
    val id = NEXT_ID.getAndIncrement()
    /**Counter used to count groups*/
    internal var count = -1

    /**
     * Regex string representation
     */
    abstract fun toRegex(): String

    /**
     * Indicates if a regex element contains by this regex element. (Itself count)
     * @param id regex element ID searched
     */
    internal open operator fun contains(id: Int): Boolean
    {
        if (id == this.id)
        {
            return true
        }

        if (this.element1 != null && (id in this.element1))
        {
            return true
        }

        if (this.element2 != null && (id in this.element2))
        {
            return true
        }

        return false
    }

    /**
     * Indicates if other object equals to this regex element
     */
    override fun equals(other: Any?): Boolean
    {
        if (this === other)
        {
            return true
        }

        if (null == other || other !is RegexElement)
        {
            return false
        }

        return this.toRegex() == other.toRegex()
    }

    /**
     * Hash code
     */
    override fun hashCode(): Int
    {
        return this.toRegex().hashCode()
    }
}