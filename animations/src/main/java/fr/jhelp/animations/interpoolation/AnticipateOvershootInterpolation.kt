/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.animations.interpoolation

import fr.jhelp.utilities.EPSILON_FLOAT
import fr.jhelp.utilities.compare
import kotlin.math.max

/**
 * Interpolation with anticipate and overshoot effect
 *
 * Anticipate : Like if it take a run-up
 *
 * Overshoot : Goes to far and return back
 * @param tension Effect factor
 */
class AnticipateOvershootInterpolation(tension: Float = 1f) : Interpolation
{
    /**Effect factor*/
    private val tension = max(EPSILON_FLOAT, tension)

    /**
     * Interpolate value with anticipation and overshoot effect
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent: Float) =
        when
        {
            percent.compare(0.5f) < 0 ->
            {
                val value = 2f * percent
                0.5f * ((this.tension + 1f) * value * value * value - this.tension * value * value)
            }
            else                      ->
            {
                val value = 2f * percent - 2f
                0.5f * ((this.tension + 1f) * value * value * value + this.tension * value * value) + 1f
            }
        }
}