/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.engine.tools

import fr.jhelp.engine.scene.Node3D
import fr.jhelp.utilities.sign

object NodeOrderZ : Comparator<Node3D>
{
    override fun compare(node1: Node3D, node2: Node3D): Int =
        (node2.zOrder - node1.zOrder).sign()
}