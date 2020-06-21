/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.engine.animation

import fr.jhelp.engine.scene.Node3D
import fr.jhelp.utilities.formal.Constant
import fr.jhelp.utilities.formal.MathFunction
import fr.jhelp.utilities.formal.T
import fr.jhelp.utilities.formal.Variable
import java.util.TreeSet
import kotlin.math.max

/**
 * Animation follow a parametric equation.
 *
 * Part X, Y and Z , must depends only on [T]
 */
fun animationEquation(node: Node3D,
                      functionX: MathFunction<*>,
                      functionY: MathFunction<*>,
                      functionZ: MathFunction<*>,
                      tStart: Double, tEnd: Double,
                      milliseconds: Int, fps: Int = 25) =
    AnimationEquation(node,
                      functionX, functionY, functionZ,
                      tStart, tEnd,
                      (milliseconds * fps) / 1000, fps)


/**
 * Animation follow a parametric equation.
 *
 * Part X, Y and Z , must depends only on [T]
 */
class AnimationEquation(private val node: Node3D,
                        functionX: MathFunction<*>,
                        functionY: MathFunction<*>,
                        functionZ: MathFunction<*>,
                        private val tStart: Double, private val tEnd: Double,
                        numberFrame: Int, fps: Int = 25) : Animation(fps)
{
    private val numberFrame = max(1, numberFrame)
    private val functionX = this.checkAndSimplify(functionX)
    private val functionY = this.checkAndSimplify(functionY)
    private val functionZ = this.checkAndSimplify(functionZ)

    private fun checkAndSimplify(function: MathFunction<*>): MathFunction<*>
    {
        val simplify = function.simplifyMax()
        val collector = TreeSet<Variable>()
        simplify.collectVariables(collector)

        return when (collector.size)
        {
            0    -> simplify
            1    ->
                if (T in collector) simplify
                else throw IllegalArgumentException("function '$function' not depends on T")
            else -> throw IllegalArgumentException(
                "function '$function' have more than one variable")
        }
    }

    override fun animate(frame: Float): Boolean
    {
        if (frame >= this.numberFrame)
        {
            this.node.position.x =
                (this.functionX.replace(T, this.tEnd).simplifyMax() as Constant).value.toFloat()
            this.node.position.y =
                (this.functionY.replace(T, this.tEnd).simplifyMax() as Constant).value.toFloat()
            this.node.position.z =
                (this.functionZ.replace(T, this.tEnd).simplifyMax() as Constant).value.toFloat()
            return false
        }

        val t = this.tStart + (((this.tEnd - this.tStart) * frame) / this.numberFrame.toDouble())
        this.node.position.x =
            (this.functionX.replace(T, t).simplifyMax() as Constant).value.toFloat()
        this.node.position.y =
            (this.functionY.replace(T, t).simplifyMax() as Constant).value.toFloat()
        this.node.position.z =
            (this.functionZ.replace(T, t).simplifyMax() as Constant).value.toFloat()
        return true
    }
}