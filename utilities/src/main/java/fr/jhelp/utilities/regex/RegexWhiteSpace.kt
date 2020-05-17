package fr.jhelp.utilities.regex

/**
 * Regex for white space characters
 */
internal object RegexWhiteSpace : RegexElement("\\s", RegexType.WHITE_SPACE)
{
    /**
     * Regex string representation
     */
    override fun toRegex() = this.pattern
}
