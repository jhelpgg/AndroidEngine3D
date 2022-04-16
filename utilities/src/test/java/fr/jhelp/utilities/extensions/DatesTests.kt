/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.utilities.extensions

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.Calendar

class DatesTests
{
    @Test
    fun serialize()
    {
        val calendar = Calendar.getInstance()
        calendar.set(1985, Calendar.FEBRUARY, 12, 3, 42, 1)
        calendar.set(Calendar.MILLISECOND, 666)
        Assertions.assertEquals("1985/2/12-3h42m1s666", calendar.serialize())
    }

    @Test
    fun parseOk()
    {
        val calendar = "1985/2/12-3h42m01s666".parseCalendar()
        Assertions.assertEquals(1985, calendar[Calendar.YEAR])
        Assertions.assertEquals(Calendar.FEBRUARY, calendar[Calendar.MONTH])
        Assertions.assertEquals(12, calendar[Calendar.DAY_OF_MONTH])
        Assertions.assertEquals(3, calendar[Calendar.HOUR_OF_DAY])
        Assertions.assertEquals(42, calendar[Calendar.MINUTE])
        Assertions.assertEquals(1, calendar[Calendar.SECOND])
        Assertions.assertEquals(666, calendar[Calendar.MILLISECOND])
    }

    @Test
    fun parseKO()
    {
        try
        {
            val calendar = "1985/73/12-3h42m01s666".parseCalendar()
            Assertions.fail<Unit>("Parse should fail : $calendar")
        }
        catch (exception: IllegalArgumentException)
        {
            // That's what we expect
        }
    }
}
