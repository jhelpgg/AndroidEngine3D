/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.io.extensions

import fr.jhelp.utilities.extensions.toUtf8
import java.io.IOException
import java.io.OutputStream

@Throws(IOException::class)
fun OutputStream.writeInt(value: Int)
{
    this.write(byteArrayOf(
        ((value shr 24) and 0xFF).toByte(),
        ((value shr 16) and 0xFF).toByte(),
        ((value shr 8) and 0xFF).toByte(),
        (value and 0xFF).toByte()))
}

@Throws(IOException::class)
fun OutputStream.writeLong(value: Long)
{
    this.write(byteArrayOf(
        ((value shr 56) and 0xFF).toByte(),
        ((value shr 48) and 0xFF).toByte(),
        ((value shr 40) and 0xFF).toByte(),
        ((value shr 32) and 0xFF).toByte(),
        ((value shr 24) and 0xFF).toByte(),
        ((value shr 16) and 0xFF).toByte(),
        ((value shr 8) and 0xFF).toByte(),
        (value and 0xFF).toByte()))
}

@Throws(IOException::class)
fun OutputStream.writeFloat(value: Float)
{
    this.writeInt(value.toBits())
}

@Throws(IOException::class)
fun OutputStream.writeDouble(value: Double)
{
    this.writeLong(value.toBits())
}

@Throws(IOException::class)
fun OutputStream.writeString(value: String)
{
    if (value.isEmpty())
    {
        this.writeInt(0)
        return
    }

    this.writeInt(value.length)
    this.write(value.toUtf8())
}

@Throws(IOException::class)
fun OutputStream.writeBoolean(value: Boolean)
{
    if (value)
    {
        this.write(1)
    }
    else
    {
        this.write(0)
    }
}
