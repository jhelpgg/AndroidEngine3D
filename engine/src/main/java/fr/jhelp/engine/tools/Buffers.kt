/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.engine.tools

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer

/**Create Byte buffer*/
fun byteBuffer(size: Int): ByteBuffer =
    ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder())

/**Create Int buffer*/
fun intBuffer(size: Int): IntBuffer =
    byteBuffer(size * 4).asIntBuffer()

/**Create Float buffer*/
fun floatBuffer(size: Int): FloatBuffer =
    byteBuffer(size * 4).asFloatBuffer()

/**
 * Create Float buffer from an array part
 *
 * @param array Array source
 * @param offset Offset where start copy from given array
 * @param length Number of elements to copy
 * @return Create Float buffer
 */
fun floatBuffer(array: FloatArray, offset: Int = 0, length: Int = array.size - offset): FloatBuffer
{
    val buffer = floatBuffer(length)
    buffer.put(array, offset, length)
    buffer.position(0)
    return buffer
}
