package fr.jhelp.utilities.regex

import java.util.regex.Pattern

/**
 * Regex that match a specific text
 * @param text Text to match
 */
internal class RegexText(text: String) : RegexElement(Pattern.quote(text), RegexType.TEXT)
{
    /**
     * Regex string representation
     */
    override fun toRegex() = this.pattern
}