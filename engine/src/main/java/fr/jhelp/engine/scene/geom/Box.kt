package fr.jhelp.engine.scene.geom

import fr.jhelp.engine.scene.Object3D

class Box(boxUV: BoxUV = BoxUV()) : Object3D()
{
    init
    {
        // Top
        this.addSquare(0.5f, 0.5f, 0.5f,
                       boxUV.top.maxU, boxUV.top.maxV,
                       0.5f, 0.5f, -0.5f,
                       boxUV.top.maxU, boxUV.top.minV,
                       -0.5f, 0.5f, -0.5f,
                       boxUV.top.minU, boxUV.top.minV,
                       -0.5f, 0.5f, 0.5f,
                       boxUV.top.minU, boxUV.top.maxV)

        // Face
        this.addSquare(-0.5f, 0.5f, 0.5f,
                       boxUV.face.minU, boxUV.face.minV,
                       -0.5f, -0.5f, 0.5f,
                       boxUV.face.minU, boxUV.face.maxV,
                       0.5f, -0.5f, 0.5f,
                       boxUV.face.maxU, boxUV.face.maxV,
                       0.5f, 0.5f, 0.5f,
                       boxUV.face.maxU, boxUV.face.minV)


        // Right
        this.addSquare(0.5f, 0.5f, 0.5f,
                       boxUV.right.minU, boxUV.right.minV,
                       0.5f, -0.5f, 0.5f,
                       boxUV.right.minU, boxUV.right.maxV,
                       0.5f, -0.5f, -0.5f,
                       boxUV.right.maxU, boxUV.right.maxV,
                       0.5f, 0.5f, -0.5f,
                       boxUV.right.maxU, boxUV.right.minV)

        // Left
        this.addSquare(-0.5f, 0.5f, -0.5f,
                       boxUV.left.minU, boxUV.left.minV,
                       -0.5f, -0.5f, -0.5f,
                       boxUV.left.minU, boxUV.left.maxV,
                       -0.5f, -0.5f, 0.5f,
                       boxUV.left.maxU, boxUV.left.maxV,
                       -0.5f, 0.5f, 0.5f,
                       boxUV.left.maxU, boxUV.left.minV)

        // Bottom
        this.addSquare(-0.5f, -0.5f, 0.5f,
                       boxUV.bottom.minU, boxUV.bottom.minV,
                       -0.5f, -0.5f, -0.5f,
                       boxUV.bottom.minU, boxUV.bottom.maxV,
                       0.5f, -0.5f, -0.5f,
                       boxUV.bottom.maxU, boxUV.bottom.maxV,
                       0.5f, -0.5f, 0.5f,
                       boxUV.bottom.maxU, boxUV.bottom.minV)

        // Back
        this.addSquare(0.5f, 0.5f, -0.5f,
                       boxUV.back.minU, boxUV.back.minV,
                       0.5f, -0.5f, -0.5f,
                       boxUV.back.minU, boxUV.back.maxV,
                       -0.5f, -0.5f, -0.5f,
                       boxUV.back.maxU, boxUV.back.maxV,
                       -0.5f, 0.5f, -0.5f,
                       boxUV.back.maxU, boxUV.back.minV)

        this.seal()
    }
}