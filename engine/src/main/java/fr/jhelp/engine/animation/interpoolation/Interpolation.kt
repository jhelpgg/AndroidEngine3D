/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.engine.animation.interpoolation

interface Interpolation
{
    /**
     * Interpolate a [0, 1] value
     *
     * The function **f(x)** MUST meet :
     *
     * * f(0) = 0
     * * f(1) = 1
     *
     * @param percent Value (in [0, 1]) to interpolate
     * @return Interpolation result (in [0, 1])
     */
    operator fun invoke(percent: Float): Float
}