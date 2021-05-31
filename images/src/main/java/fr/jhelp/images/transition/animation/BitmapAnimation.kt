/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.images.transition.animation

import fr.jhelp.animations.keyFrame.AnimationKeyFrame

internal class BitmapAnimation(animationBitmap: AnimationBitmap, fps: Int) :
    AnimationKeyFrame<AnimationBitmap, BitmapAnimationElement>(animationBitmap, fps)
{
    override fun interpolateValue(animated: AnimationBitmap, before: BitmapAnimationElement,
                                  after: BitmapAnimationElement, percent: Float)
    {
        after.transition?.updatePercent(percent)
    }

    override fun obtainValue(animated: AnimationBitmap): BitmapAnimationElement =
        animated.firstElement ?: BitmapAnimationElement(null)

    override fun setValue(animated: AnimationBitmap, value: BitmapAnimationElement)
    {
        value.transition?.updatePercent(100f)
    }

    internal fun setAnimationFrame(frame: Float) =
        this.animate(frame)
}