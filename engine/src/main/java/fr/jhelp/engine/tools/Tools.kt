/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.engine.tools

import fr.jhelp.engine.scene.BoundingBox
import fr.jhelp.engine.scene.Node3D
import fr.jhelp.engine.scene.NodeWithBoundingBox
import fr.jhelp.engine.scene.Point3D
import fr.jhelp.engine.scene.Position3D
import java.util.Stack

fun antiProjection(node: Node3D, point3D: Point3D): Point3D
{
    var projected = point3D
    var current: Node3D? = node

    while (current != null)
    {
        projected = current.position.antiProjection(projected)
        current = current.parent
    }

    return projected
}

fun projection(node: Node3D, point3D: Point3D): Point3D
{
    val stack = Stack<Node3D>()
    var current: Node3D? = node

    while (current != null)
    {
        stack.push(current)
        current = current.parent
    }

    var projected = point3D

    while (stack.isNotEmpty())
    {
        projected = stack.pop().position.projection(projected)
    }

    return projected
}

fun antiProjectionBoundingBox(node: NodeWithBoundingBox): BoundingBox
{
    var projectedPoint: Point3D
    var boundingBox = node.boundingBox()
    var projected = boundingBox
    var current: Node3D? = node
    var position: Position3D

    while (current != null)
    {
        projected = BoundingBox()
        position = current.position

        projectedPoint =
            position.antiProjection(Point3D(boundingBox.minX, boundingBox.minY, boundingBox.minZ))
        projected.add(projectedPoint.x, projectedPoint.y, projectedPoint.z)

        projectedPoint =
            position.antiProjection(Point3D(boundingBox.minX, boundingBox.minY, boundingBox.maxZ))
        projected.add(projectedPoint.x, projectedPoint.y, projectedPoint.z)

        projectedPoint =
            position.antiProjection(Point3D(boundingBox.minX, boundingBox.maxY, boundingBox.minZ))
        projected.add(projectedPoint.x, projectedPoint.y, projectedPoint.z)

        projectedPoint =
            position.antiProjection(Point3D(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ))
        projected.add(projectedPoint.x, projectedPoint.y, projectedPoint.z)

        projectedPoint =
            position.antiProjection(Point3D(boundingBox.maxX, boundingBox.minY, boundingBox.minZ))
        projected.add(projectedPoint.x, projectedPoint.y, projectedPoint.z)

        projectedPoint =
            position.antiProjection(Point3D(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ))
        projected.add(projectedPoint.x, projectedPoint.y, projectedPoint.z)

        projectedPoint =
            position.antiProjection(Point3D(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ))
        projected.add(projectedPoint.x, projectedPoint.y, projectedPoint.z)

        projectedPoint =
            position.antiProjection(Point3D(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ))
        projected.add(projectedPoint.x, projectedPoint.y, projectedPoint.z)

        boundingBox = projected
        current = current.parent
    }

    return projected
}

fun mayWillCollide(node1: NodeWithBoundingBox,
                   translateX1: Float, translateY1: Float, translateZ1: Float,
                   node2: NodeWithBoundingBox,
                   translateX2: Float, translateY2: Float, translateZ2: Float): Boolean
{
    val projectedBoundingBox1 = antiProjectionBoundingBox(node1)
    val projectedBoundingBox2 = antiProjectionBoundingBox(node2)
    return projectedBoundingBox1.willIntersects(translateX1, translateY1, translateZ1,
                                                projectedBoundingBox2,
                                                translateX2, translateY2, translateZ2)
}