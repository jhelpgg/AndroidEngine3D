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