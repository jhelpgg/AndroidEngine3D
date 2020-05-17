package fr.jhelp.engine

import org.junit.Test

import org.junit.Assert.*

data class P(val x:Int, val y:Int,val z:Int)
{
    operator fun minus(p:P) =
        P(this.x-p.x, this.y-p.y, 1)
}

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest
{
    @Test
    fun addition_isCorrect()
    {
        assertEquals(4, 2 + 2)
    }

    fun op(p1:P, p2:P) =
        P(p1.y * p2.z - p1.z * p2.y,
          p1.z * p2.x - p1.x * p2.z,
          p1.x * p2.y - p1.y * p2.x)

    @Test
    fun test()
    {
        val a = P(2,0, 1)
        val b = P(0,3,1)
        val c = P(4,4,1)

        println("a=$a ; b=$b ; c=$c")

        val ab = b - a
        val ac = c - a

        println("ab=$ab ; ac=$ac")
        println("ab * ac =${op(ab,ac)}")

        val pOut = P(1,1,1)
        println("pOut=$pOut")
        val apOut = pOut - a
        println("apOut=$apOut")
        println("apOut * ab =${op(apOut, ab)}")
        println("apOut * ac =${op(apOut, ac)}")

        val pIn = P(2,2,1)
        println("pIn=$pIn")
        val apIn = pIn - a
        println("apIn=$apIn")
        println("apIn * ab =${op(apIn, ab)}")
        println("apIn * ac =${op(apIn, ac)}")
    }
}
