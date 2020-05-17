package fr.jhelp.utilities.extensions

import fr.jhelp.utilities.regex.Group
import fr.jhelp.utilities.regex.oneOrMore
import fr.jhelp.utilities.regex.plus
import fr.jhelp.utilities.regex.regex
import fr.jhelp.utilities.regex.regexIn
import fr.jhelp.utilities.regex.zeroOrMore
import fr.jhelp.utilities.regex.zeroOrOne
import fr.jhelp.utilities.text.interval
import java.util.regex.Matcher

val DIGIT_REGEX = interval('0', '9').regexIn()

val LETTER_REGEX = (interval('a', 'z') + interval('A', 'Z')).regexIn()

val DIGIT_LETTER_UNDERSCORE_REGEX =
    (interval('a', 'z') + interval('A', 'Z') + interval('0', '9') + '_').regexIn()

val INTEGER_REGEX = charArrayOf('+', '-').regex().zeroOrOne() + DIGIT_REGEX.oneOrMore()

val REAL_REGEX = INTEGER_REGEX + ('.'.regex() + DIGIT_REGEX.oneOrMore()).zeroOrOne()

val VARIABLE_NAME_REGEX = LETTER_REGEX + DIGIT_LETTER_UNDERSCORE_REGEX.zeroOrMore()

fun Matcher.group(group: Group) =
    this.group(group.id)

/**
 * Read Int from [Matcher] group
 */
fun Matcher.int(group: Int, defaultValue: Int = 0) =
    this.group(group)
        ?.toInt()
    ?: defaultValue

fun Matcher.int(group: Group, defaultValue: Int = 0) =
    this.int(group.id, defaultValue)

/**
 * Read Double from [Matcher] group
 */
fun Matcher.double(group: Int, defaultValue: Double = 0.0) =
    this.group(group)
        ?.toDouble()
    ?: defaultValue

fun Matcher.double(group: Group, defaultValue: Double = 0.0) =
    this.double(group.id, defaultValue)

/**
 * Read non `null` String from [Matcher] group
 */
fun Matcher.string(group: Int, defaultValue: String = "") =
    this.group(group)
    ?: defaultValue

fun Matcher.string(group: Group, defaultValue: String = "") =
    this.string(group.id, defaultValue)
