/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.io.extensions

import fr.jhelp.io.readFull
import fr.jhelp.utilities.extensions.fromUtf8
import java.io.IOException
import java.io.InputStream


@Throws(IOException::class)
fun InputStream.readBuffer(size: Int): ByteArray
{
    if (size <= 0)
    {
        return ByteArray(0)
    }

    val buffer = ByteArray(size)
    val read = this.readFull(buffer)

    if (read == size)
    {
        return buffer
    }

    throw IOException("No enough data to read $size bytes")
}

@Throws(IOException::class)
fun InputStream.readInt(): Int
{
    val buffer = this.readBuffer(4)
    return ((buffer[0].toInt() and 0xFF) shl 24) or
            ((buffer[1].toInt() and 0xFF) shl 16) or
            ((buffer[2].toInt() and 0xFF) shl 8) or
            (buffer[3].toInt() and 0xFF)
}

@Throws(IOException::class)
fun InputStream.readLong(): Long
{
    val buffer = this.readBuffer(8)
    return ((buffer[0].toLong() and 0xFF) shl 56) or
            ((buffer[1].toLong() and 0xFF) shl 48) or
            ((buffer[2].toLong() and 0xFF) shl 40) or
            ((buffer[3].toLong() and 0xFF) shl 32) or
            ((buffer[4].toLong() and 0xFF) shl 24) or
            ((buffer[5].toLong() and 0xFF) shl 16) or
            ((buffer[6].toLong() and 0xFF) shl 8) or
            (buffer[7].toLong() and 0xFF)
}

@Throws(IOException::class)
fun InputStream.readFloat(): Float =
    Float.fromBits(this.readInt())

@Throws(IOException::class)
fun InputStream.readDouble(): Double =
    Double.fromBits(this.readLong())

@Throws(IOException::class)
fun InputStream.readString(): String
{
    val size = this.readInt()

    if (size < 0)
    {
        throw IOException("Invalid string")
    }

    if (size == 0)
    {
        return ""
    }

    return this.readBuffer(size).fromUtf8()
}

@Throws(IOException::class)
fun InputStream.readBoolean(): Boolean =
    when(this.read())
    {
        0 -> false
        1 -> true
        else -> throw IOException("Invalid boolean")
    }
