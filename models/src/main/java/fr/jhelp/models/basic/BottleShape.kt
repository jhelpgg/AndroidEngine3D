/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.models.basic

import fr.jhelp.engine.scene.Node3D
import fr.jhelp.engine.scene.geom.Revolution
import fr.jhelp.images.path.Path
import fr.jhelp.utilities.bounds

/**
 * Creator of shape look like a bottle
 *
 * Call [obtainShape] to generate a new instance of the shape
 */
class BottleShape(private val closeTop: Boolean,
                  neckPortion: Float, neckWidth: Float,
                  bodyWidth: Float,
                  private val closeBottom: Boolean) : ShapeProvider()
{
    private val neckPortion = neckPortion.bounds(0.01f, 0.99f)
    private val neckWidth = neckWidth.bounds(0.1f, 1f)
    private val bodyWidth = bodyWidth.bounds(this.neckWidth + 0.1f, 2f)

    override fun createReference(): Node3D
    {
        val bottlePath = Path()

        if (this.closeTop)
        {
            bottlePath.moveTo(0f, 0.5f)
            bottlePath.lineTo(this.neckWidth, 0.5f)
        }
        else
        {
            bottlePath.moveTo(this.neckWidth, 0.5f)
        }

        val y = 0.5f - this.neckPortion
        bottlePath.lineTo(this.neckWidth, y)
        val controlY = (y - 0.5f) / 2f
        bottlePath.quadraticTo(this.bodyWidth, controlY, this.neckWidth, -0.5f)

        if (this.closeBottom)
        {
            bottlePath.lineTo(0f, -0.5f)
        }

        val bottle = Revolution(bottlePath,
                                pathPrecision = this.quality.pathPrecision,
                                rotationPrecision = this.quality.rotationPrecision)
        bottle.doubleFace = !this.closeTop || !this.closeBottom
        return bottle
    }
}