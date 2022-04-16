/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.tasks

import android.os.Looper
import fr.jhelp.testor.loopAllNow
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

class TasksTests
{
    @Test
    fun parallelTest()
    {
        val done = AtomicBoolean(false)
        ({
            Thread.sleep(1024)
            done.set(true)
        }).parallel()
        Assertions.assertFalse(done.get())
        Thread.sleep(1234)
        Assertions.assertTrue(done.get())

        done.set(false)
        var value = ""
        ({ parameter: String ->
            value = parameter
            Thread.sleep(1024)
            done.set(true)
        }).parallel("Hello")
        Assertions.assertFalse(done.get())
        Thread.sleep(1234)
        Assertions.assertTrue(done.get())
        Assertions.assertEquals("Hello", value)

        done.set(false)
        value = ""
        ({ parameter: String ->
            value = parameter
            Thread.sleep(1024)
            done.set(true)
        }).parallel("Hello")
        Assertions.assertFalse(done.get())
        Thread.sleep(1234)
        Assertions.assertTrue(done.get())
        Assertions.assertEquals("Hello", value)
    }

    @Test
    fun parallelUITest()
    {
        val done = AtomicBoolean(false)
        var inMainThread = false
        ({
            inMainThread = Looper.myLooper() == Looper.getMainLooper()
            Thread.sleep(1024)
            done.set(true)
        }).parallel(ThreadType.UI)
        Assertions.assertFalse(done.get())
        loopAllNow()
        Thread.sleep(1234)
        loopAllNow()
        Assertions.assertTrue(done.get())
        Assertions.assertTrue(inMainThread)
    }

    @Test
    fun launchTest()
    {
        val result = AtomicReference(Status.UNKNOWN)
        val exception = AtomicReference<Exception>(null)
        ({ }).parallel()
            .and { result.set(Status.SUCCEED) }
            .onError { error ->
                result.set(Status.FAILED)
                exception.set(error)
            }.waitComplete()
        Thread.sleep(256)
        Assertions.assertEquals(Status.SUCCEED, result.get())
        Assertions.assertNull(exception.get())

        result.set(Status.UNKNOWN)
        exception.set(null)
        ({ throw RuntimeException("Failed") })
            .parallel()
            .and { result.set(Status.SUCCEED) }
            .onError { error ->
                result.set(Status.FAILED)
                exception.set(error)
            }.waitComplete()
        Thread.sleep(256)
        Assertions.assertEquals(Status.FAILED, result.get())
        val error = exception.get()!!
        Assertions.assertEquals("Failed", error.message)
        Assertions.assertTrue(error is RuntimeException)
    }
}