/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.utilities.formal

import java.util.TreeSet

/**
 * Generic mathematical function with one parameter
 */
abstract class UnaryOperator<UO : UnaryOperator<UO>>
internal constructor(val parameter: MathFunction<*>) : MathFunction<UO>()
{
    /**
     * Create specific type new instance with given parameter
     */
    protected abstract fun newInstance(parameter: MathFunction<*>): UO

    /**
     * Do an action on this function, if its match the given class or create same operation type with try do action on parameters
     */
    @Suppress("UNCHECKED_CAST")
    final override fun <M : MathFunction<M>> applyDeep(clazz: Class<M>,
                                                       action: (M) -> MathFunction<*>): MathFunction<*> =
        if (clazz == this.javaClass) action(this as M)
        else this.newInstance(this.parameter.applyDeep(clazz, action))

    /**
     * Indicates if given function equals.
     * The given function is guarantee to have the same type
     */
    final override fun equalsIntern(mathFunction: UO) =
        this.parameter == mathFunction.parameter

    final override fun hash() =
        this.parameter.hashCode()

    /**
     * Collect variables
     */
    final override fun collectVariables(collector: TreeSet<Variable>)
    {
        this.parameter.collectVariables(collector)
    }

    /**
     * Replace a variable by given function
     */
    final override fun replace(variable: Variable, mathFunction: MathFunction<*>): MathFunction<*> =
        this.newInstance(this.parameter.replace(variable, mathFunction))
}