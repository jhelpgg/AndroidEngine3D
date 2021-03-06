/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.animations.interpoolation

import fr.jhelp.utilities.EPSILON_FLOAT
import kotlin.math.max
import kotlin.math.pow

/**
 * Interpolation with deceleration effect
 * @param factor Deceleration factor
 */
class DecelerationInterpolation(factor: Float = 1f) : Interpolation
{
    /**Deceleration factor*/
    private val factor = 2.0 * max(EPSILON_FLOAT.toDouble(), factor.toDouble())

    /**
     * Interpolate value with deceleration effect
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent: Float) =
        (1.0 - (1.0 - percent).pow(this.factor)).toFloat()
}
