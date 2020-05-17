package fr.jhelp.utilities.regex

/**
 * Regex for any characters
 */
internal object RegexAny : RegexElement(".", RegexType.ANY)
{
    /**
     * Regex string representation
     */
    override fun toRegex() = this.pattern
}