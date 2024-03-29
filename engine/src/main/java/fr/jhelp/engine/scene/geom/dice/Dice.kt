/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.engine.scene.geom.dice

import fr.jhelp.animations.Animation
import fr.jhelp.animations.AnimationList
import fr.jhelp.animations.AnimationTask
import fr.jhelp.engine.R
import fr.jhelp.engine.animation.keyFrame.AnimationNode3D
import fr.jhelp.engine.resources.ResourcesAccess
import fr.jhelp.engine.scene.Color3D
import fr.jhelp.engine.scene.GREY
import fr.jhelp.engine.scene.Node3D
import fr.jhelp.engine.scene.Position3D
import fr.jhelp.engine.scene.geom.Box
import fr.jhelp.engine.scene.geom.CrossUV
import fr.jhelp.tasks.observable.Observable
import fr.jhelp.tasks.observable.ObservableValue
import fr.jhelp.utilities.bounds
import fr.jhelp.utilities.random
import kotlin.math.max

private val DICE_POSITIONS = arrayOf(Position3D(), // 1: Face
                                     Position3D(angleY = -90f), // 2: Right
                                     Position3D(angleX = -90f), // 3: Bottom
                                     Position3D(angleX = 90f), // 4: Top
                                     Position3D(angleY = 90f), // 5: Left
                                     Position3D(angleY = 180f)) // 6: Back

/**
 * A dice
 */
class Dice(@DiceValue value: Int = random(1, 6)) : Node3D()
{
    private val dice = Box(CrossUV())
    var value = value.bounds(1, 6)
        private set
    private val diceInfoObservableValue =
        ObservableValue<DiceInfo>(DiceInfo(this.id, this.name, this.value))
    val diceInfoObservable: Observable<DiceInfo> = this.diceInfoObservableValue.observable
    private val changeValue = { value: Int ->
        this.value = value
        this.diceInfoObservableValue.value = DiceInfo(this.id, this.name, this.value)
    }

    init
    {
        this.dice.material.texture = ResourcesAccess.obtainTexture(R.drawable.dice)
        this.dice.position = DICE_POSITIONS[this.value - 1]
        this.add(this.dice)
    }

    /**
     * Create animation tha change the dice value
     * @param value New dice value: 1, 2, 3, 4, 5 or 6
     * @param numberFrame Animation duration in frames
     * @return Created animation
     */
    @Throws(IllegalArgumentException::class)
    fun value(@DiceValue value: Int, numberFrame: Int = 1): Animation
    {
        val diceValue = value.bounds(1, 6)
        val animation = AnimationList()
        val animationNode = AnimationNode3D(this.dice)
        animationNode.frame(max(1, numberFrame), DICE_POSITIONS[diceValue - 1])
        animation.add(animationNode)
        animation.add(AnimationTask(diceValue, this.changeValue))
        return animation
    }

    /**
     * Create animation that roll the dice
     * @return Created animation
     */
    fun roll(): Animation
    {
        val animationNode = AnimationNode3D(this.dice)
        var frame = 1
        val time = random(12, 25)
        var lastValue = this.value
        var value = this.value

        for (index in 0..time)
        {
            do
            {
                value = random(1, 6)
            }
            while (value == lastValue)

            lastValue = value
            animationNode.frame(frame, DICE_POSITIONS[value - 1])
            frame += index + 1
        }

        val animation = AnimationList()
        animation.add(animationNode)
        animation.add(AnimationTask(value, this.changeValue))
        return animation
    }

    fun color(color: Color3D = GREY)
    {
        this.dice.material.diffuse = color
    }
}