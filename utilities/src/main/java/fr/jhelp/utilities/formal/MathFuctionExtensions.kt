/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.utilities.formal

/**
 * Transform a Double to constant
 */
val Double.const
    get() =
        Constant(this).simplify() as Constant

/**
 * Transform a Nubmer to constant
 */
val Number.const
    get() =
        this.toDouble().const

/**
 * Transform a String to variable
 */
val String.variable
    get() =
        Variable(this)

val X = "X".variable
val Y = "Y".variable
val Z = "Z".variable
val T = "T".variable
val A = "A".variable
val alpha = "alpha".variable
val epsilon = "epsilon".variable

operator fun MathFunction<*>.unaryMinus() =
    UnaryMinus(this)

operator fun MathFunction<*>.plus(mathFunction: MathFunction<*>) =
    Addition(this, mathFunction)

operator fun MathFunction<*>.minus(mathFunction: MathFunction<*>) =
    Subtraction(this, mathFunction)

operator fun MathFunction<*>.times(mathFunction: MathFunction<*>) =
    Multiplication(this, mathFunction)

operator fun MathFunction<*>.div(mathFunction: MathFunction<*>) =
    Division(this, mathFunction)

operator fun MathFunction<*>.plus(number: Number) =
    number.const + this

operator fun MathFunction<*>.minus(number: Number) =
    -number.const + this

operator fun MathFunction<*>.times(number: Number) =
    number.const * this

operator fun MathFunction<*>.div(number: Number) =
    number.const.invert() * this

operator fun Number.plus(mathFunction: MathFunction<*>) =
    this.const + mathFunction

operator fun Number.minus(mathFunction: MathFunction<*>) =
    this.const - mathFunction

operator fun Number.times(mathFunction: MathFunction<*>) =
    this.const * mathFunction

operator fun Number.div(mathFunction: MathFunction<*>) =
    this.const / mathFunction

operator fun MathFunction<*>.plus(string: String) =
    this + string.variable

operator fun MathFunction<*>.minus(string: String) =
    this - string.variable

operator fun MathFunction<*>.times(string: String) =
    this * string.variable

operator fun MathFunction<*>.div(string: String) =
    this / string.variable

fun cos(mathFunction: MathFunction<*>) =
    Cosinus(mathFunction)

fun sin(mathFunction: MathFunction<*>) =
    Sinus(mathFunction)