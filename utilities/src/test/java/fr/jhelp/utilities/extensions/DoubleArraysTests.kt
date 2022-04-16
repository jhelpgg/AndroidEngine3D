/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.utilities.extensions

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DoubleArraysTests
{
    @Test
    fun stringTests()
    {
        Assertions.assertEquals("[]", DoubleArray(0).string())
        val doubleArray = doubleArrayOf(42.0, 73.0, 666.0, 85.0, 44.0, 1974.0)
        Assertions.assertEquals("[42.0, 73.0, 666.0, 85.0, 44.0, 1974.0]",
                                doubleArray.string())
        Assertions.assertEquals("(42.0;73.0;666.0;85.0;44.0;1974.0}",
                                doubleArray.string(header = "(",
                                                   separator = ";",
                                                   footer = "}"))
        Assertions.assertEquals("[42.0, 73.0, 666.0, ...]",
                                doubleArray.string(limitSize = 3))
        Assertions.assertEquals("[42.0, 73.0, ..., 1974.0]",
                                doubleArray.string(limitSize = 3,
                                                   moreAtEnd = false))
    }
}
