/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.engine.scene

import fr.jhelp.engine.OpenGLThread
import fr.jhelp.engine.tools.intBuffer
import fr.jhelp.lists.Queue
import fr.jhelp.utilities.log
import javax.microedition.khronos.opengles.GL10

internal object DeleteTexture
{
    private val intBuffer = intBuffer(1)
    private val lock = Object()
    private val destroyList = Queue<Int>()

    fun freeTexture(videoID: Int)
    {
        if (videoID >= 0)
        {
            synchronized(this.lock)
            {
                this.destroyList.enqueue(videoID)
            }
        }
    }

    @OpenGLThread
    fun freeNext(gl: GL10)
    {
        val videoID =
            synchronized(this.lock)
            {
                if (this.destroyList.empty) -1
                else this.destroyList.dequeue()
            }

        if (videoID > 0)
        {
            this.intBuffer.rewind()
            this.intBuffer.put(videoID)
            this.intBuffer.rewind()
            gl.glDeleteTextures(1, this.intBuffer)
        }
    }
}