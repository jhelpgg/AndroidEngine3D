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

package fr.jhelp.utilities.formal

import fr.jhelp.utilities.extensions.VARIABLE_NAME_REGEX
import java.util.TreeSet

/**
 * A variable
 */
class Variable(name: String) : MathFunction<Variable>()
{
    val name: String

    init
    {
        if (!VARIABLE_NAME_REGEX.matches(name))
        {
            throw IllegalArgumentException(
                "'$name' is not acceptable variable name. Variable valid name must start with letter follow by zero or more letter or digit or underscore")
        }

        this.name = name
    }

    /**
     * One step in simplification
     */
    override fun simple(): MathFunction<*> = this

    override fun equalsIntern(mathFunction: Variable) =
        this.name == mathFunction.name

    override fun hash() =
        this.name.hashCode()

    /**
     * Compute derivative on given variable
     */
    override fun derivative(variable: Variable): MathFunction<*> =
        if (this.name == variable.name) ONE
        else ZERO

    /**
     * Collect variables used by the function
     */
    override fun collectVariables(collector: TreeSet<Variable>)
    {
        collector.add(this)
    }

    override fun toString(): String =
        this.name

    /**
     * Replace a variable by a function
     */
    override fun replace(variable: Variable, mathFunction: MathFunction<*>): MathFunction<*> =
        if (this.name == variable.name) mathFunction
        else this
}