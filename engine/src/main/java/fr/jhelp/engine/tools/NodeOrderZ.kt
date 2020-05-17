package fr.jhelp.engine.tools

import fr.jhelp.engine.scene.Node3D
import fr.jhelp.utilities.sign

object NodeOrderZ : Comparator<Node3D>
{
    override fun compare(node1: Node3D, node2: Node3D): Int =
        (node2.zOrder - node1.zOrder).sign()
}