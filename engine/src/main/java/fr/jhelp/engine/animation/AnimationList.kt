/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.engine.animation

/**
 * List of animation played one after other
 */
class AnimationList() : Animation(25)
{
    private val animations = ArrayList<Animation>()
    private var started = false
    private var index = 0

    fun add(animation: Animation)
    {
        synchronized(this.animations)
        {
            this.animations.add(animation)
        }
    }

    override fun initialize()
    {
        this.started = false
        this.index = 0
    }

    override fun animate(frame: Float): Boolean
    {
        val animation =
            synchronized(this.animations)
            {
                if (this.index >= this.animations.size)
                {
                    return false
                }

                this.animations[this.index]
            }

        if (!this.started)
        {
            this.started = true
            animation.start()
        }

        if (!animation.animate())
        {
            animation.finished()
            this.index++

            synchronized(this.animations)
            {
                if (this.index >= this.animations.size)
                {
                    return false
                }

                this.animations[this.index].start()
            }
        }

        return true
    }
}