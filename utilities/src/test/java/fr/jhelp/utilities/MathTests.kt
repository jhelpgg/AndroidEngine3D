package fr.jhelp.utilities

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.math.PI

class MathTests
{
    @Test
    fun log2Test()
    {
        Assertions.assertEquals(0, log2(1))
        Assertions.assertEquals(1, log2(2))
        Assertions.assertEquals(1, log2(3))
        Assertions.assertEquals(2, log2(4))
        Assertions.assertEquals(2, log2(5))
        Assertions.assertEquals(2, log2(6))
        Assertions.assertEquals(2, log2(7))
        Assertions.assertEquals(3, log2(8))
        Assertions.assertEquals(3, log2(9))
        Assertions.assertEquals(3, log2(10))
        Assertions.assertEquals(3, log2(15))
        Assertions.assertEquals(4, log2(16))
        Assertions.assertEquals(4, log2(17))
        Assertions.assertEquals(4, log2(31))
        Assertions.assertEquals(5, log2(32))
        Assertions.assertEquals(5, log2(33))
        Assertions.assertEquals(5, log2(63))
        Assertions.assertEquals(6, log2(64))
        Assertions.assertEquals(6, log2(65))
        Assertions.assertEquals(6, log2(127))
        Assertions.assertEquals(7, log2(128))
        Assertions.assertEquals(7, log2(129))
        Assertions.assertEquals(7, log2(255))
        Assertions.assertEquals(8, log2(256))
        Assertions.assertEquals(8, log2(257))
        Assertions.assertEquals(8, log2(511))
        Assertions.assertEquals(9, log2(512))
        Assertions.assertEquals(9, log2(513))
        Assertions.assertEquals(9, log2(1023))
        Assertions.assertEquals(10, log2(1024))
        Assertions.assertEquals(11, log2(2048))
    }

    @Test
    fun moduloIntervalTest()
    {
        Assertions.assertEquals(5, moduloInterval(5, 3, 10))
        Assertions.assertEquals(5, moduloInterval(13, 3, 10))
        Assertions.assertEquals(5, moduloInterval(21, 3, 10))
        Assertions.assertEquals(5, moduloInterval(-3, 3, 10))
        Assertions.assertEquals(5, moduloInterval(-11, 3, 10))
        Assertions.assertEquals(0, moduloInterval(5, -2, 2))
        Assertions.assertEquals(0, moduloInterval(10, -2, 2))
        Assertions.assertEquals(0, moduloInterval(-5, -2, 2))
        //
        Assertions.assertEquals(0.0, moduloInterval(0.0, -PI, PI), EPSILON)
        Assertions.assertEquals(-PI, moduloInterval(-PI, -PI, PI), EPSILON)
        Assertions.assertEquals(-PI, moduloInterval(PI, -PI, PI), EPSILON)
        Assertions.assertEquals(0.0, moduloInterval(720.0, 0.0, 360.0), EPSILON)
    }
}