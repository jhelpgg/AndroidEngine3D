/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.engine.animation.interpoolation

import fr.jhelp.utilities.nul
import fr.jhelp.utilities.random
import fr.jhelp.utilities.same

/**
 * Interpolation with random progression
 */
object RandomInterpolation : Interpolation
{
    /**
     * Interpolate value with random progression
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent: Float) =
        when
        {
            percent.nul || percent.same(1f) -> percent
            else                            -> random(percent, 1f)
        }
}