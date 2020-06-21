/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.engine.animation.keyFrame

import fr.jhelp.engine.OpenGLThread
import fr.jhelp.engine.scene.Point3D
import fr.jhelp.engine.scene.Sound3D

/**
 * Animation move a 3D sound
 */
class AnimationSound3D(sound: Sound3D, fps: Int = 25) :
    AnimationKeyFrame<Sound3D, Point3D>(sound, fps)
{
    @OpenGLThread
    override fun interpolateValue(animated: Sound3D, before: Point3D, after: Point3D,
                                  percent: Float)
    {
        val anti = 1f - percent
        val x = before.x * anti + after.x * percent
        val y = before.y * anti + after.y * percent
        val z = before.z * anti + after.z * percent
        animated.position(x, y, z)
    }

    @OpenGLThread
    override fun obtainValue(animated: Sound3D) =
        Point3D(animated.x, animated.y, animated.z)

    @OpenGLThread
    override fun setValue(animated: Sound3D, value: Point3D)
    {
        animated.position(value.x, value.y, value.z)
    }
}