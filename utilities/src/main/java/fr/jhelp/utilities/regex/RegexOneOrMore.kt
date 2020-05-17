package fr.jhelp.utilities.regex

/**
 * Repeat a regex one or more time
 * @param element Regex to repeat
 */
internal class RegexOneOrMore(element: RegexElement) : RegexElement("(?:%s)+",
                                                                    RegexType.ONE_OR_MORE,
                                                                    element1 = element)
{
    /**
     * Regex string representation
     */
    override fun toRegex() = String.format(this.pattern, this.element1!!.toRegex())
}