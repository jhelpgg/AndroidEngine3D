/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.animations.interpoolation

/**
 * Interpolation with hesitation effect
 */
object HesitateInterpolation : Interpolation
{
    /**
     * Interpolate value with hesitation effect
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent: Float): Float
    {
        val value = 2f * percent - 1f
        return 0.5f * (value * value * value + 1f)
    }
}