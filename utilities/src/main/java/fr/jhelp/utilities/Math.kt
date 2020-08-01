/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.utilities

import kotlin.math.E
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.ceil
import kotlin.math.exp
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round
import kotlin.math.sqrt


const val PI_FLOAT = PI.toFloat()

const val E_FLOAT = E.toFloat()

const val TWO_PI = 2.0 * PI

const val TWO_PI_FLOAT = 2.0f * PI_FLOAT

/**
 * Double precision, the "zero"
 */
val EPSILON = maximum(Double.MIN_VALUE, abs(E - exp(1.0)), abs(PI - acos(-1.0)))

/**
 * Float precision, the "zero"
 */
val EPSILON_FLOAT = maximum(Float.MIN_VALUE, abs(E_FLOAT - exp(1.0f)), abs(PI_FLOAT - acos(-1.0f)))

/**
 * Indicates if Double can be assimilate to zero
 */
val Double.nul get() = abs(this) <= EPSILON

/**
 * Indicates if Float can be assimilate to zero
 */
val Float.nul get() = abs(this) <= EPSILON_FLOAT

/**
 * Double sign
 */
fun Double.sign(): Int =
    when
    {
        this.nul -> 0
        this > 0 -> 1
        else     -> -1
    }

/**
 * Float sign
 */
fun Float.sign(): Int =
    when
    {
        this.nul -> 0
        this > 0 -> 1
        else     -> -1
    }

/**
 * Compare with an other Double
 */
fun Double.compare(real: Double) =
    (this - real).sign()

/**
 * Compare with an other Float
 */
fun Float.compare(real: Float) =
    (this - real).sign()

/**
 * Indicates if other Double is same aas this one
 */
fun Double.same(real: Double) =
    abs(this - real).nul

/**
 * Indicates if other Float is same aas this one
 */
fun Float.same(real: Float) =
    abs(this - real).nul

/**
 * Maximum of several integers
 */
fun maximum(value: Int, vararg others: Int): Int
{
    var maximum = value

    for (integer in others)
    {
        maximum = max(maximum, integer)
    }

    return maximum
}

/**
 * Maximum of several integers
 */
fun maximum(value: Long, vararg others: Long): Long
{
    var maximum = value

    for (integer in others)
    {
        maximum = max(maximum, integer)
    }

    return maximum
}

/**
 * Maximum of several real
 */
fun maximum(value: Float, vararg others: Float): Float
{
    var maximum = value

    for (integer in others)
    {
        maximum = max(maximum, integer)
    }

    return maximum
}

/**
 * Maximum of several real
 */
fun maximum(value: Double, vararg others: Double): Double
{
    var maximum = value

    for (integer in others)
    {
        maximum = max(maximum, integer)
    }

    return maximum
}

/**
 * Minimum of several integers
 */
fun minimum(value: Int, vararg others: Int): Int
{
    var minimum = value

    for (integer in others)
    {
        minimum = min(minimum, integer)
    }

    return minimum
}

/**
 * Minimum of several integers
 */
fun minimum(value: Long, vararg others: Long): Long
{
    var minimum = value

    for (integer in others)
    {
        minimum = min(minimum, integer)
    }

    return minimum
}

/**
 * Minimum of several real
 */
fun minimum(value: Float, vararg others: Float): Float
{
    var minimum = value

    for (integer in others)
    {
        minimum = min(minimum, integer)
    }

    return minimum
}

/**
 * Minimum of several real
 */
fun minimum(value: Double, vararg others: Double): Double
{
    var minimum = value

    for (integer in others)
    {
        minimum = min(minimum, integer)
    }

    return minimum
}

/**
 * Greatest Common Divider
 */
infix fun Int.GCD(integer: Int): Int
{
    var minimum = min(abs(this), abs(integer))
    var maximum = max(abs(this), abs(integer))
    var temporary: Int

    while (minimum > 0)
    {
        temporary = minimum
        minimum = maximum % minimum
        maximum = temporary
    }

    return maximum
}

/**
 * Greatest Common Divider
 */
infix fun Long.GCD(integer: Long): Long
{
    var minimum = min(abs(this), abs(integer))
    var maximum = max(abs(this), abs(integer))
    var temporary: Long

    while (minimum > 0)
    {
        temporary = minimum
        minimum = maximum % minimum
        maximum = temporary
    }

    return maximum
}

/**
 * Lowest Common Multiple
 */
infix fun Int.LCM(integer: Int): Int
{
    val gcd = this GCD integer

    if (gcd == 0)
    {
        return 0
    }

    return this * (integer / gcd)
}

/**
 * Lowest Common Multiple
 */
infix fun Long.LCM(integer: Long): Long
{
    val gcd = this GCD integer

    if (gcd == 0L)
    {
        return 0L
    }

    return this * (integer / gcd)
}

/**
 * Log2 base 2 of given integer
 */
fun log2(integer: Int): Int
{
    if (integer <= 1)
    {
        return 0
    }

    var left = integer shr 1
    var log2 = 1

    while (left > 1)
    {
        log2++
        left = left shr 1
    }

    return log2
}

fun square(number: Int) = number * number

fun square(number: Long) = number * number

fun square(number: Float) = number * number

fun square(number: Double) = number * number

/**
 * Compute the cubic interpolation
 *
 * @param cp Start value
 * @param p1 First control point
 * @param p2 Second control point
 * @param p3 Third control point
 * @param t  Factor in [0, 1]
 * @return Interpolation
 */
fun cubic(cp: Double, p1: Double, p2: Double, p3: Double, t: Double): Double
{
    val u = 1.0 - t
    return u * u * u * cp + 3.0 * t * u * u * p1 + 3.0 * t * t * u * p2 + t * t * t * p3
}

/**
 * Compute several cubic interpolation
 *
 * @param cp        Start value
 * @param p1        First control point
 * @param p2        Second control point
 * @param p3        Third control point
 * @param precision Number of interpolation
 * @param cubic     Where write interpolations. If `null` or length too small, a new array is created
 * @return Interpolations
 */
fun cubic(cp: Double, p1: Double, p2: Double, p3: Double, precision: Int,
          cubic: DoubleArray? = null): DoubleArray
{
    var cubicLocal = cubic
    var actual: Double

    if (cubicLocal == null || cubicLocal.size < precision)
    {
        cubicLocal = DoubleArray(precision)
    }

    val step = 1.0 / (precision - 1.0)
    actual = 0.0

    for (i in 0 until precision)
    {
        if (i == precision - 1)
        {
            actual = 1.0
        }

        cubicLocal[i] = cubic(cp, p1, p2, p3, actual)
        actual += step
    }

    return cubicLocal
}


/**
 * Compute the quadratic interpolation
 *
 * @param cp Start value
 * @param p1 First control point
 * @param p2 Second control point
 * @param t  Factor in [0, 1]
 * @return Interpolation
 */
fun quadratic(cp: Double, p1: Double, p2: Double, t: Double): Double
{
    val u = 1.0 - t
    return u * u * cp + 2.0 * t * u * p1 + t * t * p2
}

/**
 * Compute several quadratic interpolation
 *
 * @param cp        Start value
 * @param p1        First control point
 * @param p2        Second control point
 * @param precision Number of interpolation
 * @param quadratic Where write interpolations
 * @return Interpolations
 */
fun quadratic(cp: Double, p1: Double, p2: Double, precision: Int,
              quadratic: DoubleArray? = null): DoubleArray
{
    var quadraticLocal = quadratic
    var actual: Double

    if (quadraticLocal == null || quadraticLocal.size < precision)
    {
        quadraticLocal = DoubleArray(precision)
    }

    val step = 1.0 / (precision - 1.0)
    actual = 0.0

    for (i in 0 until precision)
    {
        if (i == precision - 1)
        {
            actual = 1.0
        }

        quadraticLocal[i] = quadratic(cp, p1, p2, actual)
        actual += step
    }

    return quadraticLocal
}


/**
 * Compute the cubic interpolation
 *
 * @param cp Start value
 * @param p1 First control point
 * @param p2 Second control point
 * @param p3 Third control point
 * @param t  Factor in [0, 1]
 * @return Interpolation
 */
fun cubic(cp: Float, p1: Float, p2: Float, p3: Float, t: Float): Float
{
    val u = 1.0f - t
    return u * u * u * cp + 3.0f * t * u * u * p1 + 3.0f * t * t * u * p2 + t * t * t * p3
}

/**
 * Compute several cubic interpolation
 *
 * @param cp        Start value
 * @param p1        First control point
 * @param p2        Second control point
 * @param p3        Third control point
 * @param precision Number of interpolation
 * @param cubic     Where write interpolations. If `null` or length too small, a new array is created
 * @return Interpolations
 */
fun cubic(cp: Float, p1: Float, p2: Float, p3: Float, precision: Int,
          cubic: FloatArray? = null): FloatArray
{
    var cubicLocal = cubic
    var actual: Float

    if (cubicLocal == null || cubicLocal.size < precision)
    {
        cubicLocal = FloatArray(precision)
    }

    val step = 1.0f / (precision - 1.0f)
    actual = 0.0f

    for (i in 0 until precision)
    {
        if (i == precision - 1)
        {
            actual = 1.0f
        }

        cubicLocal[i] = cubic(cp, p1, p2, p3, actual)
        actual += step
    }

    return cubicLocal
}


/**
 * Compute the quadratic interpolation
 *
 * @param cp Start value
 * @param p1 First control point
 * @param p2 Second control point
 * @param t  Factor in [0, 1]
 * @return Interpolation
 */
fun quadratic(cp: Float, p1: Float, p2: Float, t: Float): Float
{
    val u = 1.0f - t
    return u * u * cp + 2.0f * t * u * p1 + t * t * p2
}

/**
 * Compute several quadratic interpolation
 *
 * @param cp        Start value
 * @param p1        First control point
 * @param p2        Second control point
 * @param precision Number of interpolation
 * @param quadratic Where write interpolations
 * @return Interpolations
 */
fun quadratic(cp: Float, p1: Float, p2: Float, precision: Int,
              quadratic: FloatArray? = null): FloatArray
{
    var quadraticLocal = quadratic
    var actual: Float

    if (quadraticLocal == null || quadraticLocal.size < precision)
    {
        quadraticLocal = FloatArray(precision)
    }

    val step = 1.0f / (precision - 1.0f)
    actual = 0.0f

    for (i in 0 until precision)
    {
        if (i == precision - 1)
        {
            actual = 1.0f
        }

        quadraticLocal[i] = quadratic(cp, p1, p2, actual)
        actual += step
    }

    return quadraticLocal
}

/**
 * Distance between 2D coordinates
 */
fun distance(x1: Float, y1: Float, x2: Float, y2: Float) =
    sqrt(square(x1 - x2) + square(y1 - y2))

/**
 * Distance between 3D coordinates
 */
fun distance(x1: Float, y1: Float, z1: Float, x2: Float, y2: Float, z2: Float) =
    sqrt(square(x1 - x2) + square(y1 - y2) + square(z1 - z2))


/**
 * Compute the modulo of a real
 *
 * @param real   Real to modulate
 * @param modulo Modulo to use
 * @return Result
 */
fun modulo(real: Double, modulo: Double) = moduloInterval(real, 0.0, modulo)

/**
 * Compute the modulo of a real
 *
 * @param real   Real to modulate
 * @param modulo Modulo to use
 * @return Result
 */
fun modulo(real: Float, modulo: Float) = moduloInterval(real, 0f, modulo)

/**
 * Mathematical modulo.
 *
 * For computer -1 modulo 2 is -1, but in Mathematic -1[2]=1 (-1[2] : -1 modulo 2)
 *
 * @param integer Integer to modulate
 * @param modulo  Modulo to apply
 * @return Mathematical modulo : `integer[modulo]`
 */
fun modulo(integer: Int, modulo: Int): Int
{
    var integerLocal = integer
    integerLocal %= modulo

    if (integerLocal < 0 && modulo > 0 || integerLocal > 0 && modulo < 0)
    {
        integerLocal += modulo
    }

    return integerLocal
}

/**
 * Modulate an integer inside an interval
 *
 * @param integer Integer to modulate
 * @param min  Minimum of interval
 * @param max  Maximum of interval
 * @return Modulated value
 */
fun moduloInterval(integer: Int, min: Int, max: Int) = min + modulo(integer - min, max - min + 1)

/**
 * Modulate an integer inside an interval
 *
 * @param integer Integer to modulate
 * @param min  Minimum of interval
 * @param max  Maximum of interval
 * @return Modulated value
 */
fun moduloInterval(integer: Long, min: Long, max: Long) =
    min + modulo(integer - min, max - min + 1L)

/**
 * Mathematical modulo.
 *
 * For computer -1 modulo 2 is -1, but in Mathematic -1[2]=1 (-1[2] : -1 modulo 2)
 *
 * @param integer Integer to modulate
 * @param modulo  Modulo to apply
 * @return Mathematical modulo : `integer[modulo]`
 */
fun modulo(integer: Long, modulo: Long): Long
{
    var modulate = integer % modulo

    if (modulate < 0 && modulo > 0 || modulate > 0 && modulo < 0)
    {
        modulate += modulo
    }

    return modulate
}

/**
 * Modulate a real inside an interval
 *
 * @param real Real to modulate
 * @param min  Minimum of interval include
 * @param max  Maximum of interval exclude
 * @return Modulated value
 */
fun moduloInterval(real: Double, min: Double, max: Double): Double
{
    var realLocal = real
    var minLocal = min
    var maxLocal = max

    if (minLocal > maxLocal)
    {
        val temp = minLocal
        minLocal = maxLocal
        maxLocal = temp
    }

    if (realLocal >= minLocal && realLocal < maxLocal)
    {
        return realLocal
    }

    val space = maxLocal - minLocal

    if (space.nul)
    {
        throw IllegalArgumentException("Can't take modulo in empty interval")
    }

    realLocal = (realLocal - minLocal) / space

    return space * (realLocal - floor(realLocal)) + minLocal
}

/**
 * Modulate a real inside an interval
 *
 * @param real Real to modulate
 * @param min  Minimum of interval include
 * @param max  Maximum of interval exclude
 * @return Modulated value
 */
fun moduloInterval(real: Float, min: Float, max: Float): Float
{
    var realLocal = real
    var minLocal = min
    var maxLocal = max

    if (minLocal > maxLocal)
    {
        val temp = minLocal
        minLocal = maxLocal
        maxLocal = temp
    }

    if (realLocal >= minLocal && realLocal < maxLocal)
    {
        return realLocal
    }

    val space = maxLocal - minLocal

    if (space.nul)
    {
        throw IllegalArgumentException("Can't take modulo in empty interval")
    }

    realLocal = (realLocal - minLocal) / space

    return space * (realLocal - floor(realLocal)) + minLocal
}

fun degreeToGrade(degree: Double) = degree * 0.9

fun radianToGrade(radian: Double) = radian * 200.0 / PI

fun gradeToDegree(grade: Double) = grade / 0.9

fun gradeToRadian(grade: Double) = grade * PI / 200.0

fun degreeToRadian(degree: Float) = degree * PI_FLOAT / 180.0f

fun radianToDegree(radian: Float) = radian * 180.0f / PI_FLOAT

fun degreeToGrade(degree: Float) = degree * 0.9f

fun radianToGrade(radian: Float) = radian * 200.0f / PI_FLOAT

fun gradeToDegree(grade: Float) = grade / 0.9f

fun gradeToRadian(grade: Float) = grade * PI_FLOAT / 200.0f

/**
 * The maximum integer lower or equals to this number
 */
val Float.floor get() = floor(this).toInt()

/**
 * The minimum integer greater or equals to this number
 */
val Float.ciel get() = ceil(this).toInt()

/**
 * The nearest integer to this number
 */
val Float.round get() = round(this).toInt()

/**
 * Limit the number in given bounds:
 * * If the number lower or equals the minimum of bounds, the minimum is returns
 * * Else if the number greater or equals the maximum of bounds, the maximum is returns
 * * Else, it's mean number inside bounds, the number is return as is
 */
fun Int.bounds(bound1: Int, bound2: Int): Int
{
    val min = min(bound1, bound2)
    val max = max(bound1, bound2)
    return min(max, max(min, this))
}

/**
 * Limit the number in given bounds:
 * * If the number lower or equals the minimum of bounds, the minimum is returns
 * * Else if the number greater or equals the maximum of bounds, the maximum is returns
 * * Else, it's mean number inside bounds, the number is return as is
 */
fun Long.bounds(bound1: Long, bound2: Long): Long
{
    val min = min(bound1, bound2)
    val max = max(bound1, bound2)
    return min(max, max(min, this))
}

/**
 * Limit the number in given bounds:
 * * If the number lower or equals the minimum of bounds, the minimum is returns
 * * Else if the number greater or equals the maximum of bounds, the maximum is returns
 * * Else, it's mean number inside bounds, the number is return as is
 */
fun Float.bounds(bound1: Float, bound2: Float): Float
{
    val min = min(bound1, bound2)
    val max = max(bound1, bound2)
    return min(max, max(min, this))
}

/**
 * Limit the number in given bounds:
 * * If the number lower or equals the minimum of bounds, the minimum is returns
 * * Else if the number greater or equals the maximum of bounds, the maximum is returns
 * * Else, it's mean number inside bounds, the number is return as is
 */
fun Double.bounds(bound1: Double, bound2: Double): Double
{
    val min = min(bound1, bound2)
    val max = max(bound1, bound2)
    return min(max, max(min, this))
}