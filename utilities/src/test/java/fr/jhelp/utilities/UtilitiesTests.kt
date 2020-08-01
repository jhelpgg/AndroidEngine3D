/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.utilities

import android.util.Log
import org.junit.Assert
import org.junit.Test

class UtilitiesTests
{
    @Test
    fun log()
    {
        log { "Test" }

        Assert.assertEquals("UtilitiesTests", Log.tag)
        Assert.assertEquals("fr.jhelp.utilities.UtilitiesTests.log at 20 : Test", Log.message)
        Assert.assertNull(Log.throwable)
    }

    @Test
    fun logError()
    {
        logError(Exception("Exception")) { "Test" }

        Assert.assertEquals("UtilitiesTests", Log.tag)
        Assert.assertEquals("fr.jhelp.utilities.UtilitiesTests.logError at 30 : Test", Log.message)
        Assert.assertEquals("Exception", Log.throwable!!.message)
    }
}