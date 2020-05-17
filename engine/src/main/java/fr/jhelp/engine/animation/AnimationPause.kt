package fr.jhelp.engine.animation

class AnimationPause(milliseconds: Int) : Animation(25)
{
    private val numberFrame = milliseconds / 40f

    override fun animate(frame: Float): Boolean =
        frame <= this.numberFrame
}