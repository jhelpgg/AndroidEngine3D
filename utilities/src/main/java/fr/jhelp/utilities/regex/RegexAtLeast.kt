package fr.jhelp.utilities.regex

/**
 * Repeat a regex at least a number of time
 * @param element Regex to repeat
 * @param number Number of repetition minimum
 */
internal class RegexAtLeast(element: RegexElement, number: Int) : RegexElement("(?:%s){%d,}",
                                                                               RegexType.AT_LEAST,
                                                                               element1 = element,
                                                                               number1 = number)
{
    /**
     * Regex string representation
     */
    override fun toRegex() = String.format(this.pattern, this.element1!!.toRegex(), this.number1)
}
