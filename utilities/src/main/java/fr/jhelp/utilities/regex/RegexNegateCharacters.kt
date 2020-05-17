package fr.jhelp.utilities.regex

import fr.jhelp.utilities.text.CharactersInterval

/**
 * Regex the refuse an interval of characters
 * @param charactersInterval Interval to refuse
 */
internal class RegexNegateCharacters(val charactersInterval: CharactersInterval) :
    RegexElement("[^%s]",
                 RegexType.CHARACTERS)
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
                                                                           ""))
        }
}