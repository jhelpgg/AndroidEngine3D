/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.engine.animation.interpoolation

import fr.jhelp.utilities.EPSILON_FLOAT
import kotlin.math.max

/**
 * Interpolation with anticipation effect.
 *
 * Thai is to say it look goes reverse and then go to the good way, like if it take a run-up
 * @param tension Effect factor
 */
class AnticipateInterpolation(tension: Float = 1f) : Interpolation
{
    /**Effect factor*/
    private val tension = max(EPSILON_FLOAT, tension)

    /**
     * Interpolate value with anticipation effect
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent: Float) =
        (this.tension + 1f) * percent * percent * percent - this.tension * percent * percent
}