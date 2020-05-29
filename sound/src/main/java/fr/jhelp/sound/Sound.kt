/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.sound

import androidx.annotation.RawRes
import fr.jhelp.utilities.bounds

class Sound(@RawRes internal val resource: Int, leftVolume: Float = 1f, rightVolume: Float = 1f,
            val loop: Int = 0)
{
    var leftVolume = leftVolume.bounds(0f, 1f)
        private set
    var rightVolume = rightVolume.bounds(0f, 1f)
        private set
    internal var soundId = -1
    internal var streamId = -1

    fun play()
    {
        SoundManager.sound(this)
    }

    fun pause()
    {
        SoundManager.pause(this)
    }

    fun resume()
    {
        SoundManager.resume(this)
    }

    fun stop()
    {
        SoundManager.stop(this)
    }

    fun setVolume(leftVolume: Float, rightVolume: Float = leftVolume)
    {
        this.leftVolume = leftVolume.bounds(0f, 1f)
        this.rightVolume = rightVolume.bounds(0f, 1f)
        SoundManager.changeVolume(this)
    }
}