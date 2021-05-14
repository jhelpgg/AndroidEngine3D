/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.engine.animation.effect

import android.os.SystemClock
import fr.jhelp.engine.OpenGLThread
import javax.microedition.khronos.opengles.GL10

/**
 * Particle effect is composed of one or several [ParticleNode] that needs play on same particle effect animation
 *
 * Add [ParticleNode] with [addParticleNode] to create one effect
 *
 * To play the effect use [fr.jhelp.engine.scene.Scene3D.play] each time the effect needs to be played
 */
class ParticleEffect
{
    private val particleNodes = ArrayList<ParticleNode>()
    private val aliveParticles = ArrayList<Particle>()
    private var statTime: Long = 0L

    fun addParticleNode(particleNode: ParticleNode)
    {
        synchronized(this.particleNodes)
        {
            this.particleNodes.add(particleNode)
        }
    }

    internal fun start()
    {
        synchronized(this.aliveParticles)
        {
            this.aliveParticles.clear()
        }

        synchronized(this.particleNodes)
        {
            for (particleNode in particleNodes)
            {
                particleNode.resetEmission()
            }
        }

        this.statTime = SystemClock.uptimeMillis()
        this.update()
    }

    internal fun stop()
    {
        synchronized(this.aliveParticles)
        {
            this.aliveParticles.clear()
        }
    }

    internal fun update(): Boolean
    {
        var alive = false
        val frame = ((SystemClock.uptimeMillis() - this.statTime) * 25f) / 1000f
        val collector: (Particle) -> Unit = { particle -> this.aliveParticles.add(particle) }

        synchronized(this.particleNodes)
        {
            synchronized(this.particleNodes)
            {
                for (particleNode in this.particleNodes)
                {
                    if (particleNode.emitParticle(frame, collector))
                    {
                        alive = true
                    }
                }
            }

            var particle: Particle

            for (index in this.aliveParticles.size - 1 downTo 0)
            {
                particle = this.aliveParticles[index]

                if (particle.update(frame))
                {
                    alive = true
                }
                else
                {
                    this.aliveParticles.removeAt(index)
                }
            }
        }

        return alive
    }


    @OpenGLThread
    internal fun render(gl: GL10)
    {
        synchronized(this.aliveParticles)
        {
            for (particle in this.aliveParticles)
            {
                particle.draw(gl)
            }
        }
    }
}