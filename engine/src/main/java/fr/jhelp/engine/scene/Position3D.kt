/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.engine.scene

import fr.jhelp.engine.OpenGLThread
import fr.jhelp.utilities.angle.AngleFloat
import fr.jhelp.utilities.angle.AngleUnit
import javax.microedition.khronos.opengles.GL10

/**
 * Generic position in 3D, relative to it's parent
 */
data class Position3D(var x: Float = 0f, var y: Float = 0f, var z: Float = 0f,
                      var angleX: Float = 0f, var angleY: Float = 0f, var angleZ: Float = 0f,
                      var scaleX: Float = 1f, var scaleY: Float = 1f, var scaleZ: Float = 1f)
{
    constructor(position: Position3D) : this(position.x, position.y, position.z,
                                             position.angleX, position.angleY, position.angleZ,
                                             position.scaleX, position.scaleY, position.scaleZ)

    fun copy() = Position3D(this)

    fun position(x: Float, y: Float, z: Float)
    {
        this.x = x
        this.y = y
        this.z = z
    }

    fun translate(x: Float, y: Float, z: Float)
    {
        this.x += x
        this.y += y
        this.z += z
    }

    fun setScale(scaleX: Float, scaleY: Float, scaleZ: Float)
    {
        this.scaleX = scaleX
        this.scaleY = scaleY
        this.scaleZ = scaleZ
    }

    fun scale(scaleX: Float, scaleY: Float, scaleZ: Float)
    {
        this.scaleX *= scaleX
        this.scaleY *= scaleY
        this.scaleZ *= scaleZ
    }

    fun setScale(scale: Float)
    {
        this.scaleX = scale
        this.scaleY = scale
        this.scaleZ = scale
    }

    fun scale(scale: Float)
    {
        this.scaleX *= scale
        this.scaleY *= scale
        this.scaleZ *= scale
    }

    @OpenGLThread
    internal fun locate(gl: GL10)
    {
        gl.glTranslatef(this.x, this.y, this.z)
        gl.glRotatef(this.angleX, 1f, 0f, 0f)
        gl.glRotatef(this.angleY, 0f, 1f, 0f)
        gl.glRotatef(this.angleZ, 0f, 0f, 1f)
        gl.glScalef(this.scaleX, this.scaleY, this.scaleZ)
    }

    fun projection(point: Point3D): Point3D
    {
        var vector = Vector3D(point.x + this.x, point.y + this.y, point.z + this.z)
        vector = Rotation3D(AXIS_X, AngleFloat(this.angleX, AngleUnit.DEGREE)).rotateVector(vector)
        vector = Rotation3D(AXIS_Y, AngleFloat(this.angleY, AngleUnit.DEGREE)).rotateVector(vector)
        vector = Rotation3D(AXIS_Z, AngleFloat(this.angleZ, AngleUnit.DEGREE)).rotateVector(vector)
        return Point3D(vector.x, vector.y, vector.z)
    }

    fun antiProjection(point: Point3D): Point3D
    {
        var vector = Vector3D(point.x, point.y, point.z)
        vector = Rotation3D(AXIS_Z, AngleFloat(-this.angleZ, AngleUnit.DEGREE)).rotateVector(vector)
        vector = Rotation3D(AXIS_Y, AngleFloat(-this.angleY, AngleUnit.DEGREE)).rotateVector(vector)
        vector = Rotation3D(AXIS_X, AngleFloat(-this.angleX, AngleUnit.DEGREE)).rotateVector(vector)
        return Point3D(vector.x - this.x, vector.y - this.y, vector.z - this.z)
    }
}