package fr.jhelp.utilities.regex

import fr.jhelp.utilities.text.CharactersInterval

/**
 * Regex that accept an interval of characters
 * @param charactersInterval Interval to accept
 */
internal class RegexCharacters(val charactersInterval: CharactersInterval) : RegexElement("[%s]", RegexType.CHARACTERS)
{
    /**
     * Regex string representation
     */
    override fun toRegex() =
        when
        {
            this.charactersInterval.empty -> ""
            else                          ->
                String.format(this.pattern, this.charactersInterval.format("", "-", "",
                                                                           "", "",
                                                                           "", true))
        }
}
