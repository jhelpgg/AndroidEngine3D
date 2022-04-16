/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.io.pipe

import fr.jhelp.io.StreamArray
import fr.jhelp.tasks.parallel
import java.io.OutputStream

class PipeOutputStream(reader: (PipeOutputStreamReader) -> Unit) : OutputStream()
{
    private val pipeOutputStreamReader = PipeOutputStreamReader(this)
    internal val streamArray = StreamArray()

    init
    {
        { reader(this.pipeOutputStreamReader) }.parallel()
    }

    override fun write(byte: Int)
    {
        this.streamArray.write(byte)
        this.pipeOutputStreamReader.newDataAvailable()
    }

    override fun write(byteArray: ByteArray)
    {
        this.streamArray.write(byteArray, 0, byteArray.size)
        this.pipeOutputStreamReader.newDataAvailable()
    }

    override fun write(byteArray: ByteArray, offset: Int, length: Int)
    {
        this.streamArray.write(byteArray, offset, length)
        this.pipeOutputStreamReader.newDataAvailable()
    }

    override fun close()
    {
    }

    override fun flush()
    {
    }
}
