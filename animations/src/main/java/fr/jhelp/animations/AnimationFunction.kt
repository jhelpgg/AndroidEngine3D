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
 * Animation based on a function that consumes animation current frame
 */
class AnimationFunction(fps: Int, private val function: (Float) -> Boolean) : Animation(fps)
{
    constructor(function: (Float) -> Boolean) : this(25, function)

    override fun animate(frame: Float) = this.function(frame)
}