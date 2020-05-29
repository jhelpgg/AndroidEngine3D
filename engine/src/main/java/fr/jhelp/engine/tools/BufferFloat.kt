/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.engine.tools

import java.nio.FloatBuffer
import java.util.concurrent.atomic.AtomicBoolean

class BufferFloat
{
    private var array: FloatArray? = FloatArray(128)
    private val dirty = AtomicBoolean(true)
    private lateinit var floatBuffer: FloatBuffer
    var size = 0
        private set

    val sealed get() = synchronized(this.dirty) { this.array == null }

    private fun expand(more: Int)
    {
        val array = this.array ?: return

        if (this.size + more >= array.size)
        {
            var newSize = this.size + more
            newSize += newSize shr 3
            val buffer = FloatArray(newSize)
            System.arraycopy(array, 0, buffer, 0, this.size)
            this.array = buffer
        }
    }

    fun add(vararg floats: Float)
    {
        synchronized(this.dirty)
        {
            this.expand(floats.size)
            val array = this.array ?: return
            this.dirty.set(true)

            System.arraycopy(floats, 0, array, this.size, floats.size)
            this.size += floats.size
        }
    }

    fun buffer(): FloatBuffer
    {
        synchronized(this.dirty)
        {
            if (this.dirty.getAndSet(false))
            {
                this.floatBuffer = floatBuffer(this.array!!, 0, this.size)
            }

            return this.floatBuffer
        }
    }

    fun seal()
    {
        synchronized(this.dirty)
        {
            if (this.dirty.getAndSet(false))
            {
                this.floatBuffer = floatBuffer(this.array!!, 0, this.size)
            }

            this.array = null
        }
    }
}