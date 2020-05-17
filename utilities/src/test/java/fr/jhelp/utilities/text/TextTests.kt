package fr.jhelp.utilities.text

import org.junit.Assert
import org.junit.Test

class TextTests
{
    @Test
    fun removeWhitCharactersTest()
    {
        Assert.assertEquals("WithoutWhiteSpaces", "\t \n Without    \t White   \t \r\n  Spaces \n ".removeWhitCharacters())
    }
}