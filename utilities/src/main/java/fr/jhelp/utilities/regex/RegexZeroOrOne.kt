package fr.jhelp.utilities.regex

/**
 * Repeat a regex zero or one time
 * @param element Regex to repeat
 */
internal class RegexZeroOrOne(element: RegexElement) : RegexElement("(?:%s)?",
                                                                    RegexType.ZERO_OR_ONE,
                                                                    element1 = element)
{
    /**
     * Regex string representation
     */
    override fun toRegex() = String.format(this.pattern, this.element1!!.toRegex())
}