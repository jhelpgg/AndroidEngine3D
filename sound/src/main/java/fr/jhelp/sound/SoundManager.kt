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

import android.media.AudioAttributes
import android.media.SoundPool
import android.util.SparseIntArray
import androidx.annotation.RawRes
import fr.jhelp.tasks.delay
import fr.jhelp.utilities.ContextReference

object SoundManager
{
    private val soundPool =
        SoundPool.Builder()
            .setMaxStreams(3)
            .setAudioAttributes(AudioAttributes.Builder()
                                    .setUsage(AudioAttributes.USAGE_GAME)
                                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                    .build())
            .build()

    private var effectSound = -1
    private var backgroundSound = -1
    private var currentSound: Sound? = null
    private val loadedSounds = SparseIntArray()

    fun background(@RawRes soundResource: Int)
    {
        this.play(soundResource, true)
    }

    fun effect(@RawRes soundResource: Int)
    {
        this.play(soundResource, false)
    }

    fun sound(sound: Sound)
    {
        this.currentSound?.let { current ->
            if (current.streamId >= 0)
            {
                this.soundPool.stop(current.streamId)
                sound.streamId = -1
            }
        }

        if (sound.soundId < 0)
        {
            sound.soundId =
                synchronized(this.loadedSounds)
                {
                    this.loadedSounds[sound.resource, -1]
                }


            if (sound.soundId < 0)
            {
                sound.soundId = this.soundPool.load(ContextReference, sound.resource, 1)
            }

            synchronized(this.loadedSounds)
            {
                this.loadedSounds.put(sound.resource, sound.soundId)
            }

            delay(1024, sound, this::playSound)
        }
        else
        {
            this.playSound(sound)
        }
    }

    fun pause(sound: Sound)
    {
        if (sound == this.currentSound && sound.streamId >= 0)
        {
            this.soundPool.pause(sound.streamId)
        }
    }

    fun resume(sound: Sound)
    {
        if (sound == this.currentSound && sound.streamId >= 0)
        {
            this.soundPool.resume(sound.streamId)
        }
    }

    fun stop(sound: Sound)
    {
        if (sound == this.currentSound && sound.streamId >= 0)
        {
            this.soundPool.stop(sound.streamId)
            sound.streamId = -1
        }
    }

    internal fun changeVolume(sound: Sound)
    {
        if (this.currentSound == sound && sound.streamId >= 0)
        {
            this.soundPool.setVolume(sound.streamId, sound.leftVolume, sound.rightVolume)
        }
    }

    private fun playSound(sound: Sound)
    {
        this.currentSound = sound
        sound.streamId =
            this.soundPool.play(sound.soundId, sound.leftVolume, sound.rightVolume, 1, sound.loop,
                                1f)
    }

    private fun play(@RawRes soundResource: Int, background: Boolean)
    {
        var soundId =
            synchronized(this.loadedSounds)
            {
                this.loadedSounds[soundResource, -1]
            }

        if (soundId > 0)
        {
            if (background)
            {
                this.playBackground(soundId)
            }
            else
            {
                this.playEffect(soundId)
            }
        }
        else
        {
            soundId = this.soundPool.load(ContextReference, soundResource, 1)

            synchronized(this.loadedSounds)
            {
                this.loadedSounds.put(soundResource, soundId)
            }

            if (background)
            {
                delay(1024, soundId, this::playBackground)
            }
            else
            {
                delay(1024, soundId, this::playEffect)
            }
        }
    }

    fun pause()
    {
        if (this.backgroundSound >= 0)
        {
            this.soundPool.pause(this.backgroundSound)
        }

        if (this.effectSound >= 0)
        {
            this.soundPool.stop(this.effectSound)
        }

        this.currentSound?.let { current -> this.soundPool.pause(current.streamId) }
    }

    fun resume()
    {
        if (this.backgroundSound >= 0)
        {
            this.soundPool.resume(this.backgroundSound)
        }

        this.currentSound?.let { current -> this.soundPool.resume(current.streamId) }
    }

    fun stopSounds()
    {
        if (this.backgroundSound >= 0)
        {
            this.soundPool.stop(this.backgroundSound)
            this.backgroundSound = -1
        }

        if (this.effectSound > 0)
        {
            this.soundPool.stop(this.effectSound)
            this.effectSound = -1
        }

        this.currentSound?.let { current ->
            if (current.streamId >= 0)
            {
                this.soundPool.stop(current.streamId)
            }

            current.soundId = -1
            current.streamId = -1
        }

        synchronized(this.loadedSounds)
        {
            for (index in 0 until this.loadedSounds.size())
            {
                this.soundPool.unload(this.loadedSounds.valueAt(index))
            }

            this.loadedSounds.clear()
        }
    }

    private fun playBackground(soundId: Int)
    {
        if (this.backgroundSound >= 0)
        {
            this.soundPool.stop(this.backgroundSound)
        }

        this.backgroundSound = this.soundPool.play(soundId, 1f, 1f, 1, -1, 1f)
    }

    private fun playEffect(soundId: Int)
    {
        if (this.effectSound > 0)
        {
            this.soundPool.stop(this.effectSound)
        }

        this.effectSound = this.soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
    }
}