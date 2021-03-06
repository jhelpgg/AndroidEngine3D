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

/**
 * Interpolation that overshoot.
 *
 * That is to say it goes to far and then go back to the good place
 * @param tension Effect factor
 */
class OvershootInterpolation(tension: Float = 1f) : Interpolation
{
    /**Effect factor*/
    private val tension = max(EPSILON_FLOAT, tension)

    /**
     * Interpolate value with overshoot effect
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent: Float): Float
    {
        val value = percent - 1f
        return (this.tension + 1f) * value * value * value + this.tension * value * value + 1f
    }
}