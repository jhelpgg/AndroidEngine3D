package fr.jhelp.engine.tools

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer

fun byteBuffer(size: Int): ByteBuffer =
    ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder())


fun intBuffer(size: Int): IntBuffer =
    byteBuffer(size * 4).asIntBuffer()

fun floatBuffer(size: Int): FloatBuffer =
    byteBuffer(size * 4).asFloatBuffer()

fun floatBuffer(array: FloatArray, offset: Int = 0, length: Int = array.size - offset): FloatBuffer
{
    val buffer = floatBuffer(length)
    buffer.put(array, offset, length)
    buffer.position(0)
    return buffer
}

fun fillFloatArray(buffer: FloatBuffer, array: FloatArray, offset: Int = 0,
                   length: Int = array.size - offset)
{
    buffer.position(0)
    buffer.get(array, offset, length)
}