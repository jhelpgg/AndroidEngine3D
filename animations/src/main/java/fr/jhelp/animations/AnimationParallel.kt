/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.animations

/**
 * Launch several animations in "same time"
 */
class AnimationParallel : Animation(25)
{
    private val animations = ArrayList<Animation>()
    private var length = 0

    fun add(animation: Animation)
    {
        synchronized(this.animations)
        {
            this.animations.add(animation)
        }
    }

    override fun initialize()
    {
        synchronized(this.animations)
        {
            this.length = this.animations.size

            for (index in 0 until this.length)
            {
                this.animations[index].start()
            }
        }
    }

    override fun animate(frame: Float): Boolean
    {
        var animate = false

        for (index in 0 until this.length)
        {
            animate = this.animations[index].animate() || animate
        }

        return animate
    }
}