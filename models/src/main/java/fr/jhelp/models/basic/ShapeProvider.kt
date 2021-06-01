/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.models.basic

import fr.jhelp.engine.scene.Node3D
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Creator of shape on optimizing by cloning same shapes
 *
 * Call [obtainShape] to generate a new instance of the shape
 */
abstract class ShapeProvider
{
    /**
     * Shape quality.
     * Change the value will take in count next time [obtainShape] is called
     */
    var quality = ShapeQuality.NORMAL
        set(value)
        {
            if (value != this.quality)
            {
                this.needToCreateReference.set(true)
            }

            field = value
        }

    private val needToCreateReference = AtomicBoolean(true)
    private lateinit var reference: Node3D

    /**
     * New shape instance
     */
    fun obtainShape(): Node3D =
        synchronized(this.needToCreateReference)
        {
            if (this.needToCreateReference.getAndSet(false))
            {
                this.reference = this.createReference()
                this.reference
            }
            else
            {
                this.reference.copy()
            }
        }

    protected abstract fun createReference(): Node3D
}