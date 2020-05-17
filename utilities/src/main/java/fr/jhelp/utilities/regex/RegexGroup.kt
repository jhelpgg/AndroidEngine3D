package fr.jhelp.utilities.regex

/**
 * Regex capture group
 * @param element Regex to capture
 */
internal class RegexGroup(element: RegexElement) : RegexElement("(%s)", RegexType.GROUP,
                                                                element1 = element)
{
    /**
     * Regex string representation
     */
    override fun toRegex() = String.format(this.pattern, this.element1!!.toRegex())
}