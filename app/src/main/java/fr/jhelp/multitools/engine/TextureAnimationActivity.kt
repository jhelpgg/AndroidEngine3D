/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.multitools.engine

import androidx.recyclerview.widget.RecyclerView
import fr.jhelp.animations.Animation
import fr.jhelp.animations.AnimationList
import fr.jhelp.animations.animationTask
import fr.jhelp.engine.animation.keyFrame.AnimationTexture
import fr.jhelp.engine.animation.keyFrame.AnimationTextureSize
import fr.jhelp.engine.resources.ResourcesAccess
import fr.jhelp.engine.scene.Scene3D
import fr.jhelp.engine.scene.Texture
import fr.jhelp.engine.scene.WHITE
import fr.jhelp.engine.scene.geom.Plane
import fr.jhelp.engine.view.View3D
import fr.jhelp.images.transition.animation.AnimationBitmapTransition
import fr.jhelp.multitools.R
import java.util.concurrent.atomic.AtomicBoolean

class TextureAnimationActivity : Activity3D()
{
    private val playing = AtomicBoolean(false)
    private val plane = Plane()
    private lateinit var scene: Scene3D
    private val animations = HashMap<AnimationBitmapTransition, Pair<Animation, Texture>>()

    override fun listAdapter(): RecyclerView.Adapter<*> =
        TextElementAdapter(this::startAnimation,
                           *AnimationBitmapTransition.values())

    override fun fill3D(view3D: View3D)
    {
        this.scene = view3D.scene3D
        this.scene.root.position.z = -2f

        this.plane.position.scale(2f)
        this.plane.material.diffuse = WHITE
        this.plane.material.texture = ResourcesAccess.obtainTexture(R.drawable.floor)

        this.scene.root.add(this.plane)
    }

    private fun startAnimation(bitmapTransition: AnimationBitmapTransition)
    {
        if (!this.playing.getAndSet(true))
        {
            val (animation, texture) =
                this.animations.getOrPut(bitmapTransition,
                                         { this.createAnimation(bitmapTransition) })
            this.plane.material.texture = texture
            this.scene.play(animation)
        }
    }

    private fun createAnimation(
        bitmapTransition: AnimationBitmapTransition): Pair<Animation, Texture>
    {
        val animationTexture = AnimationTexture(R.drawable.floor, AnimationTextureSize.SMALL)
        animationTexture.appendTransitionIn(100, R.drawable.emerald_bk, bitmapTransition)
        val animationList = AnimationList()
        animationList.add(animationTexture)
        animationList.add(animationTask { this.playing.set(false) })
        return Pair(animationList, animationTexture.texture)
    }
}