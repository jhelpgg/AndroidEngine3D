package fr.jhelp.utilities

import java.util.Random
import kotlin.math.max
import kotlin.math.min


private val RANDOM = Random()

fun random(bound1: Int, bound2: Int): Int
{
    val min = min(bound1, bound2)
    val max = max(bound1, bound2)
    return min + RANDOM.nextInt(max - min + 1)
}

fun random(bound1: Long, bound2: Long): Long
{
    val min = min(bound1, bound2)
    val max = max(bound1, bound2)
    return min + (RANDOM.nextDouble() * (max - min + 1)).toLong()
}

fun random(bound1: Float, bound2: Float): Float
{
    val min = min(bound1, bound2)
    val max = max(bound1, bound2)
    return min + (RANDOM.nextFloat() * (max - min))
}

fun random(bound1: Double, bound2: Double): Double
{
    val min = min(bound1, bound2)
    val max = max(bound1, bound2)
    return min + (RANDOM.nextDouble() * (max - min))
}