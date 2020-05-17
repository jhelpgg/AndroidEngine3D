package  fr.jhelp.io

import java.io.InputStream

/**
 * Input stream associated to [StreamArray]
 */
internal class InputStreamArray(private val streamArray: StreamArray) : InputStream()
{
    override fun skip(n: Long) =
        this.streamArray.skip(n)

    override fun available() = this.streamArray.size

    override fun reset() = Unit

    override fun close() = Unit

    override fun mark(readlimit: Int) = Unit

    override fun markSupported() = false

    override fun read() =
        this.streamArray.read()

    override fun read(b: ByteArray) =
        this.streamArray.read(b, 0, b.size)

    override fun read(b: ByteArray, off: Int, len: Int) =
        this.streamArray.read(b, off, len)
}