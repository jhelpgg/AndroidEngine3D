package fr.jhelp.utilities.regex

/**
 * Regex for choose between two regex
 * @param element1 First possibility
 * @param element2 Second possibility
 */
internal class RegexOr(element1: RegexElement, element2: RegexElement) :
    RegexElement("(?:(?:%s)|(?:%s))", RegexType.OR,
                 element1 = element1, element2 = element2)
{
    /**
     * Regex string representation
     */
    override fun toRegex() =
        String.format(this.pattern, this.element1!!.toRegex(), this.element2!!.toRegex())
}
