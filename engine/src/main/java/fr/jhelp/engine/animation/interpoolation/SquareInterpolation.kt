/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.engine.animation.interpoolation

/**
 * Interpolation follow square progression
 */
object SquareInterpolation : Interpolation
{
    /**
     * Interpolate value with following equation:
     *
     *    t²
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent: Float) = percent * percent
}
