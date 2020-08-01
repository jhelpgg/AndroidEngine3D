package fr.jhelp.utilities

import fr.jhelp.utilities.formal.T
import fr.jhelp.utilities.formal.X
import fr.jhelp.utilities.formal.cos
import fr.jhelp.utilities.formal.minus
import fr.jhelp.utilities.formal.plus
import fr.jhelp.utilities.formal.times
import org.junit.Assert
import org.junit.Test
import kotlin.math.PI

class MathTests
{
    @Test
    fun log2Test()
    {
        Assert.assertEquals(0, log2(1))
        Assert.assertEquals(1, log2(2))
        Assert.assertEquals(1, log2(3))
        Assert.assertEquals(2, log2(4))
        Assert.assertEquals(2, log2(5))
        Assert.assertEquals(2, log2(6))
        Assert.assertEquals(2, log2(7))
        Assert.assertEquals(3, log2(8))
        Assert.assertEquals(3, log2(9))
        Assert.assertEquals(3, log2(10))
        Assert.assertEquals(3, log2(15))
        Assert.assertEquals(4, log2(16))
        Assert.assertEquals(4, log2(17))
        Assert.assertEquals(4, log2(31))
        Assert.assertEquals(5, log2(32))
        Assert.assertEquals(5, log2(33))
        Assert.assertEquals(5, log2(63))
        Assert.assertEquals(6, log2(64))
        Assert.assertEquals(6, log2(65))
        Assert.assertEquals(6, log2(127))
        Assert.assertEquals(7, log2(128))
        Assert.assertEquals(7, log2(129))
        Assert.assertEquals(7, log2(255))
        Assert.assertEquals(8, log2(256))
        Assert.assertEquals(8, log2(257))
        Assert.assertEquals(8, log2(511))
        Assert.assertEquals(9, log2(512))
        Assert.assertEquals(9, log2(513))
        Assert.assertEquals(9, log2(1023))
        Assert.assertEquals(10, log2(1024))
        Assert.assertEquals(11, log2(2048))
    }

    @Test
    fun moduloIntervalTest()
    {
        Assert.assertEquals(5, moduloInterval(5, 3, 10))
        Assert.assertEquals(5, moduloInterval(13, 3, 10))
        Assert.assertEquals(5, moduloInterval(21, 3, 10))
        Assert.assertEquals(5, moduloInterval(-3, 3, 10))
        Assert.assertEquals(5, moduloInterval(-11, 3, 10))
        Assert.assertEquals(0, moduloInterval(5, -2, 2))
        Assert.assertEquals(0, moduloInterval(10, -2, 2))
        Assert.assertEquals(0, moduloInterval(-5, -2, 2))
        //
        Assert.assertEquals(0.0, moduloInterval(0.0, -PI, PI), EPSILON)
        Assert.assertEquals(-PI, moduloInterval(-PI, -PI, PI), EPSILON)
        Assert.assertEquals(-PI, moduloInterval(PI, -PI, PI), EPSILON)
        Assert.assertEquals(0.0, moduloInterval(720.0, 0.0, 360.0), EPSILON)
    }
}