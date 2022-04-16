/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.utilities.extensions

import fr.jhelp.utilities.regex.Group
import fr.jhelp.utilities.regex.between
import fr.jhelp.utilities.regex.oneOrMore
import fr.jhelp.utilities.regex.plus
import fr.jhelp.utilities.regex.regex
import fr.jhelp.utilities.regex.regexIn
import fr.jhelp.utilities.regex.zeroOrMore
import fr.jhelp.utilities.regex.zeroOrOne
import fr.jhelp.utilities.text.interval
import java.util.regex.Matcher

/**Regex match any digit in `0`to `9` one time*/
val DIGIT_REGEX = interval('0', '9').regexIn()

/**Regex match any upper case and lower case letter in `a`to `z` and `A` to `Z` one time*/
val LETTER_REGEX = (interval('a', 'z') + interval('A', 'Z')).regexIn()

/**digit, letter or underscore*/
val DIGIT_LETTER_UNDERSCORE_REGEX =
    (interval('a', 'z') + interval('A', 'Z') + interval('0', '9') + '_').regexIn()

/**Match to an integer*/
val INTEGER_REGEX = charArrayOf('+', '-').regex().zeroOrOne() + DIGIT_REGEX.oneOrMore()

/**Match to a real*/
val REAL_REGEX = INTEGER_REGEX + ('.'.regex() + DIGIT_REGEX.oneOrMore()).zeroOrOne()

/**Variable name regular expression*/
val VARIABLE_NAME_REGEX = LETTER_REGEX + DIGIT_LETTER_UNDERSCORE_REGEX.zeroOrMore()

/**
 * Regex for validate email address, inspired from [android.util.Patterns.EMAIL_ADDRESS]
 */
val EMAIL_REGEX =
    (interval('a', 'z') + interval('A', 'Z') + interval('0', '9') + '+'+ '.'+ '_'+ '%'+ '-').regexIn().between(1, 256) +
    '@'.regex() +
    (interval('a', 'z') + interval('A', 'Z')+ interval('0', '9')).regexIn() +
    (interval('a', 'z') + interval('A', 'Z')+ interval('0', '9') + '-').regexIn().between(0, 64) +
    (
        '.'.regex() +
        (interval('a', 'z') + interval('A', 'Z')+ interval('0', '9')).regexIn() +
        (interval('a', 'z') + interval('A', 'Z')+ interval('0', '9') + '-').regexIn().between(0, 25)
    ).oneOrMore()

/**
 * Obtain capture value by a group
 */
fun Matcher.group(group: Group) =
    this.group(group.id)

/**
 * Read Int from [Matcher] group
 */
fun Matcher.int(group: Int, defaultValue: Int = 0) =
    this.group(group)
        ?.toInt()
    ?: defaultValue

/**
 * Read Int from [Matcher] group
 */
fun Matcher.int(group: Group, defaultValue: Int = 0) =
    this.int(group.id, defaultValue)

/**
 * Read Double from [Matcher] group
 */
fun Matcher.double(group: Int, defaultValue: Double = 0.0) =
    this.group(group)
        ?.toDouble()
    ?: defaultValue

/**
 * Read Double from [Matcher] group
 */
fun Matcher.double(group: Group, defaultValue: Double = 0.0) =
    this.double(group.id, defaultValue)

/**
 * Read non `null` String from [Matcher] group
 */
fun Matcher.string(group: Int, defaultValue: String = "") =
    this.group(group)
    ?: defaultValue

/**
 * Read non `null` String from [Matcher] group
 */
fun Matcher.string(group: Group, defaultValue: String = "") =
    this.string(group.id, defaultValue)
