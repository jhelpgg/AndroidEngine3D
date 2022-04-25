/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.lists

import fr.jhelp.utilities.extensions.int
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.math.min

class TransformAndFilter
{
    @Test
    fun transformTest()
    {
        val list = listOf<String>("42", "73", "666")
        val iterator = list.transform { string -> string.int(0) }.iterator()
        Assertions.assertTrue(iterator.hasNext())
        Assertions.assertEquals(42, iterator.next())
        Assertions.assertTrue(iterator.hasNext())
        Assertions.assertEquals(73, iterator.next())
        Assertions.assertTrue(iterator.hasNext())
        Assertions.assertEquals(666, iterator.next())
        Assertions.assertFalse(iterator.hasNext())

        try
        {
            iterator.next()
            Assertions.fail<Unit>("Should throw since no more elements")
        }
        catch (_: NoSuchElementException)
        {
            // That's what we expect
        }
    }

    @Test
    fun smartFilterTest()
    {
        val list = listOf<String>("42", "73", "666", "48", "85", "44")
        val iterator = list.smartFilter { string -> string.startsWith('4') }.iterator()
        Assertions.assertTrue(iterator.hasNext())
        Assertions.assertEquals("42", iterator.next())
        Assertions.assertTrue(iterator.hasNext())
        Assertions.assertEquals("48", iterator.next())
        Assertions.assertTrue(iterator.hasNext())
        Assertions.assertEquals("44", iterator.next())
        Assertions.assertFalse(iterator.hasNext())

        try
        {
            iterator.next()
            Assertions.fail<Unit>("Should throw since no more elements")
        }
        catch (_: NoSuchElementException)
        {
            // That's what we expect
        }
    }


    @Test
    fun performanceTests()
    {
        val list = ArrayList<Int>()

        for (value in 0 until 9_000_000)
        {
            list.add(value)
        }

        // To be fair we load classes in memory before doing the test
        list.map { value -> value }.filter { true }
        list.transform { value -> value }.smartFilter { true }
        var startTime = System.nanoTime()
        var timeKotlin = System.nanoTime() - startTime
        var timeJHelp = System.nanoTime() - startTime
        val runtime = Runtime.getRuntime()
        var memoryBeforeKotlin = runtime.freeMemory()
        var memoryAfterKotlin = runtime.freeMemory()
        var memoryKotlin = min(0, runtime.freeMemory())
        var memoryBeforeJHelp = runtime.freeMemory()
        var memoryAfterJHelp = runtime.freeMemory()
        var memoryJHelp = min(0, runtime.freeMemory())
        var k = 0


        // Test map / transform alone

        startTime = System.nanoTime()
        for (element in list.map { value -> value })
        {
            k = element
        }
        timeKotlin = System.nanoTime() - startTime

        startTime = System.nanoTime()
        for (element in list.transform { value -> value })
        {
            k = element
        }
        timeJHelp = System.nanoTime() - startTime

        println(
            "Map only Kotlin=$timeKotlin | JHelp=$timeJHelp : JHelp is about  ${timeKotlin.toDouble() / timeJHelp} times faster than Kotlin")
        Assertions.assertTrue(timeJHelp * 5L < timeKotlin,
                              "Map only Kotlin=$timeKotlin | JHelp=$timeJHelp : JHelp is about  ${timeKotlin / timeJHelp} times faster than Kotlin")

        runtime.gc()
        runtime.gc()
        runtime.gc()

        memoryKotlin = Long.MAX_VALUE
        memoryBeforeKotlin = runtime.freeMemory()
        for (element in list.map { value -> value })
        {
            memoryKotlin = min(memoryKotlin, runtime.freeMemory())
        }

        runtime.gc()
        runtime.gc()
        runtime.gc()

        memoryJHelp = Long.MAX_VALUE
        memoryBeforeJHelp = runtime.freeMemory()
        for (element in list.transform { value -> value })
        {
            memoryJHelp = min(memoryJHelp, runtime.freeMemory())
        }

        memoryKotlin = memoryBeforeKotlin - memoryKotlin
        memoryJHelp = memoryBeforeJHelp - memoryJHelp

        println(
            "Map only memory Kotlin=$memoryKotlin | JHelp=$memoryJHelp : JHelp is about  take ${memoryKotlin.toDouble() / memoryJHelp} times less memory than Kotlin")
        Assertions.assertTrue(memoryJHelp * 40L < memoryKotlin,
                              "Map only memory Kotlin=$memoryKotlin | JHelp=$memoryJHelp : JHelp is about  take ${memoryKotlin / memoryJHelp} times less memory than Kotlin")

        // Map + filter
        startTime = System.nanoTime()
        for (element in list.map { value -> value }.filter { value -> value % 2 == 0 })
        {
            k = element
        }
        timeKotlin = System.nanoTime() - startTime

        startTime = System.nanoTime()
        for (element in list.transform { value -> value }.smartFilter { value -> value % 2 == 0 })
        {
            k = element
        }
        timeJHelp = System.nanoTime() - startTime

        println(
            "Map + filter Kotlin=$timeKotlin | JHelp=$timeJHelp : JHelp is about  ${timeKotlin.toDouble() / timeJHelp} times faster than Kotlin")
        Assertions.assertTrue(timeJHelp * 2L < timeKotlin,
                              "Map + filter Kotlin=$timeKotlin | JHelp=$timeJHelp : JHelp is about  ${timeKotlin / timeJHelp} times faster than Kotlin")

        runtime.gc()
        runtime.gc()
        runtime.gc()

        memoryKotlin = Long.MAX_VALUE
        memoryBeforeKotlin = runtime.freeMemory()
        for (element in list.map { value -> value }.filter { value -> value % 2 == 0 })
        {
            memoryKotlin = min(memoryKotlin, runtime.freeMemory())
        }

        runtime.gc()
        runtime.gc()
        runtime.gc()

        memoryJHelp = Long.MAX_VALUE
        memoryBeforeJHelp = runtime.freeMemory()
        for (element in list.transform { value -> value }.smartFilter { value -> value % 2 == 0 })
        {
            memoryJHelp = min(memoryJHelp, runtime.freeMemory())
        }

        memoryKotlin = memoryBeforeKotlin - memoryKotlin
        memoryJHelp = memoryBeforeJHelp - memoryJHelp

        println(
            "Map + filter memory Kotlin=$memoryKotlin | JHelp=$memoryJHelp : JHelp is about  take ${memoryKotlin.toDouble() / memoryJHelp} times less memory than Kotlin")
        Assertions.assertTrue(memoryJHelp * 3L < memoryKotlin,
                              "Map + filter memory Kotlin=$memoryKotlin | JHelp=$memoryJHelp : JHelp is about  take ${memoryKotlin / memoryJHelp} times less memory than Kotlin")

        // Filter + map
        startTime = System.nanoTime()
        for (element in list.filter { value -> value % 2 == 0 }.map { value -> value })
        {
            k = element
        }
        timeKotlin = System.nanoTime() - startTime

        startTime = System.nanoTime()
        for (element in list.smartFilter { value -> value % 2 == 0 }.transform { value -> value })
        {
            k = element
        }
        timeJHelp = System.nanoTime() - startTime

        println(
            "Filter + map Kotlin=$timeKotlin | JHelp=$timeJHelp : JHelp is about  ${timeKotlin.toDouble() / timeJHelp} times faster than Kotlin")
        Assertions.assertTrue(timeJHelp < timeKotlin,
                              "Filter + map Kotlin=$timeKotlin | JHelp=$timeJHelp : JHelp is about  ${timeKotlin / timeJHelp} times faster than Kotlin")

        runtime.gc()
        runtime.gc()
        runtime.gc()

        memoryKotlin = Long.MAX_VALUE
        memoryBeforeKotlin = runtime.freeMemory()
        for (element in list.filter { value -> value % 2 == 0 }.map { value -> value })
        {
            memoryKotlin = min(memoryKotlin, runtime.freeMemory())
        }

        runtime.gc()
        runtime.gc()
        runtime.gc()

        memoryJHelp = Long.MAX_VALUE
        memoryBeforeJHelp = runtime.freeMemory()
        for (element in list.smartFilter { value -> value % 2 == 0 }.transform { value -> value })
        {
            memoryJHelp = min(memoryJHelp, runtime.freeMemory())
        }

        memoryKotlin = memoryBeforeKotlin - memoryKotlin
        memoryJHelp = memoryBeforeJHelp - memoryJHelp

        println(
            "Filter + map only memory Kotlin=$memoryKotlin | JHelp=$memoryJHelp : JHelp is about  take ${memoryKotlin.toDouble() / memoryJHelp} times less memory than Kotlin")
        Assertions.assertTrue(memoryJHelp * 2L < memoryKotlin,
                              "Filter + map only memory Kotlin=$memoryKotlin | JHelp=$memoryJHelp : JHelp is about  take ${memoryKotlin / memoryJHelp} times less memory than Kotlin")
    }
}
