/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.utilities.extensions

import fr.jhelp.utilities.regex.between
import fr.jhelp.utilities.regex.group
import fr.jhelp.utilities.regex.plus
import fr.jhelp.utilities.regex.regex
import fr.jhelp.utilities.regex.regexIn
import fr.jhelp.utilities.regex.zeroOrOne
import fr.jhelp.utilities.text.interval
import java.util.Calendar

private val YEAR_REGEX = INTEGER_REGEX.group()
private val MONTH_REGEX =
    (interval('0', '1').regexIn().zeroOrOne() + interval('0', '9').regexIn()).group()
private val DAY_REGEX =
    (interval('0', '3').regexIn().zeroOrOne() + interval('0', '9').regexIn()).group()
private val HOUR_REGEX =
    (interval('0', '2').regexIn().zeroOrOne() + interval('0', '9').regexIn()).group()
private val MINUTE_REGEX =
    (interval('0', '5').regexIn().zeroOrOne() + interval('0', '9').regexIn()).group()
private val SECOND_REGEX =
    (interval('0', '5').regexIn().zeroOrOne() + interval('0', '9').regexIn()).group()
private val MILLISECOND_REGEX = interval('0', '9').regexIn().between(1, 3).group()

private val DATE_REGEX = YEAR_REGEX + '/'.regex() +
                         MONTH_REGEX + '/'.regex() +
                         DAY_REGEX + '-'.regex() +
                         HOUR_REGEX + 'h'.regex() +
                         MINUTE_REGEX + 'm'.regex() +
                         SECOND_REGEX + 's'.regex() +
                         MILLISECOND_REGEX

/**
 * Serialize a [Calendar]
 *
 * Can be parse again with [String.parseCalendar]
 */
fun Calendar.serialize() =
    StringBuilder()
        .append(this[Calendar.YEAR])
        .append("/")
        .append(this[Calendar.MONTH] + 1)
        .append("/")
        .append(this[Calendar.DAY_OF_MONTH])
        .append("-")
        .append(this[Calendar.HOUR_OF_DAY])
        .append("h")
        .append(this[Calendar.MINUTE])
        .append("m")
        .append(this[Calendar.SECOND])
        .append("s")
        .append(this[Calendar.MILLISECOND])
        .toString()

/**
 * Parse [Calendar] serialized with [Calendar.serialize]
 */
fun String.parseCalendar(): Calendar
{
    val matcher = DATE_REGEX.matcher(this)

    if (!matcher.matches())
    {
        throw IllegalArgumentException("$this not a valid calendar")
    }

    val calendar = Calendar.getInstance()
    calendar.set(matcher.int(YEAR_REGEX),
                 matcher.int(MONTH_REGEX) - 1,
                 matcher.int(DAY_REGEX),
                 matcher.int(HOUR_REGEX),
                 matcher.int(MINUTE_REGEX),
                 matcher.int(SECOND_REGEX))
    calendar.set(Calendar.MILLISECOND, matcher.int(MILLISECOND_REGEX))
    return calendar
}

/**
 * Compute age of given date
 */
val Calendar.age
    get(): Int
    {
        val thisYear = this[Calendar.YEAR]
        val thisMonth = this[Calendar.MONTH]
        val thisDay = this[Calendar.DAY_OF_MONTH]
        val now = Calendar.getInstance()
        val nowYear = now[Calendar.YEAR]
        val nowMonth = now[Calendar.MONTH]
        val nowDay = now[Calendar.DAY_OF_MONTH]
        val minus =
            if (thisMonth > nowMonth || (thisMonth == nowMonth && thisDay > nowDay)) 1
            else 0
        return nowYear - thisYear - minus
    }
