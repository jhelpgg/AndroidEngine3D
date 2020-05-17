package fr.jhelp.engine.scene

abstract class NodeWithBoundingBox : Node3D()
{
    abstract fun boundingBox() : BoundingBox
}