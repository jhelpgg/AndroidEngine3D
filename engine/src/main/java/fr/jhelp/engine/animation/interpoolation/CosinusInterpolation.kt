/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.engine.animation.interpoolation

import kotlin.math.PI
import kotlin.math.cos

/**
 * Interpolation follow cosinus function
 */
object CosinusInterpolation : Interpolation
{
    /**
     * Interpolate value with following equation :
     *
     *    1 + cos((t + 1) * PI)
     *    ---------------------
     *              2
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent: Float) =
        ((1.0 + cos((percent.toDouble() + 1.0) * PI)) / 2.0).toFloat()
}
