package fr.jhelp.utilities.regex

/**
 * Repeat a regex zero or more time
 * @param element Regex to repeat
 */
internal class RegexZeroOrMore(element: RegexElement) : RegexElement("(?:%s)*",
                                                                     RegexType.ZERO_OR_MORE,
                                                                     element1 = element)
{
    /**
     * Regex string representation
     */
    override fun toRegex() = String.format(this.pattern, this.element1!!.toRegex())
}