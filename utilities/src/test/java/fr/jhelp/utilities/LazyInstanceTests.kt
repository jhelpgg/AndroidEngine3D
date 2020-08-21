/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.utilities

import org.junit.Assert
import org.junit.Test
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class LazyInstanceTests
{
    @Test
    fun invokeTest()
    {
        val count = AtomicInteger(0)

        val lazyString = LazyInstance<String> {
            count.incrementAndGet()
            "Hello"
        }
        Assert.assertEquals(0, count.get())

        lazyString { Assert.assertEquals("Hello", this) }
        Assert.assertEquals(1, count.get())

        lazyString { Assert.assertEquals("Hello".length, this.length) }
        Assert.assertEquals(1, count.get())
    }

    @Test
    fun ifCreatedTest()
    {
        val count = AtomicInteger(0)
        val lazyString = LazyInstance<String> {
            count.incrementAndGet()
            "Hello"
        }
        val called = AtomicBoolean(false)
        lazyString.ifCreated { called.set(true) }
        Assert.assertEquals(0, count.get())
        Assert.assertFalse(called.get())

        lazyString { Assert.assertEquals("Hello", this) }
        Assert.assertEquals(1, count.get())

        lazyString.ifCreated { called.set(true) }
        Assert.assertEquals(1, count.get())
        Assert.assertTrue(called.get())
    }
}