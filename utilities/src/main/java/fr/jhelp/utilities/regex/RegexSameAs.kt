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