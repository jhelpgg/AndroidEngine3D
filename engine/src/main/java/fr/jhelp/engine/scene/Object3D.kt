package fr.jhelp.engine.scene

import fr.jhelp.engine.tools.BufferFloat
import javax.microedition.khronos.opengles.GL10

open class Object3D : NodeWithBoundingBox()
{
    private val points = BufferFloat()
    private val uvs = BufferFloat()
    private val boundingBox = BoundingBox()
    var numberTriangle = 0
        private set
    var material = Material()
    var doubleFace = false

    val sealed get() = this.points.sealed
    final override   fun center() = this.boundingBox.center()

    final override fun boundingBox() = this.boundingBox.copy()

    final override fun hasSomethingToDraw() = this.numberTriangle > 0

    final override fun material(material: Material)
    {
        this.material = material
    }

    final override fun internalCopy(): Node3D
    {
        val copy = Clone3D(this)
        copy.material = this.material
        return copy
    }

    fun seal()
    {
        this.points.seal()
        this.uvs.seal()
    }

    fun addTriangle(x1: Float, y1: Float, z1: Float, u1: Float, v1: Float,
                    x2: Float, y2: Float, z2: Float, u2: Float, v2: Float,
                    x3: Float, y3: Float, z3: Float, u3: Float, v3: Float)
    {
        if (this.sealed)
        {
            return
        }

        this.boundingBox.add(x1, y1, z1)
        this.boundingBox.add(x2, y3, z2)
        this.boundingBox.add(x3, y3, z3)

        this.points.add(x1, y1, z1,
                        x2, y2, z2,
                        x3, y3, z3)
        this.uvs.add(u1, v1,
                     u2, v2,
                     u3, v3)
        this.numberTriangle++
    }

    fun addSquare(topLeftX: Float, topLeftY: Float, topLeftZ: Float,
                  topLeftU: Float, topLeftV: Float,
                  bottomLeftX: Float, bottomLeftY: Float, bottomLeftZ: Float,
                  bottomLeftU: Float, bottomLeftV: Float,
                  bottomRightX: Float, bottomRightY: Float, bottomRightZ: Float,
                  bottomRightU: Float, bottomRightV: Float,
                  topRightX: Float, topRightY: Float, topRightZ: Float,
                  topRightU: Float, topRightV: Float)
    {
        if (this.sealed)
        {
            return
        }

        this.boundingBox.add(topLeftX, topLeftY, topLeftZ)
        this.boundingBox.add(bottomLeftX, bottomLeftY, bottomLeftZ)
        this.boundingBox.add(bottomRightX, bottomRightY, bottomRightZ)
        this.boundingBox.add(topRightX, topRightY, topRightZ)

        this.points.add(topLeftX, topLeftY, topLeftZ,
                        bottomLeftX, bottomLeftY, bottomLeftZ,
                        bottomRightX, bottomRightY, bottomRightZ,
            //
                        topLeftX, topLeftY, topLeftZ,
                        bottomRightX, bottomRightY, bottomRightZ,
                        topRightX, topRightY, topRightZ)
        this.uvs.add(topLeftU, topLeftV,
                     bottomLeftU, bottomLeftV,
                     bottomRightU, bottomRightV,
            //
                     topLeftU, topLeftV,
                     bottomRightU, bottomRightV,
                     topRightU, topRightV)
        this.numberTriangle += 2
    }

    final override fun render(gl: GL10)
    {
        this.material.render(gl)
        this.draw(gl)
    }

    internal fun draw(gl: GL10)
    {
        if (this.doubleFace)
        {
            gl.glDisable(GL10.GL_CULL_FACE)
        }
        else
        {
            gl.glEnable(GL10.GL_CULL_FACE)
        }

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, this.points.buffer())
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, this.uvs.buffer())

        for (offset in 0 until this.numberTriangle * 3 step 3)
        {
            gl.glDrawArrays(GL10.GL_TRIANGLES, offset, 3)
        }
    }
}