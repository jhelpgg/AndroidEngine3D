/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.utilities.regex

/**
 * Regex union of regex(s)
 */
internal class RegexUnion() : RegexElement("", RegexType.UNION)
{
    /**Regex union*/
    private val elements = ArrayList<RegexElement>()

    /**
     * Regex string representation
     */
    override fun toRegex(): String
    {
        val stringBuilder = StringBuilder()
        this.elements.forEach { stringBuilder.append(it.toRegex()) }
        return stringBuilder.toString()
    }

    /**
     * Add regex to the union
     */
    operator fun plusAssign(element: RegexElement)
    {
        if (element is RegexUnion)
        {
            this.elements.addAll(element.elements)
            return
        }

        this.elements.add(element)
    }

    /**
     * Indicates if a regex element contains by this regex element. (Itself count)
     * @param id regex element ID searched
     */
    internal override operator fun contains(id: Int) = this.elements.any { id in it }

    /**
     * Do action on each regex of the union
     */
    fun forEach(action: (RegexElement) -> Unit) = this.elements.forEach(action)
}