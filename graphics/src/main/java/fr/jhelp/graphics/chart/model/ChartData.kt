/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.graphics.chart.model

import fr.jhelp.tasks.ThreadType

class ChartData internal constructor(animated: Boolean,
                                     private val updateListener: () -> Unit,
                                     private val updateThreadType: ThreadType = ThreadType.SHORT)
{
    var animated = animated
        private set
    private val elements = ArrayList<CharDataElement>()
    val size get() = synchronized(this.elements) { this.elements.size }

    internal fun animated(animated: Boolean)
    {
        if (animated != this.animated)
        {
            this.animated = animated

            if (!animated)
            {
                synchronized(this.elements) {
                    for (element in this.elements)
                    {
                        element.fixValue()
                    }
                }
            }
        }
    }

    fun addValue(value: Float)
    {
        synchronized(this.elements) {
            this.elements.add(CharDataElement(value, this.animated))
        }

        this.updateThreadType.parallel(this.updateListener)
    }

    operator fun get(index: Int): Float =
        synchronized(this.elements) { this.elements[index].value }

    operator fun set(index: Int, value: Float)
    {
        synchronized(this.elements) { this.elements[index].value(value, this.animated) }

        this.updateThreadType.parallel(this.updateListener)
    }
}