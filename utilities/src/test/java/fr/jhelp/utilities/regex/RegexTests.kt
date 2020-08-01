/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.utilities.regex

import fr.jhelp.utilities.text.interval
import org.junit.Assert
import org.junit.Test

class RegexTests
{
    @Test
    fun regexText()
    {
        val regex = "Hello".regexText()
        Assert.assertTrue(regex.matches("Hello"))
        Assert.assertFalse(regex.matches("World"))
    }

    @Test
    fun plus()
    {
        val regex = "Hello".regexText() + ANY + "World"
        Assert.assertTrue(regex.matches("Hello World"))
        Assert.assertTrue(regex.matches("Hello-World"))
        Assert.assertTrue(regex.matches("Hello*World"))
        Assert.assertTrue(regex.matches("Hello/World"))
        Assert.assertTrue(regex.matches("Hello_World"))
        Assert.assertFalse(regex.matches("HelloWorld"))
        Assert.assertFalse(regex.matches("Hello"))
        Assert.assertFalse(regex.matches("World"))
    }

    @Test
    fun or()
    {
        val regex = "Hello".regexText() OR "World".regexText()
        Assert.assertFalse(regex.matches("HelloWorld"))
        Assert.assertFalse(regex.matches("Hello World"))
        Assert.assertTrue(regex.matches("Hello"))
        Assert.assertTrue(regex.matches("World"))
    }

    @Test
    fun regexIn()
    {
        val regex = interval('C', 'R').regexIn()
        Assert.assertTrue(regex.matches("F"))
        Assert.assertTrue(regex.matches("K"))
        Assert.assertTrue(regex.matches("J"))
        Assert.assertTrue(regex.matches("C"))
        Assert.assertTrue(regex.matches("R"))
        Assert.assertFalse(regex.matches("A"))
        Assert.assertFalse(regex.matches("B"))
        Assert.assertFalse(regex.matches("S"))
        Assert.assertFalse(regex.matches("Y"))
        Assert.assertEquals("-AVA -S ---- !", regex.replaceAll("JAVA IS COOL !", "-"))
    }

    @Test
    fun regexOut()
    {
        val regex = interval('C', 'R').regexOut()
        Assert.assertFalse(regex.matches("F"))
        Assert.assertFalse(regex.matches("K"))
        Assert.assertFalse(regex.matches("J"))
        Assert.assertFalse(regex.matches("C"))
        Assert.assertFalse(regex.matches("R"))
        Assert.assertTrue(regex.matches("A"))
        Assert.assertTrue(regex.matches("B"))
        Assert.assertTrue(regex.matches("S"))
        Assert.assertTrue(regex.matches("Y"))
        Assert.assertEquals("J----I--COOL--", regex.replaceAll("JAVA IS COOL !", "-"))
    }

    @Test
    fun regexCharArray()
    {
        val regex = charArrayOf('A', 'R', 'T').regex()
        Assert.assertTrue(regex.matches("A"))
        Assert.assertTrue(regex.matches("R"))
        Assert.assertTrue(regex.matches("T"))
        Assert.assertFalse(regex.matches("F"))
        Assert.assertFalse(regex.matches("K"))
        Assert.assertFalse(regex.matches("J"))
        Assert.assertEquals("-HE H--- BOUNCE", regex.replaceAll("THE HART BOUNCE", "-"))
    }

    @Test
    fun regexOutCharArray()
    {
        val regex = charArrayOf('A', 'R', 'T').regexOut()
        Assert.assertFalse(regex.matches("A"))
        Assert.assertFalse(regex.matches("R"))
        Assert.assertFalse(regex.matches("T"))
        Assert.assertTrue(regex.matches("F"))
        Assert.assertTrue(regex.matches("K"))
        Assert.assertTrue(regex.matches("J"))
        Assert.assertEquals("T----ART-------", regex.replaceAll("THE HART BOUNCE", "-"))
    }

    @Test
    fun regexChar()
    {
        val regex = 'E'.regex()
        Assert.assertTrue(regex.matches("E"))
        Assert.assertFalse(regex.matches("F"))
        Assert.assertFalse(regex.matches("K"))
        Assert.assertFalse(regex.matches("J"))
        Assert.assertEquals("-L-PHANT", regex.replaceAll("ELEPHANT", "-"))
    }

    @Test
    fun regexOutChar()
    {
        val regex = 'E'.regexOut()
        Assert.assertFalse(regex.matches("E"))
        Assert.assertTrue(regex.matches("F"))
        Assert.assertTrue(regex.matches("K"))
        Assert.assertTrue(regex.matches("J"))
        Assert.assertEquals("E-E-----", regex.replaceAll("ELEPHANT", "-"))
    }

    @Test
    fun zeroOrMore()
    {
        val regex = 'A'.regex().zeroOrMore()
        Assert.assertTrue(regex.matches(""))
        Assert.assertTrue(regex.matches("A"))
        Assert.assertTrue(regex.matches("AA"))
        Assert.assertTrue(regex.matches("AAA"))
        Assert.assertTrue(regex.matches("AAAAA"))
        Assert.assertFalse(regex.matches("E"))
        Assert.assertFalse(regex.matches("AE"))
    }

    @Test
    fun zeroOrOne()
    {
        val regex = 'A'.regex().zeroOrOne()
        Assert.assertTrue(regex.matches(""))
        Assert.assertTrue(regex.matches("A"))
        Assert.assertFalse(regex.matches("AA"))
        Assert.assertFalse(regex.matches("AAA"))
        Assert.assertFalse(regex.matches("AAAAA"))
        Assert.assertFalse(regex.matches("E"))
        Assert.assertFalse(regex.matches("AE"))
    }

    @Test
    fun oneOrMore()
    {
        val regex = 'A'.regex().oneOrMore()
        Assert.assertFalse(regex.matches(""))
        Assert.assertTrue(regex.matches("A"))
        Assert.assertTrue(regex.matches("AA"))
        Assert.assertTrue(regex.matches("AAA"))
        Assert.assertTrue(regex.matches("AAAAA"))
        Assert.assertFalse(regex.matches("E"))
        Assert.assertFalse(regex.matches("AE"))
    }

    @Test
    fun atLeast()
    {
        val regex = 'A'.regex().atLeast(3)
        Assert.assertFalse(regex.matches(""))
        Assert.assertFalse(regex.matches("A"))
        Assert.assertFalse(regex.matches("AA"))
        Assert.assertTrue(regex.matches("AAA"))
        Assert.assertTrue(regex.matches("AAAAA"))
        Assert.assertFalse(regex.matches("E"))
        Assert.assertFalse(regex.matches("AE"))
    }

    @Test
    fun exactly()
    {
        val regex = 'A'.regex().exactly(3)
        Assert.assertFalse(regex.matches(""))
        Assert.assertFalse(regex.matches("A"))
        Assert.assertFalse(regex.matches("AA"))
        Assert.assertTrue(regex.matches("AAA"))
        Assert.assertFalse(regex.matches("AAAAA"))
        Assert.assertFalse(regex.matches("E"))
        Assert.assertFalse(regex.matches("AE"))
    }


    @Test
    fun between()
    {
        val regex = 'A'.regex().between(1, 3)
        Assert.assertFalse(regex.matches(""))
        Assert.assertTrue(regex.matches("A"))
        Assert.assertTrue(regex.matches("AA"))
        Assert.assertTrue(regex.matches("AAA"))
        Assert.assertFalse(regex.matches("AAAAA"))
        Assert.assertFalse(regex.matches("E"))
        Assert.assertFalse(regex.matches("AE"))
    }

    @Test
    fun group()
    {
        val headerFooter = ('{'.regex() + ANY.zeroOrMore() + '}'.regex()).group()
        val something = ANY.oneOrMore().group()
        val regex = headerFooter + something + headerFooter.same()
        Assert.assertTrue(regex.matches("{42}The answer{42}"))
        Assert.assertFalse(regex.matches("{24}Not same{37}"))
        Assert.assertEquals("-*-{24}Not same{37}-*-", regex.replaceAll(
            "{42}The answer{42}{24}Not same{37}{73}Magic number{73}", "-*-"))
        val replacer =
            regex.replacer()
                .append("[")
                .append(something)
                .append("]:")
                .append(headerFooter)
        Assert.assertEquals("[The answer]:{42}", replacer.replaceAll("{42}The answer{42}"))
        Assert.assertEquals("[The answer]:{42}{24}Not same{37}[Magic number]:{73}",
                            replacer.replaceAll(
                                "{42}The answer{42}{24}Not same{37}{73}Magic number{73}"))
    }
}