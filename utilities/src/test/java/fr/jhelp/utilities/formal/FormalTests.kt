/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.utilities.formal

import org.junit.Assert
import org.junit.Test

class FormalTests
{
    @Test
    fun toStringTest()
    {
        Assert.assertEquals("3.0 + X", (3 + X).toString())
        Assert.assertEquals("3.0 + X + Y", (3 + X + Y).toString())
        Assert.assertEquals("3.0 + X * Y", (3 + X * Y).toString())
        Assert.assertEquals("3.0 + X * Y", (X * Y + 3).toString())
        Assert.assertEquals("X * Y + T * alpha", (X * Y + T * alpha).toString())
        Assert.assertEquals("cos(T)", (cos(T)).toString())
        Assert.assertEquals("(3.0 + X) * Y", ((3 + X) * Y).toString())
    }

    @Test
    fun equalsTest()
    {
        Assert.assertTrue(X + Y == Y + X)
        Assert.assertTrue(X * Y == Y * X)
        val f1: MathFunction<*> = X * Y
        val f2: MathFunction<*> = X + Y
        Assert.assertFalse(f1 == f2)
    }

    @Test
    fun simplifyMaxTest()
    {
        Assert.assertEquals(X, (2 * X - X).simplifyMax { println(it) })
        Assert.assertEquals(X + Y, (Y + 2 * X - X).simplifyMax { println(it) })
        Assert.assertEquals(ZERO, (X + Y + Z + T + alpha + epsilon - X - Y - Z - T - alpha - epsilon).simplifyMax { println(it) })
        Assert.assertEquals(ZERO, (X + Y + Z + T + alpha + epsilon - epsilon - Y - alpha - T - Z - X).simplifyMax { println(it) })
        Assert.assertEquals(ONE, (cos(X + T) * ((X + 1 - X) * cos(T + X)) + sin(X + T) * sin(T + X)).simplifyMax { println(it) })
    }
}