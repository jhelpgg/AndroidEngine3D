/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.animations.interpoolation

import kotlin.math.PI
import kotlin.math.sin

/**
 * Interpolation follow sinus function
 */
object SinusInterpolation : Interpolation
{
    /**
     * Interpolate value with following equation :
     *
     *    1 + sin(t * PI - PI/2)
     *    ----------------------
     *              2
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent: Float) =
        ((1.0 + sin(percent.toDouble() * PI - PI / 2.0)) / 2.0).toFloat()
}