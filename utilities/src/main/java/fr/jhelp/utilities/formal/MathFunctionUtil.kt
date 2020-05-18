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

fun MathFunction<*>.simpleType() =
    this is Constant || this is Variable

/**
 * Make an addition of several functions
 */
fun addition(list: List<MathFunction<*>>): MathFunction<*>
{
    if (list.isEmpty())
    {
        return ZERO
    }

    var result = list[0]

    for (index in 1 until list.size)
    {
        result += list[index]
    }

    return result
}

/**
 * Make an addition of several functions
 */
fun addition(vararg functions: MathFunction<*>): MathFunction<*>
{
    if (functions.isEmpty())
    {
        return ZERO
    }

    var result = functions[0]

    for (index in 1 until functions.size)
    {
        result += functions[index]
    }

    return result
}

/**
 * Make an multiplication of several functions
 */
fun multiplication(list: List<MathFunction<*>>): MathFunction<*>
{
    if (list.isEmpty())
    {
        return ZERO
    }

    var result = list[0]

    for (index in 1 until list.size)
    {
        result *= list[index]
    }

    return result
}

/**
 * Make an multiplication of several functions
 */
fun multiplication(vararg functions: MathFunction<*>): MathFunction<*>
{
    if (functions.isEmpty())
    {
        return ZERO
    }

    var result = functions[0]

    for (index in 1 until functions.size)
    {
        result *= functions[index]
    }

    return result
}