/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.utilities

import java.util.Random
import kotlin.math.max
import kotlin.math.min


private val RANDOM = Random()

/**
 * Give a number randomly choose in given bounds.
 *
 * Bounds are include
 */
fun random(bound1: Int, bound2: Int): Int
{
    val min = min(bound1, bound2)
    val max = max(bound1, bound2)
    return min + RANDOM.nextInt(max - min + 1)
}

/**
 * Give a number randomly choose in given bounds.
 *
 * Bounds are include
 */
fun random(bound1: Long, bound2: Long): Long
{
    val min = min(bound1, bound2)
    val max = max(bound1, bound2)
    return min + (RANDOM.nextDouble() * (max - min + 1)).toLong()
}

/**
 * Give a number randomly choose in given bounds.
 *
 * The minimum of bound i include, the maximum exclude.
 * That is to say, where  :
 * ````
 * min = min(bounds1, bounds2)
 * max = max(bounds1, bounds2)
 * ````
 * The chosen number where in [min, max[
 */
fun random(bound1: Float, bound2: Float): Float
{
    val min = min(bound1, bound2)
    val max = max(bound1, bound2)
    return min + (RANDOM.nextFloat() * (max - min))
}

/**
 * Give a number randomly choose in given bounds.
 *
 * The minimum of bound i include, the maximum exclude.
 * That is to say, where  :
 * ````
 * min = min(bounds1, bounds2)
 * max = max(bounds1, bounds2)
 * ````
 * The chosen number where in [min, max[
 */
fun random(bound1: Double, bound2: Double): Double
{
    val min = min(bound1, bound2)
    val max = max(bound1, bound2)
    return min + (RANDOM.nextDouble() * (max - min))
}

fun <T> List<T>.random(): T =
    this[random(0, this.size - 1)]

fun <T> Array<T>.random(): T =
    this[random(0, this.size - 1)]

inline fun <reified E : Enum<E>> random(): E =
    (E::class.java.getDeclaredMethod("values").invoke(null) as Array<E>).random()

