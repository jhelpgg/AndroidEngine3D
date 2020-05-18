/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.utilities.regex

import java.util.regex.Pattern

/**
 * Represents a regex
 * @param regexElement Regex base
 */
open class RegexPart internal constructor(internal val regexElement: RegexElement)
{
    companion object
    {
        /**
         * Compute groups ID of given regex
         */
        internal fun computeGroupsID(regexPart: RegexPart)
        {
            if (regexPart.groupIdComputed)
            {
                return
            }

            RegexPart.computeGroupsID(regexPart.regexElement, 0)
            regexPart.groupIdComputed = true
        }

        /**
         * Compute groups ID of given regex part
         * @param regexElement Regex part to explore
         * @param currentID Last group ID
         * @return Last given group ID
         */
        private fun computeGroupsID(regexElement: RegexElement, currentID: Int): Int
        {
            var id = currentID

            if (regexElement.regexType == RegexType.GROUP)
            {
                id++
                regexElement.count = id
            }

            if (regexElement.element1 != null)
            {
                id = computeGroupsID(regexElement.element1, id)
            }

            if (regexElement.element2 != null)
            {
                id = computeGroupsID(regexElement.element2, id)
            }

            if (regexElement is RegexUnion)
            {
                regexElement.forEach { id = computeGroupsID(it, id) }
            }

            return id
        }

        /**
         * Compile pattern for given regex
         */
        internal fun compile(regexPart: RegexPart): Pattern
        {
            RegexPart.computeGroupsID(regexPart)
            return Pattern.compile(regexPart.regexElement.toRegex())
        }
    }

    /**Indicates if groups ID are computed*/
    internal var groupIdComputed = false

    /**Regex Pattern*/
    val pattern: Pattern by lazy { RegexPart.compile(this) }

    /**
     * Compute matcher for given text
     */
    fun matcher(text: String) = this.pattern.matcher(text)

    /**
     * Indicates if given text match exactly to the regex
     */
    fun matches(text: String) = this.matcher(text).matches()

    /**
     * Indicates if a regex inside this regex
     */
    operator fun contains(regexPart: RegexPart) = regexPart.regexElement.id in this.regexElement

    /**
     * Compute the given group number.
     *
     * Usually used by [java.util.regex.Matcher.start], [java.util.regex.Matcher.end] and [java.util.regex.Matcher.group]
     * @throws IllegalArgumentException If group not inside the regex
     */
    @Throws(IllegalArgumentException::class)
    fun groupNumber(group: Group): Int
    {
        if (group !in this)
        {
            throw IllegalArgumentException("The group: ${group}\nis not inside this regex: ${this}")
        }

        RegexPart.computeGroupsID(this)
        return group.regexElement.count
    }

    /**
     * Compute the given group name.
     *
     * Usually used on replacement in [replaceAll], [java.util.regex.Matcher.appendReplacement],
     * [java.util.regex.Matcher.replaceAll], [java.util.regex.Matcher.replaceFirst]
     * @throws IllegalArgumentException If group not inside the regex
     */
    @Throws(IllegalArgumentException::class)
    fun groupName(group: Group) = "$${this.groupNumber(group)}"

    /**
     * String representation
     */
    override fun toString() = this.regexElement.toRegex()

    /**
     * Indicates if this regex equals to given object
     */
    override fun equals(other: Any?): Boolean
    {
        if (this === other)
        {
            return true
        }

        if (null == other || other !is RegexPart)
        {
            return false
        }

        return this.regexElement == other.regexElement
    }

    /**
     * Hash code
     */
    override fun hashCode() = this.regexElement.hashCode()

    /**
     * Replace all matching element in given tex by the replacement.
     *
     * For replace what is captured by a group, think use [groupName]
     *
     * Example:
     *
     * ````Kotlin
     * val group = ')'.regexOut().zeroOrMore().group()
     * val regex = '('.regex() + group + ')'.regex()
     * val groupName = regex.groupName(group)
     * val replaced = regex.replaceAll(text, "{$groupName}")
     * ````
     */
    fun replaceAll(text: String, replacement: String): String
    {
        val matcher = this.matcher(text)
        val stringBuffer = StringBuffer()

        while (matcher.find())
        {
            matcher.appendReplacement(stringBuffer, replacement)
        }

        matcher.appendTail(stringBuffer)
        return stringBuffer.toString()
    }

    /**
     * Create a replacer for replace all matching element in given text by a replacement.
     *
     * Example:
     *
     * ````Kotlin
     *  val group = ')'.regexOut().zeroOrMore().group()
     *  val regex = '('.regex() + group + ')'.regex()
     *  val replacer = regex.replacer() + "{" + group + "}"
     *  val result = replacer.replaceAll(text)
     * ````
     *In the given example, if `text` is **`"hello (85, call) something, (bob)"`** then `result` will be :
     * **`"hello {85, call} something, {bob}"`**
     */
    fun replacer() =
        Replacer(this)
}
