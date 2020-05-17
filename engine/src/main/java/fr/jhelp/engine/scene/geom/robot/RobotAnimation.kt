package fr.jhelp.engine.scene.geom.robot

import fr.jhelp.engine.animation.keyFrame.AnimationKeyFrame

class RobotAnimation(robot: Robot, fps: Int = 25) :
    AnimationKeyFrame<Robot, RobotPosition>(robot, fps)
{
    override fun interpolateValue(animated: Robot, before: RobotPosition, after: RobotPosition,
                                  percent: Float)
    {
        val anti = 1f - percent
        val robotPosition = RobotPosition(before.neckAngleX * anti + after.neckAngleX * percent,
                                          before.neckAngleY * anti + after.neckAngleY * percent,
                                          before.neckAngleZ * anti + after.neckAngleZ * percent,
                                          before.rightShoulderAngleX * anti + after.rightShoulderAngleX * percent,
                                          before.rightShoulderAngleZ * anti + after.rightShoulderAngleZ * percent,
                                          before.rightElbowAngleX * anti + after.rightElbowAngleX * percent,
                                          before.leftShoulderAngleX * anti + after.leftShoulderAngleX * percent,
                                          before.leftShoulderAngleZ * anti + after.leftShoulderAngleZ * percent,
                                          before.leftElbowAngleX * anti + after.leftElbowAngleX * percent,
                                          before.rightAssAngleX * anti + after.rightAssAngleX * percent,
                                          before.rightAssAngleZ * anti + after.rightAssAngleZ * percent,
                                          before.rightKneeAngleX * anti + after.rightKneeAngleX * percent,
                                          before.leftAssAngleX * anti + after.leftAssAngleX * percent,
                                          before.leftAssAngleZ * anti + after.leftAssAngleZ * percent,
                                          before.leftKneeAngleX * anti + after.leftKneeAngleX * percent)
        animated.robotPosition(robotPosition)
    }

    override fun obtainValue(animated: Robot): RobotPosition = animated.robotPosition()

    override fun setValue(animated: Robot, value: RobotPosition)
    {
        animated.robotPosition(value)
    }
}