/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.utilities

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


const val TIMES = 16

class RandomTests
{
    @Test
    fun randomInt()
    {
        val min = (Math.random() * 20 - 10).toInt()
        val max = min + (Math.random() * 10 + 1).toInt()

        for (time in 0 until TIMES)
        {
            val value = random(min, max)
            Assertions.assertTrue(value in min..max, "$value not in [$min, $max]")
        }
    }

    @Test
    fun randomLong()
    {
        val min = (Math.random() * 20 - 10).toLong()
        val max = min + (Math.random() * 10 + 1).toLong()

        for (time in 0 until TIMES)
        {
            val value = random(min, max)
            Assertions.assertTrue(value in min..max, "$value not in [$min, $max]")
        }
    }

    @Test
    fun randomFloat()
    {
        val min = (Math.random() * 20 - 10).toFloat()
        val max = min + (Math.random() * 10 + 1).toFloat()

        for (time in 0 until TIMES)
        {
            val value = random(min, max)
            Assertions.assertTrue(value >= min && value < max, "$value not in [$min, $max[")
        }
    }

    @Test
    fun randomDouble()
    {
        val min = Math.random() * 20 - 10
        val max = min + Math.random() * 10 + 1

        for (time in 0 until TIMES)
        {
            val value = random(min, max)
            Assertions.assertTrue(value >= min && value < max, "$value not in [$min, $max[")
        }
    }

    @Test
    fun randomList()
    {
        val list = listOf("T1", "T2", "T3")

        for (time in 0 until TIMES)
        {
            val value = list.random()
            Assertions.assertTrue(value in list, "$value not in $list")
        }
    }

    @Test
    fun randomArray()
    {
        val array = arrayOf("T1", "T2", "T3")

        for (time in 0 until TIMES)
        {
            val value = array.random()
            Assertions.assertTrue(value in array, "$value not in $array")
        }
    }

    @Test
    fun randomEnum()
    {
        val values = TestEnum.values()

        for (time in 0 until TIMES)
        {
            val value = random<TestEnum>()
            Assertions.assertTrue(value in values, "$value not in $values")
        }
    }
}