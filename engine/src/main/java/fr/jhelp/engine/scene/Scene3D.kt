/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.engine.scene

import fr.jhelp.engine.OpenGLThread
import fr.jhelp.engine.animation.Animation
import fr.jhelp.engine.animation.AnimationFunction
import fr.jhelp.engine.tools.NodeOrderZ
import fr.jhelp.engine.tools.antiProjection
import fr.jhelp.lists.SortedArray
import fr.jhelp.tasks.parallel
import java.util.Stack
import javax.microedition.khronos.opengles.GL10

/**
 * Scene is the scene graph, any attached (directly or indirectly) object are drawn
 */
class Scene3D
{
    private val animations = ArrayList<Animation>()

    /**
     * Scene background color.
     *
     * The alpha part is ignored
     */
    var backgroundColor = WHITE

    /**
     * Main node of the scene
     */
    var root = Node3D()

    /**
     * Launch an animtation
     */
    fun play(animation: Animation)
    {
        parallel(animation)
        { anim ->
            anim.start()

            synchronized(this.animations)
            {
                this.animations.add(anim)
            }
        }
    }

    /**
     * Launch an animation based on task
     */
    fun play(animation: (Float) -> Boolean): Animation
    {
        val animationFunction = AnimationFunction(animation)
        this.play(animationFunction)
        return animationFunction
    }

    /**
     * Stop an animation
     */
    fun stop(animation: Animation)
    {
        parallel(animation)
        { anim ->
            synchronized(this.animations)
            {
                if (this.animations.remove(anim))
                {
                    anim.finished()
                }
            }
        }
    }

    /**
     * Draw the scene
     */
    @OpenGLThread
    internal fun render(gl: GL10)
    {
        gl.glClearColor(this.backgroundColor.red,
                        this.backgroundColor.green,
                        this.backgroundColor.blue,
                        1f)

        val nodes = SortedArray(NodeOrderZ)
        val stack = Stack<Node3D>()
        stack.push(this.root)
        var current: Node3D

        while (stack.isNotEmpty())
        {
            current = stack.pop()

            if (current.hasSomethingToDraw())
            {
                current.zOrder = antiProjection(current, current.center()).z
                nodes.add(current)
            }

            for (child in current)
            {
                stack.push(child)
            }
        }

        for (node in nodes)
        {
            var explore: Node3D? = node

            while (explore != null)
            {
                stack.push(explore)
                explore = explore.parent
            }

            gl.glPushMatrix()

            while (stack.isNotEmpty())
            {
                stack.pop().position.locate(gl)
            }

            node.render(gl)
            gl.glPopMatrix()
        }

        synchronized(this.animations)
        {
            for (index in this.animations.size - 1 downTo 0)
            {
                val animation = this.animations[index]

                if (!animation.animate())
                {
                    this.animations.removeAt(index)
                    parallel(animation) { anim -> anim.finished() }
                }
            }
        }

       // DeleteTexture.freeNext(gl)
    }

    operator fun Node3D.unaryPlus() = this@Scene3D.root.add(this)

    /**
     * Search node by it's ID
     */
    fun nodeById(id: Int): Node3D?
    {
        val stack = Stack<Node3D>()
        stack.push(this.root)
        var current: Node3D

        while (stack.isNotEmpty())
        {
            current = stack.pop()

            if (current.id == id)
            {
                return current
            }

            for (child in current)
            {
                stack.push(child)
            }
        }

        return null
    }

    /**
     * Search node by name. If two or more nodes have the requested name, it gives the first found
     */
    fun firstNodeByName(name: String): Node3D?
    {
        val stack = Stack<Node3D>()
        stack.push(this.root)
        var current: Node3D

        while (stack.isNotEmpty())
        {
            current = stack.pop()

            if (current.name == name)
            {
                return current
            }

            for (child in current)
            {
                stack.push(child)
            }
        }

        return null
    }

    /**
     * Get all nodes with given name
     */
    fun allNodesWithName(name: String): List<Node3D>
    {
        val list = ArrayList<Node3D>()
        val stack = Stack<Node3D>()
        stack.push(this.root)
        var current: Node3D

        while (stack.isNotEmpty())
        {
            current = stack.pop()

            if (current.name == name)
            {
                list.add(current)
            }

            for (child in current)
            {
                stack.push(child)
            }
        }

        return list
    }
}