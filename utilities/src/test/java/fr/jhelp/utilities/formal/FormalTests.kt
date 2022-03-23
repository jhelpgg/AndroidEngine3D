/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.utilities.formal

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class FormalTests
{
    @Test
    fun toStringTest()
    {
        Assertions.assertEquals("3.0 + X", (3 + X).toString())
        Assertions.assertEquals("3.0 + X + Y", (3 + X + Y).toString())
        Assertions.assertEquals("3.0 + X * Y", (3 + X * Y).toString())
        Assertions.assertEquals("3.0 + X * Y", (X * Y + 3).toString())
        Assertions.assertEquals("X * Y + T * alpha", (X * Y + T * alpha).toString())
        Assertions.assertEquals("cos(T)", (cos(T)).toString())
        Assertions.assertEquals("(3.0 + X) * Y", ((3 + X) * Y).toString())
    }

    @Test
    fun equalsTest()
    {
        Assertions.assertTrue(X + Y == Y + X)
        Assertions.assertTrue(X * Y == Y * X)
        val f1: MathFunction<*> = X * Y
        val f2: MathFunction<*> = X + Y
        Assertions.assertFalse(f1 == f2)
    }

    @Test
    fun simplifyMaxTest()
    {
        Assertions.assertEquals(X, (2 * X - X).simplifyMax { println(it) })
        Assertions.assertEquals(X + Y, (Y + 2 * X - X).simplifyMax { println(it) })
        Assertions.assertEquals(ZERO,
                                (X + Y + Z + T + alpha + epsilon - X - Y - Z - T - alpha - epsilon).simplifyMax {
                                    println(it)
                                })
        Assertions.assertEquals(ZERO,
                                (X + Y + Z + T + alpha + epsilon - epsilon - Y - alpha - T - Z - X).simplifyMax {
                                    println(it)
                                })
        Assertions.assertEquals(ONE, (cos(X + T) * ((X + 1 - X) * cos(T + X)) + sin(X + T) * sin(
            T + X)).simplifyMax { println(it) })
    }
}