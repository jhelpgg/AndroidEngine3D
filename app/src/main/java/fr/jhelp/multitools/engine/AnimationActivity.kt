/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.multitools.engine

import androidx.recyclerview.widget.RecyclerView
import fr.jhelp.animations.AnimationList
import fr.jhelp.animations.animationTask
import fr.jhelp.animations.interpoolation.AccelerationInterpolation
import fr.jhelp.animations.interpoolation.AnticipateInterpolation
import fr.jhelp.animations.interpoolation.AnticipateOvershootInterpolation
import fr.jhelp.animations.interpoolation.BounceInterpolation
import fr.jhelp.animations.interpoolation.BouncingInterpolation
import fr.jhelp.animations.interpoolation.CosinusInterpolation
import fr.jhelp.animations.interpoolation.CubicInterpolation
import fr.jhelp.animations.interpoolation.DecelerationInterpolation
import fr.jhelp.animations.interpoolation.ExponentialInterpolation
import fr.jhelp.animations.interpoolation.HesitateInterpolation
import fr.jhelp.animations.interpoolation.LinearInterpolation
import fr.jhelp.animations.interpoolation.LogarithmInterpolation
import fr.jhelp.animations.interpoolation.OvershootInterpolation
import fr.jhelp.animations.interpoolation.QuadraticInterpolation
import fr.jhelp.animations.interpoolation.RandomInterpolation
import fr.jhelp.animations.interpoolation.SinusInterpolation
import fr.jhelp.animations.interpoolation.SquareInterpolation
import fr.jhelp.animations.interpoolation.SquareRootInterpolation
import fr.jhelp.engine.animation.keyFrame.AnimationNode3D
import fr.jhelp.engine.resources.ResourcesAccess
import fr.jhelp.engine.scene.BLUE
import fr.jhelp.engine.scene.Clone3D
import fr.jhelp.engine.scene.GREEN
import fr.jhelp.engine.scene.Position3D
import fr.jhelp.engine.scene.Scene3D
import fr.jhelp.engine.scene.WHITE
import fr.jhelp.engine.scene.geom.Box
import fr.jhelp.engine.scene.geom.Plane
import fr.jhelp.engine.view.View3D
import fr.jhelp.multitools.R
import java.util.concurrent.atomic.AtomicBoolean

class AnimationActivity : Activity3D()
{
    private val playing = AtomicBoolean(false)
    private val box = Box()
    private lateinit var scene: Scene3D

    override fun listAdapter(): RecyclerView.Adapter<*> =
        TextElementAdapter(this::startAnimation,
                           InterpolationInformation(AccelerationInterpolation(2f)),
                           InterpolationInformation(AnticipateInterpolation(2f)),
                           InterpolationInformation(AnticipateOvershootInterpolation(2f)),
                           InterpolationInformation(BounceInterpolation),
                           InterpolationInformation(BouncingInterpolation(2)),
                           InterpolationInformation(CosinusInterpolation),
                           InterpolationInformation(CubicInterpolation(0.1f, 0.9f)),
                           InterpolationInformation(DecelerationInterpolation(2f)),
                           InterpolationInformation(ExponentialInterpolation),
                           InterpolationInformation(HesitateInterpolation),
                           InterpolationInformation(LinearInterpolation),
                           InterpolationInformation(LogarithmInterpolation),
                           InterpolationInformation(OvershootInterpolation(2f)),
                           InterpolationInformation(QuadraticInterpolation(0.25f)),
                           InterpolationInformation(RandomInterpolation),
                           InterpolationInformation(SinusInterpolation),
                           InterpolationInformation(SquareInterpolation),
                           InterpolationInformation(SquareRootInterpolation))

    override fun fill3D(view3D: View3D)
    {
        this.scene = view3D.scene3D
        this.scene.root.position.z = -3f

        val start = Plane()
        start.material.diffuse = BLUE
        start.position.angleX = 80f
        start.position.x = -1.5f
        start.position.y = -1.01f
        this.scene.root.add(start)

        val end = Clone3D(start)
        end.material.diffuse = GREEN
        end.position.angleX = 80f
        end.position.x = 1.5f
        end.position.y = -1.01f
        this.scene.root.add(end)

        this.box.material.diffuse = WHITE
        this.box.material.texture = ResourcesAccess.obtainTexture(R.drawable.floor)
        this.box.position.x = -1.5f
        this.scene.root.add(this.box)
    }

    private fun startAnimation(interpolationInformation: InterpolationInformation)
    {
        if (!this.playing.getAndSet(true))
        {
            val animationFrame = AnimationNode3D(this.box)
            animationFrame.frame(50, Position3D(x = 1.5f), interpolationInformation.interpolation)
            animationFrame.frame(100, Position3D(x = -1.5f), interpolationInformation.interpolation)
            val animationList = AnimationList()
            animationList.add(animationFrame)
            animationList.add(animationTask { this.playing.set(false) })
            this.scene.play(animationList)
        }
    }
}