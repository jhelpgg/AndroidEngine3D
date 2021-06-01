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
import fr.jhelp.models.basic.CylinderShape.obtainShape

/**
 * Creator of shape simple cylinder. Move point is the center
 *
 * Call [obtainShape] to generate a new instance of the shape
 */
object CylinderShape : ShapeProvider()
{
    override fun createReference(): Node3D
    {
        val cylinderPath = Path()
        cylinderPath.moveTo(0.5f, 0.5f)
        cylinderPath.lineTo(0.5f, -0.5f)
        val cylinder = Revolution(cylinderPath,
                                  pathPrecision = this.quality.pathPrecision,
                                  rotationPrecision = this.quality.rotationPrecision)
        cylinder.doubleFace = true
        return cylinder
    }
}