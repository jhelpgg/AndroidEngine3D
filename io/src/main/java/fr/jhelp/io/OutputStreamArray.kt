/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package  fr.jhelp.io

import java.io.OutputStream

/**
 * Output stream associated to [StreamArray]
 */
internal class OutputStreamArray(private val streamArray: StreamArray) : OutputStream()
{
    override fun write(b: Int) =
        this.streamArray.write(b)

    override fun write(b: ByteArray) =
        this.streamArray.write(b, 0, b.size)

    override fun write(b: ByteArray, off: Int, len: Int) =
        this.streamArray.write(b, off, len)

    override fun flush() = Unit

    override fun close() = Unit
}