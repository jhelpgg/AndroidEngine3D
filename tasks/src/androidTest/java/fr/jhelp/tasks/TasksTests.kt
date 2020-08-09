/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.tasks

import android.os.Looper
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

class TasksTests
{
    @Test
    fun parallelTest()
    {
        val done = AtomicBoolean(false)
        parallel {
            Thread.sleep(1024)
            done.set(true)
        }
        Assert.assertFalse(done.get())
        Thread.sleep(1234)
        Assert.assertTrue(done.get())

        done.set(false)
        var value = ""
        parallel("Hello") { parameter ->
            value = parameter
            Thread.sleep(1024)
            done.set(true)
        }
        Assert.assertFalse(done.get())
        Thread.sleep(1234)
        Assert.assertTrue(done.get())
        Assert.assertEquals("Hello", value)

        done.set(false)
        value = ""
        var value2 = ""
        parallel("Hello", "World") { parameter1, parameter2 ->
            value = parameter1
            value2 = parameter2
            Thread.sleep(1024)
            done.set(true)
        }
        Assert.assertFalse(done.get())
        Thread.sleep(1234)
        Assert.assertTrue(done.get())
        Assert.assertEquals("Hello", value)
        Assert.assertEquals("World", value2)

        done.set(false)
        value = ""
        value2 = ""
        var value3 = ""
        parallel("Hello", "World", "!") { parameter1, parameter2, parameter3 ->
            value = parameter1
            value2 = parameter2
            value3 = parameter3
            Thread.sleep(1024)
            done.set(true)
        }
        Assert.assertFalse(done.get())
        Thread.sleep(1234)
        Assert.assertTrue(done.get())
        Assert.assertEquals("Hello", value)
        Assert.assertEquals("World", value2)
        Assert.assertEquals("!", value3)
    }

    @Test
    fun parallelUITest()
    {
        val done = AtomicBoolean(false)
        var inMainThread = false
        parallelUI {
            inMainThread = Looper.myLooper() == Looper.getMainLooper()
            Thread.sleep(1024)
            done.set(true)
        }
        Assert.assertFalse(done.get())
        Thread.sleep(1234)
        Assert.assertTrue(done.get())
        Assert.assertTrue(inMainThread)
    }

    @Test
    fun parallelIOTest()
    {
        val parallelThread = AtomicReference<Thread>(null)
        val parallelIOThread = AtomicReference<Thread>(null)

        parallel { parallelThread.set(Thread.currentThread()) }
        parallelIO { parallelIOThread.set(Thread.currentThread()) }

        Thread.sleep(1234)

        val threadParallel = parallelThread.get()!!
        val threadParallelIO = parallelIOThread.get()!!
        Assert.assertNotEquals(threadParallel, threadParallelIO)
    }

    @Test
    fun launchTest()
    {
        val result = AtomicReference(Status.UNKNOWN)
        val exception = AtomicReference<Exception>(null)
        launch { }.and { result.set(Status.SUCCEED) }
            .onError { error ->
            result.set(Status.FAILED)
            exception.set(error)
        }.waitComplete()
        Thread.sleep(256)
        Assert.assertEquals(Status.SUCCEED, result.get())
        Assert.assertNull(exception.get())

        result.set(Status.UNKNOWN)
        exception.set(null)
        launch { throw RuntimeException("Failed") }
            .and { result.set(Status.SUCCEED) }
            .onError { error ->
                result.set(Status.FAILED)
                exception.set(error)
            }.waitComplete()
        Thread.sleep(256)
        Assert.assertEquals(Status.FAILED, result.get())
        val error = exception.get()!!
        Assert.assertEquals("Failed", error.message)
        Assert.assertTrue(error is RuntimeException)
    }
}