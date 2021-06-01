/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.models.objects

import fr.jhelp.animations.Animation
import fr.jhelp.animations.AnimationParallel
import fr.jhelp.engine.animation.keyFrame.AnimationNode3D
import fr.jhelp.engine.position
import fr.jhelp.engine.scene.Node3D
import fr.jhelp.engine.scene.Position3D
import fr.jhelp.models.basic.CylinderShape
import fr.jhelp.models.basic.EggShape
import fr.jhelp.models.basic.SausageNotCenterShape
import fr.jhelp.models.basic.SausageShape
import kotlin.math.max

/**
 * "Horse" model
 */
class Horse
{
    private val head = EggShape.obtainShape()
    private val neck = CylinderShape.obtainShape()
    private val body = SausageShape.obtainShape()
    private val legFrontLeft = SausageNotCenterShape.obtainShape()
    private val legFrontRight = SausageNotCenterShape.obtainShape()
    private val legBackLeft = SausageNotCenterShape.obtainShape()
    private val legBackRight = SausageNotCenterShape.obtainShape()

    /**
     * Horse node to place in scene
     */
    val node = Node3D()

    init
    {
        this.head.position {
            this.y = 0.5f
            this.z = -0.25f

            this.angleX = -90f

            this.scaleX = 2f
            this.scaleY = 3f
        }

        this.neck.position {
            this.y = 0.6f
            this.z = 0.2f

            this.angleX = 45f

            this.scaleX = 0.25f
            this.scaleY = 0.5f
            this.scaleZ = 0.25f
        }

        this.legFrontLeft.position {
            this.x = -0.15f
            this.y = 0.35f

            this.angleX = -90f

            this.scaleX = 0.25f
            this.scaleZ = 0.25f
        }

        this.legFrontRight.position {
            this.x = 0.15f
            this.y = 0.35f

            this.angleX = -90f

            this.scaleX = 0.25f
            this.scaleZ = 0.25f
        }

        this.legBackLeft.position {
            this.x = -0.15f
            this.y = -0.35f

            this.angleX = -90f

            this.scaleX = 0.25f
            this.scaleZ = 0.25f
        }

        this.legBackRight.position {
            this.x = 0.15f
            this.y = -0.35f

            this.angleX = -90f

            this.scaleX = 0.25f
            this.scaleZ = 0.25f
        }

        this.neck.add(this.head)

        this.body.add(this.neck)
        this.body.add(this.legFrontLeft)
        this.body.add(this.legFrontRight)
        this.body.add(this.legBackLeft)
        this.body.add(this.legBackRight)

        this.node.add(this.body)
    }

    /**
     * Create a walk animation
     */
    fun walkAnimation(numberStep: Int = 10, numberFramePerStep: Int = 24): Animation
    {
        val semiStep = max(1, (numberFramePerStep + 1) shr 1)
        val step = semiStep shl 1

        val positionStandFrontLeft = Position3D(x = -0.15f, y = 0.35f,
                                                angleX = -90f,
                                                scaleX = 0.25f, scaleZ = 0.25f)
        val positionStandFrontRight = Position3D(x = 0.15f, y = 0.35f,
                                                 angleX = -90f,
                                                 scaleX = 0.25f, scaleZ = 0.25f)
        val positionStandBackLeft = Position3D(x = -0.15f, y = -0.35f,
                                               angleX = -90f,
                                               scaleX = 0.25f, scaleZ = 0.25f)
        val positionStandBackRight = Position3D(x = 0.15f, y = -0.35f,
                                                angleX = -90f,
                                                scaleX = 0.25f, scaleZ = 0.25f)

        val positionFrontLeftFront = Position3D(x = -0.15f, y = 0.35f,
                                                angleX = -75f,
                                                scaleX = 0.25f, scaleZ = 0.25f)
        val positionFrontRightFront = Position3D(x = 0.15f, y = 0.35f,
                                                 angleX = -75f,
                                                 scaleX = 0.25f, scaleZ = 0.25f)
        val positionFrontLeftBack = Position3D(x = -0.15f, y = -0.35f,
                                               angleX = -75f,
                                               scaleX = 0.25f, scaleZ = 0.25f)
        val positionFrontRightBack = Position3D(x = 0.15f, y = -0.35f,
                                                angleX = -75f,
                                                scaleX = 0.25f, scaleZ = 0.25f)

        val positionBackLeftFront = Position3D(x = -0.15f, y = 0.35f,
                                               angleX = -115f,
                                               scaleX = 0.25f, scaleZ = 0.25f)
        val positionBackRightFront = Position3D(x = 0.15f, y = 0.35f,
                                                angleX = -115f,
                                                scaleX = 0.25f, scaleZ = 0.25f)
        val positionBackLeftBack = Position3D(x = -0.15f, y = -0.35f,
                                              angleX = -115f,
                                              scaleX = 0.25f, scaleZ = 0.25f)
        val positionBackRightBack = Position3D(x = 0.15f, y = -0.35f,
                                               angleX = -115f,
                                               scaleX = 0.25f, scaleZ = 0.25f)

        val animationLegFrontLeft = AnimationNode3D(this.legFrontLeft)
        val animationLegFrontRight = AnimationNode3D(this.legFrontRight)
        val animationLegBackLeft = AnimationNode3D(this.legBackLeft)
        val animationLegBackRight = AnimationNode3D(this.legBackRight)

        var frame = semiStep
        animationLegFrontLeft.frame(frame, positionFrontLeftFront)
        animationLegFrontRight.frame(frame, positionBackRightFront)
        animationLegBackLeft.frame(frame, positionBackLeftBack)
        animationLegBackRight.frame(frame, positionFrontRightBack)

        for (time in 0 until numberStep)
        {
            frame += step

            if (time % 2 == 0)
            {
                animationLegFrontLeft.frame(frame, positionBackLeftFront)
                animationLegFrontRight.frame(frame, positionFrontRightFront)
                animationLegBackLeft.frame(frame, positionFrontLeftBack)
                animationLegBackRight.frame(frame, positionBackRightBack)
            }
            else
            {
                animationLegFrontLeft.frame(frame, positionFrontLeftFront)
                animationLegFrontRight.frame(frame, positionBackRightFront)
                animationLegBackLeft.frame(frame, positionBackLeftBack)
                animationLegBackRight.frame(frame, positionFrontRightBack)
            }
        }

        frame += semiStep
        animationLegFrontLeft.frame(frame, positionStandFrontLeft)
        animationLegFrontRight.frame(frame, positionStandFrontRight)
        animationLegBackLeft.frame(frame, positionStandBackLeft)
        animationLegBackRight.frame(frame, positionStandBackRight)

        val animationWalk = AnimationParallel()
        animationWalk.add(animationLegFrontLeft)
        animationWalk.add(animationLegFrontRight)
        animationWalk.add(animationLegBackLeft)
        animationWalk.add(animationLegBackRight)

        return animationWalk
    }

    /**
     * Create a run animation
     */
    fun runAnimation(numberStep: Int = 10, numberFramePerStep: Int = 12): Animation
    {
        val semiStep = max(1, (numberFramePerStep + 1) shr 1)
        val step = semiStep shl 1

        val positionStandFrontLeft = Position3D(x = -0.15f, y = 0.35f,
                                                angleX = -90f,
                                                scaleX = 0.25f, scaleZ = 0.25f)
        val positionStandFrontRight = Position3D(x = 0.15f, y = 0.35f,
                                                 angleX = -90f,
                                                 scaleX = 0.25f, scaleZ = 0.25f)
        val positionStandBackLeft = Position3D(x = -0.15f, y = -0.35f,
                                               angleX = -90f,
                                               scaleX = 0.25f, scaleZ = 0.25f)
        val positionStandBackRight = Position3D(x = 0.15f, y = -0.35f,
                                                angleX = -90f,
                                                scaleX = 0.25f, scaleZ = 0.25f)

        val positionFrontLeftFront = Position3D(x = -0.15f, y = 0.35f,
                                                angleX = -75f,
                                                scaleX = 0.25f, scaleZ = 0.25f)
        val positionFrontRightFront = Position3D(x = 0.15f, y = 0.35f,
                                                 angleX = -75f,
                                                 scaleX = 0.25f, scaleZ = 0.25f)
        val positionFrontLeftBack = Position3D(x = -0.15f, y = -0.35f,
                                               angleX = -75f,
                                               scaleX = 0.25f, scaleZ = 0.25f)
        val positionFrontRightBack = Position3D(x = 0.15f, y = -0.35f,
                                                angleX = -75f,
                                                scaleX = 0.25f, scaleZ = 0.25f)

        val positionBackLeftFront = Position3D(x = -0.15f, y = 0.35f,
                                               angleX = -115f,
                                               scaleX = 0.25f, scaleZ = 0.25f)
        val positionBackRightFront = Position3D(x = 0.15f, y = 0.35f,
                                                angleX = -115f,
                                                scaleX = 0.25f, scaleZ = 0.25f)
        val positionBackLeftBack = Position3D(x = -0.15f, y = -0.35f,
                                              angleX = -115f,
                                              scaleX = 0.25f, scaleZ = 0.25f)
        val positionBackRightBack = Position3D(x = 0.15f, y = -0.35f,
                                               angleX = -115f,
                                               scaleX = 0.25f, scaleZ = 0.25f)

        val animationLegFrontLeft = AnimationNode3D(this.legFrontLeft)
        val animationLegFrontRight = AnimationNode3D(this.legFrontRight)
        val animationLegBackLeft = AnimationNode3D(this.legBackLeft)
        val animationLegBackRight = AnimationNode3D(this.legBackRight)

        var frame = semiStep
        animationLegFrontLeft.frame(frame, positionBackLeftFront)
        animationLegFrontRight.frame(frame, positionBackRightFront)
        animationLegBackLeft.frame(frame, positionFrontLeftBack)
        animationLegBackRight.frame(frame, positionFrontRightBack)

        for (time in 0 until numberStep)
        {
            frame += step

            if (time % 2 == 0)
            {
                animationLegFrontLeft.frame(frame, positionFrontLeftFront)
                animationLegFrontRight.frame(frame, positionFrontRightFront)
                animationLegBackLeft.frame(frame, positionBackLeftBack)
                animationLegBackRight.frame(frame, positionBackRightBack)
            }
            else
            {
                animationLegFrontLeft.frame(frame, positionBackLeftFront)
                animationLegFrontRight.frame(frame, positionBackRightFront)
                animationLegBackLeft.frame(frame, positionFrontLeftBack)
                animationLegBackRight.frame(frame, positionFrontRightBack)
            }
        }

        frame += semiStep
        animationLegFrontLeft.frame(frame, positionStandFrontLeft)
        animationLegFrontRight.frame(frame, positionStandFrontRight)
        animationLegBackLeft.frame(frame, positionStandBackLeft)
        animationLegBackRight.frame(frame, positionStandBackRight)

        val animationWalk = AnimationParallel()
        animationWalk.add(animationLegFrontLeft)
        animationWalk.add(animationLegFrontRight)
        animationWalk.add(animationLegBackLeft)
        animationWalk.add(animationLegBackRight)

        return animationWalk
    }

}