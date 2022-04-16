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

class LongArraysTests
{
    @Test
    fun stringTests()
    {
        Assertions.assertEquals("[]", LongArray(0).string())
        val intArray = longArrayOf(42, 73, 666, 85, 44, 1974)
        Assertions.assertEquals("[42, 73, 666, 85, 44, 1974]",
                                intArray.string())
        Assertions.assertEquals("(42;73;666;85;44;1974}",
                                intArray.string(header = "(",
                                                separator = ";",
                                                footer = "}"))
        Assertions.assertEquals("[42, 73, 666, ...]",
                                intArray.string(limitSize = 3))
        Assertions.assertEquals("[42, 73, ..., 1974]",
                                intArray.string(limitSize = 3,
                                                moreAtEnd = false))
    }
}
