# Robot

[Robot](../../../main/java/fr/jhelp/engine/scene/geom/robot/Robot.kt) is a character can be animate or fix on position by change angles on its joins.
It is also possible to change arms or legs color. And the texture applied in body.

To change the face, get its [Head](../../../main/java/fr/jhelp/engine/scene/geom/robot/Head.kt).
Change hair color, eyes or mouth. To see last changes, have to call `refresh` method.

[RobotPosition](../../../main/java/fr/jhelp/engine/scene/geom/robot/RobotPosition.kt) represents angles for each joints.
To get actual values use `robotPosition` without parameter.
To set to actual position use `robotPosition` with parameter.
This position can be used to put robot in specific position at start, or memorize some positions.
It can be also used for animation 

To animate the robot, can use [RobotAnimation](../../../main/java/fr/jhelp/engine/scene/geom/robot/RobotAnimation.kt).
It places the robot at specific position at a frame and interpolate between defined frames. More about [animations](../../animations/Animations.md) 

Some prebuilt animations exists via extensions to `Robot`class:
* For run : [Run animation](../../../main/java/fr/jhelp/engine/scene/geom/robot/animation/RunAnimation.kt)
* For walk : [Walk animation](../../../main/java/fr/jhelp/engine/scene/geom/robot/animation/WalkAnimation.kt)
* For return to default position : [Stand animation](../../../main/java/fr/jhelp/engine/scene/geom/robot/animation/StandAnimation.kt)
* For express yes or no : [Head animation](../../../main/java/fr/jhelp/engine/scene/geom/robot/animation/HeadAnimations.kt)
