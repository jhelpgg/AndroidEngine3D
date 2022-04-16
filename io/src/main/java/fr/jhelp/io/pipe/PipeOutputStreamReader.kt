/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.io.pipe

import java.io.InputStream
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max

class PipeOutputStreamReader internal constructor(private val pipeOutputStream: PipeOutputStream) :
    InputStream()
{
    private val closed = AtomicBoolean(false)
    private val numberDataRequested = AtomicInteger(0)
    private val lock = Object()
    private val waiting = AtomicBoolean(false)

    override fun read(): Int
    {
        if (this.closed.get())
        {
            return -1
        }

        if (this.pipeOutputStream.streamArray.size < 1)
        {
            this.numberDataRequested.incrementAndGet()
            this.waitForEnoughData()
        }

        return this.pipeOutputStream.streamArray.read()
    }

    override fun read(buffer: ByteArray, offset: Int, length: Int): Int
    {
        if (this.closed.get())
        {
            return -1
        }

        if (this.pipeOutputStream.streamArray.size < length)
        {
            val number = length - this.pipeOutputStream.streamArray.size
            this.numberDataRequested.addAndGet(number)
            this.waitForEnoughData()
        }

        return this.pipeOutputStream.streamArray.read(buffer, offset, length)
    }

    override fun skip(length: Long): Long
    {
        if (this.closed.get())
        {
            return 0L
        }

        if (this.pipeOutputStream.streamArray.size < length)
        {
            val number = (length - this.pipeOutputStream.streamArray.size).toInt()
            this.numberDataRequested.addAndGet(number)
            this.waitForEnoughData()
        }

        return this.pipeOutputStream.streamArray.skip(length)
    }

    override fun available(): Int =
        this.pipeOutputStream.streamArray.size

    override fun close()
    {
        if (this.closed.compareAndSet(false, true))
        {
            this.numberDataRequested.set(0)
            this.checkup()
        }
    }

    internal fun newDataAvailable()
    {
        val number = max(0, this.pipeOutputStream.streamArray.size - this.numberDataRequested.get())
        this.numberDataRequested.getAndAdd(-number)
        this.checkup()
    }

    private fun waitForEnoughData()
    {
        synchronized(this.lock)
        {
            this.waiting.set(true)
            while (this.numberDataRequested.get() > 0)
            {
                this.lock.wait()
            }
            this.waiting.set(false)
        }
    }

    private fun checkup()
    {
        synchronized(this.lock)
        {
            if (this.waiting.get())
            {
                this.lock.notify()
            }
        }
    }
}