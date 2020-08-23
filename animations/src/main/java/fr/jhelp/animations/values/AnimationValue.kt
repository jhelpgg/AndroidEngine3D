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

abstract class AnimationValue<V : Any>(reference: AtomicReference<V>) :
    AnimationKeyFrame<AtomicReference<V>, V>(reference, 25)
{
    final override fun obtainValue(animated: AtomicReference<V>): V =
        animated.get()

    final override fun setValue(animated: AtomicReference<V>, value: V)
    {
        animated.set(value)
    }
}