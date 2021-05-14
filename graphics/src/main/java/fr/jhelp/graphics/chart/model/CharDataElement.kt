/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.graphics.chart.model

import fr.jhelp.animations.interpoolation.SinusInterpolation
import fr.jhelp.animations.values.AnimatedFloat

internal class CharDataElement(private var targetValue: Float, animated: Boolean)
{
    private val animatedValue = AnimatedFloat()
    val value get() = this.animatedValue.value

    init
    {
        this.updateValue(animated)
    }

    private fun updateValue(animated: Boolean)
    {
        if (animated)
        {
            this.animatedValue.setValueIn(this.targetValue, 4096, SinusInterpolation)
            this.animatedValue.start()
        }
        else
        {
            this.animatedValue.setValue(this.targetValue)
        }
    }

    fun value(value: Float, animated: Boolean)
    {
        this.targetValue = value
        this.updateValue(animated)
    }

    fun fixValue()
    {
        this.updateValue(false)
    }
}