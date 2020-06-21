/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.engine.animation.interpoolation

import fr.jhelp.utilities.compare
import fr.jhelp.utilities.nul
import fr.jhelp.utilities.square
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

/**
 * Interpolation that bounce
 * @param numberBounce Number of bounce
 */
class BouncingInterpolation(numberBounce: Int = 2) : Interpolation
{
    /**Number of bounce*/
    private val numberBounce = max(0, numberBounce)

    /**
     * Interpolate value with bounce effect
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent: Float): Float
    {
        if (this.numberBounce == 0)
        {
            return square(percent)
        }

        var amplitude = 1f / (this.numberBounce + 1)

        if (percent.compare(amplitude) < 0)
        {
            return square(percent / amplitude)
        }

        var free = 1f - amplitude * 0.56789f
        var minimum = 0.56789f
        var percentReal = percent - amplitude
        var left = this.numberBounce - 1

        while (percentReal.compare(
                amplitude) >= 0 && !amplitude.nul && !minimum.nul && !percentReal.nul && left > 0)
        {
            minimum *= 0.56789f
            percentReal -= amplitude
            free -= amplitude
            amplitude = free * 0.56789f
            left--
        }

        if (left == 0)
        {
            amplitude = free / 2f
        }

        val squareRoot = sqrt(minimum.toDouble()).toFloat()
        percentReal = (percentReal - amplitude / 2f) * (squareRoot * 2f / amplitude)
        return min(square(percentReal) + 1 - minimum, 1f)
    }
}
