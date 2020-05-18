/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

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