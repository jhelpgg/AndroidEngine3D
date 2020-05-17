package fr.jhelp.utilities.regex

/**
 * Regex that match exactly what it was capture by a capture group
 * @param regexGroup Group to duplicate
 */
internal class RegexSameAs(val regexGroup: RegexGroup) : RegexElement("\\%d", RegexType.SAME_AS)
{
    /**
     * Regex string representation
     */
    override fun toRegex() = String.format(this.pattern, this.regexGroup.count)
}