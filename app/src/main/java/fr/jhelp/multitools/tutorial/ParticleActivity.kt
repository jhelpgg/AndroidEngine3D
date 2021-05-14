/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.multitools.tutorial

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.jhelp.animations.interpoolation.AccelerationInterpolation
import fr.jhelp.animations.interpoolation.DecelerationInterpolation
import fr.jhelp.engine.animation.effect.ParticleEffect
import fr.jhelp.engine.animation.effect.ParticleNode
import fr.jhelp.engine.resources.ResourcesAccess
import fr.jhelp.engine.scene
import fr.jhelp.engine.scene.Color3D
import fr.jhelp.engine.scene.LIGHT_GREEN
import fr.jhelp.engine.scene.LIGHT_GREY
import fr.jhelp.engine.scene.Scene3D
import fr.jhelp.engine.scene.WHITE
import fr.jhelp.engine.scene.geom.Box
import fr.jhelp.engine.view.View3D
import fr.jhelp.images.COLOR_LIGHT_GREEN_0300
import fr.jhelp.multitools.R

class ParticleActivity : Activity()
{
    private lateinit var scene: Scene3D
    private val swordParticleEffect: ParticleEffect by lazy { this.swordEffect() }
    private val explodeParticleEffect: ParticleEffect by lazy { this.explodeEffect() }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_particle)

        val controls = this.findViewById<RecyclerView>(R.id.controls)
        controls.layoutManager = LinearLayoutManager(this)
        controls.adapter =
            ParticleControlsAdapter(
                Pair(R.string.particleSword, { this.scene.play(this.swordParticleEffect) }),
                Pair(R.string.particleExplode, { this.scene.play(this.explodeParticleEffect) }))

        // Get the 3D view and draw it in independent thread to free the UI thread
        val view3D = this.findViewById<View3D>(R.id.view3D)
        view3D.scene(this::drawScene)
    }

    /**
     * Draw scene on 3D view
     */
    private fun drawScene(scene3D: Scene3D)
    {
        this.scene = scene3D
        val box = Box()
        box.material.diffuse = LIGHT_GREY
        box.material.texture = ResourcesAccess.obtainTexture(R.drawable.floor)
        scene3D.root.add(box)
        scene3D.root.position.z = -2f
    }

    private fun swordEffect(): ParticleEffect
    {
        val colorStart = LIGHT_GREEN
        val colorEnd = Color3D(COLOR_LIGHT_GREEN_0300)
        val particleTexture = ResourcesAccess.obtainTexture(R.drawable.particle_clear_hard)
        val particleEffect = ParticleEffect()

        val particleNodeSwordLine = ParticleNode(1, 20f, 0f, 0f)
        particleNodeSwordLine.setPosition(0f, 0f, -2f)
        particleNodeSwordLine.setAngle(-45f)
        particleNodeSwordLine.setScale(0.05f, 0.2f, 0.05f)
        particleNodeSwordLine.setAccelerationScale(0f, 0.012f, 0f)
        particleNodeSwordLine.setDiffuseColor(colorStart, colorEnd)
        particleNodeSwordLine.texture = particleTexture
        particleEffect.addParticleNode(particleNodeSwordLine)

        val particleNodeStarLine = ParticleNode(10, 20f, 0f, 0f)
        particleNodeStarLine.setPosition(0f, 0f, -2f)
        particleNodeStarLine.setAngle(0f, 180f)
        particleNodeStarLine.setScale(0.04f)
        particleNodeStarLine.setAccelerationScale(0f, 0.007f, 0f)
        particleNodeStarLine.setDiffuseColor(colorStart, colorEnd)
        particleNodeStarLine.diffuseInterpolation = DecelerationInterpolation(1.25f)
        particleNodeStarLine.texture = particleTexture
        particleEffect.addParticleNode(particleNodeStarLine)

        val particleNodeCircleEffect = ParticleNode(1, 20f, 0f, 0f)
        particleNodeCircleEffect.setPosition(0f, 0f, -2f)
        particleNodeCircleEffect.setDiffuseColor(colorStart, colorEnd)
        particleNodeCircleEffect.diffuseInterpolation = AccelerationInterpolation(1.25f)
        particleNodeCircleEffect.setAlpha(0.5f)
        particleNodeCircleEffect.setAngle(-45f)
        particleNodeCircleEffect.setScale(0.02f)
        particleNodeCircleEffect.setAccelerationScale(0.005f, 0.006f, 0.05f)
        particleNodeCircleEffect.texture = particleTexture
        particleEffect.addParticleNode(particleNodeCircleEffect)

        return particleEffect
    }

    private fun explodeEffect(): ParticleEffect
    {
        val particleFireTexture = ResourcesAccess.obtainTexture(R.drawable.fire_0)
        val particleSmokeTexture = ResourcesAccess.obtainTexture(R.drawable.smoke)
        val particleEffect = ParticleEffect()

        val particleNodeFire = ParticleNode(1, 20f, 0f, 0f)
        particleNodeFire.setPosition(0f, 0f, -2f)
        particleNodeFire.texture = particleFireTexture
        particleNodeFire.setDiffuseColor(WHITE)
        particleNodeFire.setScale(0.02f)
        particleNodeFire.setAccelerationScale(0.007f)
        particleEffect.addParticleNode(particleNodeFire)

        val particleNodeFireDisappear = ParticleNode(1, 20f, 19f, 19f)
        particleNodeFireDisappear.setPosition(0f, 0f, -2f)
        particleNodeFireDisappear.texture = particleFireTexture
        particleNodeFireDisappear.setDiffuseColor(WHITE)
        particleNodeFireDisappear.setScale(2f)
        particleNodeFireDisappear.setAlpha(1f, 0f)
        particleNodeFireDisappear.alphaInterpolation = DecelerationInterpolation(2.0f)
        particleEffect.addParticleNode(particleNodeFireDisappear)

        val particleSmoke = ParticleNode(3, 40f, 20f, 20f)
        particleSmoke.setPosition(0f, -0.1f, -2f)
        particleSmoke.setSpeedDirection(-0.01f, 0.04f, 0f,
                                        0.01f, 0.04f, 0f)
        particleSmoke.texture = particleSmokeTexture
        particleSmoke.setDiffuseColor(WHITE)
        particleSmoke.setScale(1f)
        particleSmoke.setAlpha(1f, 0f)
        particleSmoke.alphaInterpolation = AccelerationInterpolation(2.0f)
        particleEffect.addParticleNode(particleSmoke)

        return particleEffect
    }
}