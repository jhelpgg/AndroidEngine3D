/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.engine.scene

import fr.jhelp.engine.OpenGLThread
import javax.microedition.khronos.opengles.GL10

/**
 * Clone of an [Object3D], it will have the exact same shape. [Material] and [Position3D] can be different
 *
 * The aim is to save memory, only original object stores the mesh, clones just use it
 */
class Clone3D(private val object3D: Object3D) : NodeWithBoundingBox()
{
    /**Clone material*/
    var material = Material()

    override fun boundingBox() = this.object3D.boundingBox()

    override fun hasSomethingToDraw() = this.object3D.numberTriangle > 0

    override fun center() = this.object3D.center()

    override fun internalCopy(): Node3D
    {
        val copy = Clone3D(this.object3D)
        copy.material = this.material
        return copy
    }

    override fun material(material: Material)
    {
        this.material = material
    }

    @OpenGLThread
    override fun render(gl: GL10)
    {
        this.material.render(gl)
        this.object3D.draw(gl)
    }

    override fun toString(): String =
        "${super.toString()} => ${this.object3D}"
}