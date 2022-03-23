/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.utilities.regex

import fr.jhelp.utilities.text.interval
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class RegexTests
{
    @Test
    fun regexText()
    {
        val regex = "Hello".regexText()
        Assertions.assertTrue(regex.matches("Hello"))
        Assertions.assertFalse(regex.matches("World"))
    }

    @Test
    fun plus()
    {
        val regex = "Hello".regexText() + ANY + "World"
        Assertions.assertTrue(regex.matches("Hello World"))
        Assertions.assertTrue(regex.matches("Hello-World"))
        Assertions.assertTrue(regex.matches("Hello*World"))
        Assertions.assertTrue(regex.matches("Hello/World"))
        Assertions.assertTrue(regex.matches("Hello_World"))
        Assertions.assertFalse(regex.matches("HelloWorld"))
        Assertions.assertFalse(regex.matches("Hello"))
        Assertions.assertFalse(regex.matches("World"))
    }

    @Test
    fun or()
    {
        val regex = "Hello".regexText() OR "World".regexText()
        Assertions.assertFalse(regex.matches("HelloWorld"))
        Assertions.assertFalse(regex.matches("Hello World"))
        Assertions.assertTrue(regex.matches("Hello"))
        Assertions.assertTrue(regex.matches("World"))
    }

    @Test
    fun regexIn()
    {
        val regex = interval('C', 'R').regexIn()
        Assertions.assertTrue(regex.matches("F"))
        Assertions.assertTrue(regex.matches("K"))
        Assertions.assertTrue(regex.matches("J"))
        Assertions.assertTrue(regex.matches("C"))
        Assertions.assertTrue(regex.matches("R"))
        Assertions.assertFalse(regex.matches("A"))
        Assertions.assertFalse(regex.matches("B"))
        Assertions.assertFalse(regex.matches("S"))
        Assertions.assertFalse(regex.matches("Y"))
        Assertions.assertEquals("-AVA -S ---- !", regex.replaceAll("JAVA IS COOL !", "-"))
    }

    @Test
    fun regexOut()
    {
        val regex = interval('C', 'R').regexOut()
        Assertions.assertFalse(regex.matches("F"))
        Assertions.assertFalse(regex.matches("K"))
        Assertions.assertFalse(regex.matches("J"))
        Assertions.assertFalse(regex.matches("C"))
        Assertions.assertFalse(regex.matches("R"))
        Assertions.assertTrue(regex.matches("A"))
        Assertions.assertTrue(regex.matches("B"))
        Assertions.assertTrue(regex.matches("S"))
        Assertions.assertTrue(regex.matches("Y"))
        Assertions.assertEquals("J----I--COOL--", regex.replaceAll("JAVA IS COOL !", "-"))
    }

    @Test
    fun regexCharArray()
    {
        val regex = charArrayOf('A', 'R', 'T').regex()
        Assertions.assertTrue(regex.matches("A"))
        Assertions.assertTrue(regex.matches("R"))
        Assertions.assertTrue(regex.matches("T"))
        Assertions.assertFalse(regex.matches("F"))
        Assertions.assertFalse(regex.matches("K"))
        Assertions.assertFalse(regex.matches("J"))
        Assertions.assertEquals("-HE H--- BOUNCE", regex.replaceAll("THE HART BOUNCE", "-"))
    }

    @Test
    fun regexOutCharArray()
    {
        val regex = charArrayOf('A', 'R', 'T').regexOut()
        Assertions.assertFalse(regex.matches("A"))
        Assertions.assertFalse(regex.matches("R"))
        Assertions.assertFalse(regex.matches("T"))
        Assertions.assertTrue(regex.matches("F"))
        Assertions.assertTrue(regex.matches("K"))
        Assertions.assertTrue(regex.matches("J"))
        Assertions.assertEquals("T----ART-------", regex.replaceAll("THE HART BOUNCE", "-"))
    }

    @Test
    fun regexChar()
    {
        val regex = 'E'.regex()
        Assertions.assertTrue(regex.matches("E"))
        Assertions.assertFalse(regex.matches("F"))
        Assertions.assertFalse(regex.matches("K"))
        Assertions.assertFalse(regex.matches("J"))
        Assertions.assertEquals("-L-PHANT", regex.replaceAll("ELEPHANT", "-"))
    }

    @Test
    fun regexOutChar()
    {
        val regex = 'E'.regexOut()
        Assertions.assertFalse(regex.matches("E"))
        Assertions.assertTrue(regex.matches("F"))
        Assertions.assertTrue(regex.matches("K"))
        Assertions.assertTrue(regex.matches("J"))
        Assertions.assertEquals("E-E-----", regex.replaceAll("ELEPHANT", "-"))
    }

    @Test
    fun zeroOrMore()
    {
        val regex = 'A'.regex().zeroOrMore()
        Assertions.assertTrue(regex.matches(""))
        Assertions.assertTrue(regex.matches("A"))
        Assertions.assertTrue(regex.matches("AA"))
        Assertions.assertTrue(regex.matches("AAA"))
        Assertions.assertTrue(regex.matches("AAAAA"))
        Assertions.assertFalse(regex.matches("E"))
        Assertions.assertFalse(regex.matches("AE"))
    }

    @Test
    fun zeroOrOne()
    {
        val regex = 'A'.regex().zeroOrOne()
        Assertions.assertTrue(regex.matches(""))
        Assertions.assertTrue(regex.matches("A"))
        Assertions.assertFalse(regex.matches("AA"))
        Assertions.assertFalse(regex.matches("AAA"))
        Assertions.assertFalse(regex.matches("AAAAA"))
        Assertions.assertFalse(regex.matches("E"))
        Assertions.assertFalse(regex.matches("AE"))
    }

    @Test
    fun oneOrMore()
    {
        val regex = 'A'.regex().oneOrMore()
        Assertions.assertFalse(regex.matches(""))
        Assertions.assertTrue(regex.matches("A"))
        Assertions.assertTrue(regex.matches("AA"))
        Assertions.assertTrue(regex.matches("AAA"))
        Assertions.assertTrue(regex.matches("AAAAA"))
        Assertions.assertFalse(regex.matches("E"))
        Assertions.assertFalse(regex.matches("AE"))
    }

    @Test
    fun atLeast()
    {
        val regex = 'A'.regex().atLeast(3)
        Assertions.assertFalse(regex.matches(""))
        Assertions.assertFalse(regex.matches("A"))
        Assertions.assertFalse(regex.matches("AA"))
        Assertions.assertTrue(regex.matches("AAA"))
        Assertions.assertTrue(regex.matches("AAAAA"))
        Assertions.assertFalse(regex.matches("E"))
        Assertions.assertFalse(regex.matches("AE"))
    }

    @Test
    fun exactly()
    {
        val regex = 'A'.regex().exactly(3)
        Assertions.assertFalse(regex.matches(""))
        Assertions.assertFalse(regex.matches("A"))
        Assertions.assertFalse(regex.matches("AA"))
        Assertions.assertTrue(regex.matches("AAA"))
        Assertions.assertFalse(regex.matches("AAAAA"))
        Assertions.assertFalse(regex.matches("E"))
        Assertions.assertFalse(regex.matches("AE"))
    }


    @Test
    fun between()
    {
        val regex = 'A'.regex().between(1, 3)
        Assertions.assertFalse(regex.matches(""))
        Assertions.assertTrue(regex.matches("A"))
        Assertions.assertTrue(regex.matches("AA"))
        Assertions.assertTrue(regex.matches("AAA"))
        Assertions.assertFalse(regex.matches("AAAAA"))
        Assertions.assertFalse(regex.matches("E"))
        Assertions.assertFalse(regex.matches("AE"))
    }

    @Test
    fun group()
    {
        val headerFooter = ('{'.regex() + ANY.zeroOrMore() + '}'.regex()).group()
        val something = ANY.oneOrMore().group()
        val regex = headerFooter + something + headerFooter.same()
        Assertions.assertTrue(regex.matches("{42}The answer{42}"))
        Assertions.assertFalse(regex.matches("{24}Not same{37}"))
        Assertions.assertEquals("-*-{24}Not same{37}-*-", regex.replaceAll(
            "{42}The answer{42}{24}Not same{37}{73}Magic number{73}", "-*-"))
        val replacer =
            regex.replacer()
                .append("[")
                .append(something)
                .append("]:")
                .append(headerFooter)
        Assertions.assertEquals("[The answer]:{42}", replacer.replaceAll("{42}The answer{42}"))
        Assertions.assertEquals("[The answer]:{42}{24}Not same{37}[Magic number]:{73}",
                                replacer.replaceAll(
                                    "{42}The answer{42}{24}Not same{37}{73}Magic number{73}"))
    }
}