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

class ArraysTests
{
    @Test
    fun stringTests()
    {
        Assertions.assertEquals("[]", emptyArray<Any>().string())
        val stringArray = arrayOf("42", "answer", "73", "magic", "666", "beast")
        Assertions.assertEquals("[42, answer, 73, magic, 666, beast]",
                                stringArray.string())
        Assertions.assertEquals("(42;answer;73;magic;666;beast}",
                                stringArray.string(header = "(",
                                                   separator = ";",
                                                   footer = "}"))
        Assertions.assertEquals("[42, answer, 73, ...]",
                                stringArray.string(limitSize = 3))
        Assertions.assertEquals("[42, answer, ..., beast]",
                                stringArray.string(limitSize = 3,
                                                   moreAtEnd = false))
    }
}
