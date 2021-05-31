/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.engine.animation.keyFrame

import androidx.annotation.DrawableRes
import fr.jhelp.animations.Animation
import fr.jhelp.animations.interpoolation.Interpolation
import fr.jhelp.animations.interpoolation.LinearInterpolation
import fr.jhelp.engine.resources.ResourcesAccess
import fr.jhelp.engine.scene.Texture
import fr.jhelp.engine.scene.texture
import fr.jhelp.images.transition.animation.AnimationBitmap
import fr.jhelp.images.transition.animation.AnimationBitmapTransition

/**
 * Animated texture that do transitions between images.
 * Can decide how many frames it takes for transition and interpolation type to use
 */
class AnimationTexture(@DrawableRes firstImage: Int,
                       val size: AnimationTextureSize = AnimationTextureSize.MEDIUM) :
    Animation(25)
{
    private val animationBitmap: AnimationBitmap =
        AnimationBitmap(ResourcesAccess.obtainBitmap(firstImage, this.size.size, this.size.size),
                        this.size.size, this.size.size, 25)
    /**Animated texture to apply to object*/
    val texture: Texture = texture(this.animationBitmap.resultBitmap, false)

    /**
     * Add transition image
     * @param numberFrame Number frames to go to the given image
     * @param image Target image
     * @param transition Transition type to use
     * @param interpolation Interpolation to use
     */
    fun appendTransitionIn(numberFrame: Int, @DrawableRes image: Int,
                           transition: AnimationBitmapTransition = AnimationBitmapTransition.MELT,
                           interpolation: Interpolation = LinearInterpolation)
    {
        this.animationBitmap.appendTransitionIn(numberFrame,
                                                ResourcesAccess.obtainBitmap(image,
                                                                             this.size.size,
                                                                             this.size.size),
                                                transition, interpolation)
    }

    override fun animate(frame: Float): Boolean
    {
        val stillAlive = this.animationBitmap.setAnimateFrame(frame)
        this.texture.refresh()
        return stillAlive
    }
}