package fr.jhelp.engine.scene

import javax.microedition.khronos.opengles.GL10

class Clone3D(private val object3D: Object3D) : NodeWithBoundingBox()
{
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

    override fun render(gl: GL10)
    {
        this.material.render(gl)
        this.object3D.draw(gl)
    }

    override fun toString(): String =
        "${super.toString()} => ${this.object3D}"
}