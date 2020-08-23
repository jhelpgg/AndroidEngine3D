/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.animations.interpoolation

import kotlin.math.E
import kotlin.math.expm1

/**
 * Interpolation follow exponential progression
 */
object ExponentialInterpolation : Interpolation
{
    /**
     * Interpolate value with following equation :
     *
     *     t
     *    e - 1
     *    ------
     *    e - 1
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent: Float) =
        (expm1(percent.toDouble()) / (E - 1.0)).toFloat()
}