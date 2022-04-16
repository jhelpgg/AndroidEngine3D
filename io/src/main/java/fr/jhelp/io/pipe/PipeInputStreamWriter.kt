/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.io.pipe

import java.io.OutputStream

class PipeInputStreamWriter internal constructor(private val pipeInputStream: PipeInputStream): OutputStream()
{
    override fun write(byte: Int)
    {
        this.pipeInputStream.streamArray.write(byte)
        this.pipeInputStream.newDataAvailable()
    }

    override fun write(byteArray: ByteArray)
    {
        this.pipeInputStream.streamArray.write(byteArray,0,byteArray.size)
        this.pipeInputStream.newDataAvailable()
    }

    override fun write(byteArray: ByteArray, offset: Int, length: Int)
    {
        this.pipeInputStream.streamArray.write(byteArray,offset, length)
        this.pipeInputStream.newDataAvailable()
    }

    override fun close()
    {
        this.pipeInputStream.close()
    }

    override fun flush()
    {
    }
}
