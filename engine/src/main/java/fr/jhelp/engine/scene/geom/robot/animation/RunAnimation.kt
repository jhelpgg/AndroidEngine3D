/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.engine.scene.geom.robot.animation

import fr.jhelp.animations.Animation
import fr.jhelp.animations.interpoolation.DecelerationInterpolation
import fr.jhelp.engine.scene.geom.robot.Robot
import fr.jhelp.engine.scene.geom.robot.RobotAnimation
import fr.jhelp.engine.scene.geom.robot.RobotPosition
import kotlin.math.max

/**
 * Create animation that make robot to run
 * @param numberFramePerStep Number frame to make a step
 * @param numberStep Number step to make
 * @return Created animation
 */
fun Robot.run(numberFramePerStep: Int = 1, numberStep: Int = 1): Animation
{
    val frame = max(1, numberFramePerStep);
    val semiFrame = frame shr 1
    val portion = 5
    val part = frame / portion
    val left = frame - part
    val angle = (36 * portion) / (portion + 1)
    val stepMax = max(1, numberStep)
    val animation = RobotAnimation(this)
    val robotPosition1 =
        RobotPosition(rightShoulderAngleX = 180f - angle, leftShoulderAngleX = 180f + angle,
                      rightElbowAngleX = -90f, leftElbowAngleX = -90f,
                      rightAssAngleX = 180f + angle, leftAssAngleX = 90f,
                      rightKneeAngleX = 0f, leftKneeAngleX = 90f)
    val robotPosition2 = RobotPosition(rightShoulderAngleX = 144f, leftShoulderAngleX = 216f,
                                       rightElbowAngleX = -90f, leftElbowAngleX = -90f,
                                       rightAssAngleX = 216f, leftAssAngleX = 144f)
    val robotPosition3 =
        RobotPosition(rightShoulderAngleX = 180f + angle, leftShoulderAngleX = 180f - angle,
                      rightElbowAngleX = -90f, leftElbowAngleX = -90f,
                      rightAssAngleX = 90f, leftAssAngleX = 180f + angle,
                      rightKneeAngleX = 90f, leftKneeAngleX = 0f)
    val robotPosition4 = RobotPosition(rightShoulderAngleX = 216f, leftShoulderAngleX = 144f,
                                       rightElbowAngleX = -90f, leftElbowAngleX = -90f,
                                       rightAssAngleX = 144f, leftAssAngleX = 216f)
    var key = semiFrame
    animation.frame(key, robotPosition1)
    var step = 1

    while (step < stepMax)
    {
        key += left
        animation.frame(key, if ((step and 1) == 0) robotPosition1 else robotPosition3)
        key += part
        animation.frame(key, if ((step and 1) == 0) robotPosition2 else robotPosition4)
        step++
    }

    key += frame
    animation.frame(key, RobotPosition(), DecelerationInterpolation(2f))
    return animation
}
