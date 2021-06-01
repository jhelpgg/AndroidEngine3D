/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.models.objects

import fr.jhelp.engine.scene.Material
import fr.jhelp.models.basic.BottleShape

/**
 * Hat model
 */
class Hat
{
    companion object
    {
        private val hatShape = BottleShape(true, 0.99f, 0.5f, 1.5f, true)
    }

    /**
     * Hat node to place in scene
     */
    var node = Hat.hatShape.obtainShape()
        private set

    /**
     * Hat quality
     */
    var quality = Hat.hatShape.quality
        set(value)
        {
            if (value != this.quality)
            {
                Hat.hatShape.quality = value
                val parent = this.node.parent
                parent?.remove(this.node)
                this.node = Hat.hatShape.obtainShape()
                parent?.add(this.node)
            }

            field = value
        }

    /**
     * Material used on hat
     */
    var material = Material()
        set(value)
        {
            this.node.applyMaterialHierarchically(value)
            field = value
        }

    init
    {
        this.node.applyMaterialHierarchically(this.material)
    }
}