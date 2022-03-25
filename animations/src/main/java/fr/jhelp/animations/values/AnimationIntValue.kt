/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.animations.values

import java.util.concurrent.atomic.AtomicReference

/**
 * Animated int that have frame like milestone value must pass at given ime after start or at specific frame
 */
class AnimationIntValue(reference: AtomicReference<Int>) :
    AnimationValue<Int>(reference)
{
    /**
     * Called each time value should be interpolated
     */
    override fun interpolateValue(animated: AtomicReference<Int>,
                                  before: Int, after: Int,
                                  percent: Float)
    {
        animated.set(((1f - percent) * before + percent * after).toInt())
    }
}