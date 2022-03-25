/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.animations.values

import fr.jhelp.animations.keyFrame.AnimationKeyFrame
import java.util.concurrent.atomic.AtomicReference

/**
 * Animated generic value that have frame like milestone value must pass at given ime after start or at specific frame
 */
abstract class AnimationValue<V : Any>(reference: AtomicReference<V>) :
    AnimationKeyFrame<AtomicReference<V>, V>(reference, 25)
{
    /**
     * Called each time current value is neessary
     */
    final override fun obtainValue(animated: AtomicReference<V>): V =
        animated.get()

    /**
     * CCalled each time value changed and need to be set
     */
    final override fun setValue(animated: AtomicReference<V>, value: V)
    {
        animated.set(value)
    }
}