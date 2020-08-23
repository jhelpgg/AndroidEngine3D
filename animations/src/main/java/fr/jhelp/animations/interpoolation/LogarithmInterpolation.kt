/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.animations.interpoolation

import kotlin.math.ln
import kotlin.math.ln1p

/**
 * Interpolation follow logarithm progression
 */
object LogarithmInterpolation : Interpolation
{
    /**
     * Interpolate value with following equation:
     *
     *    ln(t + 1)
     *    --------
     *     ln(2)
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent: Float) =
        (ln1p(percent.toDouble()) / ln(2.0)).toFloat()
}
