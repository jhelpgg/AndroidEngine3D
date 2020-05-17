package fr.jhelp.engine.animation

class AnimationFunction(fps: Int, private val function: (Float) -> Boolean) : Animation(fps)
{
    constructor(function: (Float) -> Boolean) : this(25, function)

    override fun animate(frame: Float) = this.function(frame)
}