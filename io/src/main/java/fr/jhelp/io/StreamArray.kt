/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package  fr.jhelp.io

import fr.jhelp.utilities.minimum
import java.io.InputStream
import java.io.OutputStream
import kotlin.math.min

/**
 * List of bytes can be read as an [InputStream] and write an an [OutputStream]
 */
class StreamArray
{
    private var data = ByteArray(4096)
    private var start = 0
    private var end = 0

    /**Array size*/
    val size
        get() =
            if (this.start <= this.end) this.end - this.start
            else this.data.size - this.start + this.end

    /**Associated [InputStream] to read data*/
    val inputStream: InputStream =
        InputStreamArray(this)

    /**Associated [OutputStream] to write data*/
    val outputStream: OutputStream =
        OutputStreamArray(this)

    /**
     * Expand, if need, the size
     */
    private fun expand(more: Int)
    {
        val size = this.size
        val dataSize = this.data.size

        if (size + more >= dataSize)
        {
            var newSize = size + more
            newSize += newSize shr 3
            val buffer = ByteArray(newSize)

            if (this.start <= this.end)
            {
                System.arraycopy(this.data, this.start, buffer, 0, size)
            }
            else
            {
                val first = dataSize - this.start
                System.arraycopy(this.data, this.start, buffer, 0, first)
                System.arraycopy(this.data, 0, buffer, first, this.end)
            }

            this.start = 0
            this.end = size
            this.data = buffer
        }
    }

    /**
     * Read, if possible, one byte
     *
     * @return The read byte in [0, 255] or -1 if no more data to read
     */
    fun read(): Int
    {
        if (this.size == 0)
        {
            return -1
        }

        val read = this.data[this.start].toInt() and 0xFF
        this.start = (this.start + 1) % this.data.size
        return read
    }

    /**
     * Read data and fill given array with it
     *
     * @param buffer Array to fill with data
     * @param offset Offset where start write data in given array
     * @param length Number maximum data to read
     * @return Number data read  or -1 if no more data to read
     */
    fun read(buffer: ByteArray, offset: Int, length: Int): Int
    {
        if (this.size == 0)
        {
            return -1
        }

        var start = offset
        var size = length

        if (start < 0)
        {
            size += start
            start = 0
        }

        size = minimum(this.size, size, buffer.size - start)

        if (size <= 0)
        {
            return 0
        }

        if (this.start <= this.end)
        {
            System.arraycopy(this.data, this.start, buffer, start, size)
        }
        else
        {
            val first = this.data.size - this.start

            if (size <= first)
            {
                System.arraycopy(this.data, this.start, buffer, start, size)
            }
            else
            {
                System.arraycopy(this.data, this.start, buffer, start, first)
                System.arraycopy(this.data, 0, buffer, start + first, size - first)
            }
        }

        this.start = (this.start + size) % this.data.size
        return size
    }

    /**
     * Skip a number of data to read
     *
     * @param number Number bytes to skip
     * @return Number of effective data skip
     */
    fun skip(number: Long): Long
    {
        val nb = number.toInt()

        if (nb <= 0)
        {
            return 0L
        }

        val skip = min(this.size, nb)
        this.start = (this.start + skip) % this.data.size
        return skip.toLong()
    }

    /**
     * Write a byte
     */
    fun write(data: Int)
    {
        this.expand(1)
        this.data[this.end] = data.toByte()
        this.end = (this.end + 1) % this.data.size
    }

    /**
     * Write an array of bytes
     *
     * @param buffer Array to write
     * @param offset Offset where start read the given array
     * @param length Number of bytes to write
     */
    fun write(buffer: ByteArray, offset: Int, length: Int)
    {
        var start = offset
        var size = length

        if (start < 0)
        {
            size += start
            start = 0
        }

        size = min(size, buffer.size - start)

        if (size <= 0)
        {
            return
        }

        this.expand(size)
        System.arraycopy(buffer, start, this.data, this.end, size)
        this.end = (this.end + size) % this.data.size
    }
}