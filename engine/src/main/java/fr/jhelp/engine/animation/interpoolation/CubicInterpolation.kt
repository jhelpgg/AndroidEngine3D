package fr.jhelp.engine.animation.interpoolation

import fr.jhelp.utilities.cubic

/**
 * Cubic interpolation
 * @param firstControl First control point
 * @param secondControl Second control point
 */
class CubicInterpolation(private val firstControl: Float = 0.1f,
                         private val secondControl: Float = 0.9f) : Interpolation
{
    /**
     * Compute cubic interpolation
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent: Float) =
        cubic(0.0, this.firstControl.toDouble(), this.secondControl.toDouble(), 1.0, percent.toDouble()).toFloat()
}