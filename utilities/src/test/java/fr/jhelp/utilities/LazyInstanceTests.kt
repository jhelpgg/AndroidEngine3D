/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.utilities

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
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
        Assertions.assertEquals(0, count.get())

        lazyString { Assertions.assertEquals("Hello", this) }
        Assertions.assertEquals(1, count.get())

        lazyString { Assertions.assertEquals("Hello".length, this.length) }
        Assertions.assertEquals(1, count.get())
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
        Assertions.assertEquals(0, count.get())
        Assertions.assertFalse(called.get())

        lazyString { Assertions.assertEquals("Hello", this) }
        Assertions.assertEquals(1, count.get())

        lazyString.ifCreated { called.set(true) }
        Assertions.assertEquals(1, count.get())
        Assertions.assertTrue(called.get())
    }
}