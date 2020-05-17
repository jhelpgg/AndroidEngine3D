package fr.jhelp.engine.animation.interpoolation

object LinearInterpolation : Interpolation
{
    override fun invoke(percent: Float): Float = percent
}