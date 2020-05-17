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