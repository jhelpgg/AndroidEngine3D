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

/**
 * Sinus function
 */
class Sinus(parameter: MathFunction<*>) : UnaryOperator<Sinus>(parameter)
{
    override fun newInstance(parameter: MathFunction<*>): Sinus =
        sin(parameter)

    /**
     * One step in simplification
     */
    override fun simple(): MathFunction<*> =
        when (this.parameter)
        {
            INVALID       ->
                INVALID
            // sin(C1) = C2 where C2=sin(C1)
            is Constant   ->
                kotlin.math.sin(this.parameter.value).const
            // sin(-F) -> -sin(F)
            is UnaryMinus ->
                -sin(this.parameter.parameter.simplify())
            // TODO other cases
            else          ->
                sin(this.parameter.simplify())
        }

    /**
     * Compute derivative on given variable
     */
    override fun derivative(variable: Variable): MathFunction<*> =
        this.parameter.derivative(variable) * cos(this.parameter)

    override fun toString(): String =
        "sin(${this.parameter})"
}