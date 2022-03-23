package fr.jhelp.utilities.text

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class TextTests
{
    @Test
    fun removeWhitCharactersTest()
    {
        Assertions.assertEquals("WithoutWhiteSpaces",
                                "\t \n Without    \t White   \t \r\n  Spaces \n ".removeWhitCharacters())
    }
}