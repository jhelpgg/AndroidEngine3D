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
 * Repeat a regex at inside an interval number of time
 * @param element Regex to repeat
 * @param minimum Number of repetition minimum
 * @param maximum Number of repetition maximum
 */
internal class RegexBetween(element: RegexElement, minimum: Int, maximum: Int) :
    RegexElement("(?:%s){%d,%d}",
                 RegexType.BETWEEN,
                 element1 = element,
                 number1 = minimum,
                 number2 = maximum)
{
    /**
     * Regex string representation
     */
    override fun toRegex() =
        String.format(this.pattern, this.element1!!.toRegex(), this.number1, this.number2)
}