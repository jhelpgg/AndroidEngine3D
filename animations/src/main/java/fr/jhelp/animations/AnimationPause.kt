/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.animations

import fr.jhelp.animations.Animation

/**
 * Animation that make a pause
 */
class AnimationPause(milliseconds: Int) : Animation(25)
{
    private val numberFrame = milliseconds / 40f

    override fun animate(frame: Float): Boolean =
        frame <= this.numberFrame
}