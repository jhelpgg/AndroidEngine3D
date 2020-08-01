/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.utilities

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Test
import java.util.Locale

class UtilitiesTestsInstrumented
{
    @Test
    fun localizedString()
    {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("One", localizedString(R.string.testOne, Locale.ENGLISH, appContext))
        Assert.assertEquals("Un", localizedString(R.string.testOne, Locale.FRENCH, appContext))
        Assert.assertEquals("Ein", localizedString(R.string.testOne, Locale.GERMAN, appContext))
        // Default
        Assert.assertEquals("One", localizedString(R.string.testOne, Locale.JAPANESE, appContext))
    }
}