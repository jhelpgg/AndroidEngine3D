package fr.jhelp.utilities.text

import org.junit.Assert
import org.junit.Test

class BasicCharactersIntervalTests
{
    @Test
    fun formatTest()
    {
        Assert.assertEquals("[]", EMPTY_CHARACTERS_INTERVAL.format())
        Assert.assertEquals("[]", interval('Y', 'A').format())
        Assert.assertEquals("{U}", interval('U', 'U').format())
        Assert.assertEquals("[C, R]", interval('C', 'R').format())
        Assert.assertEquals("{\\u0020}", interval(' ', ' ').format(hexadecimalForm=true))
        Assert.assertEquals("<C-R>", interval('C', 'R').format("<","-", ">"))
    }

    @Test
    fun emptyTest()
    {
        Assert.assertTrue(EMPTY_CHARACTERS_INTERVAL.empty)
        Assert.assertTrue(interval('Y', 'A').empty)
        Assert.assertFalse(interval('U', 'U').empty)
        Assert.assertFalse(interval('C', 'R').empty)
    }

    @Test
    fun containsTest()
    {
        Assert.assertFalse('B' in interval('Y', 'A'))
        Assert.assertFalse('B' in interval('U', 'U'))
        Assert.assertTrue('B' in interval('B', 'B'))
        Assert.assertFalse('B' in interval('C', 'R'))
        Assert.assertTrue('F' in interval('C', 'R'))
        Assert.assertFalse('W' in interval('C', 'R'))
    }

    @Test
    fun intersectsTest()
    {
        Assert.assertFalse(interval('Y', 'A').intersects(interval('C', 'R')))
        Assert.assertTrue(interval('D', 'D').intersects(interval('C', 'R')))
        Assert.assertTrue(interval('A', 'D').intersects(interval('C', 'R')))
        Assert.assertTrue(interval('A', 'Z').intersects(interval('C', 'R')))
        Assert.assertTrue(interval('G', 'Z').intersects(interval('C', 'R')))
        Assert.assertFalse(interval('W', 'Z').intersects(interval('C', 'R')))
        Assert.assertFalse(interval('C', 'H').intersects(interval('I', 'M')))
    }

    @Test
    fun intersectsUnionTest()
    {
        Assert.assertFalse(interval('Y', 'A').intersectsUnion(interval('C', 'R')))
        Assert.assertTrue(interval('D', 'D').intersectsUnion(interval('C', 'R')))
        Assert.assertTrue(interval('A', 'D').intersectsUnion(interval('C', 'R')))
        Assert.assertTrue(interval('A', 'Z').intersectsUnion(interval('C', 'R')))
        Assert.assertTrue(interval('G', 'Z').intersectsUnion(interval('C', 'R')))
        Assert.assertFalse(interval('W', 'Z').intersectsUnion(interval('C', 'R')))
        Assert.assertTrue(interval('C', 'H').intersectsUnion(interval('I', 'M')))
    }

    @Test
    fun timesTest()
    {
        var result = interval('Y', 'A') * interval('C', 'R')
        Assert.assertTrue(result.empty)
        result = interval('D', 'D') * interval('C', 'R')
        Assert.assertFalse(result.empty)
        Assert.assertEquals('D', result.minimum)
        Assert.assertEquals('D', result.maximum)
        result = interval('W', 'Z') * interval('C', 'R')
        Assert.assertTrue(result.empty)
        result = interval('G', 'Z') * interval('C', 'R')
        Assert.assertFalse(result.empty)
        Assert.assertEquals('G', result.minimum)
        Assert.assertEquals('R', result.maximum)
    }

    @Test
    fun plusTest()
    {
        var result = interval('Y', 'A') + interval('C', 'R')
        Assert.assertFalse(result.empty)
        Assert.assertFalse('B' in result)
        Assert.assertTrue('C' in result)
        Assert.assertTrue('H' in result)
        Assert.assertTrue('R' in result)
        Assert.assertFalse('T' in result)

        result = interval('C', 'H') + interval('I', 'M')
        Assert.assertFalse(result.empty)
        Assert.assertFalse('B' in result)
        Assert.assertTrue('C' in result)
        Assert.assertTrue('F' in result)
        Assert.assertTrue('H' in result)
        Assert.assertTrue('I' in result)
        Assert.assertTrue('K' in result)
        Assert.assertTrue('M' in result)
        Assert.assertFalse('P' in result)

        result = interval('C', 'H') + interval('J', 'M')
        Assert.assertFalse(result.empty)
        Assert.assertFalse('B' in result)
        Assert.assertTrue('C' in result)
        Assert.assertTrue('F' in result)
        Assert.assertTrue('H' in result)
        Assert.assertFalse('I' in result)
        Assert.assertTrue('K' in result)
        Assert.assertTrue('M' in result)
        Assert.assertFalse('P' in result)
    }

    @Test
    fun minusTest()
    {
        var result = interval('Y', 'A') - interval('C', 'R')
        Assert.assertTrue(result.empty)

        result = interval('C', 'R') - interval('Y', 'A')
        Assert.assertFalse(result.empty)
        Assert.assertFalse('B' in result)
        Assert.assertTrue('C' in result)
        Assert.assertTrue('G' in result)
        Assert.assertTrue('R' in result)
        Assert.assertFalse('S' in result)

        result = interval('C', 'R') - interval('G', 'O')
        Assert.assertFalse(result.empty)
        Assert.assertFalse('B' in result)
        Assert.assertTrue('C' in result)
        Assert.assertTrue('D' in result)
        Assert.assertTrue('E' in result)
        Assert.assertTrue('F' in result)
        Assert.assertFalse('G' in result)
        Assert.assertFalse('K' in result)
        Assert.assertFalse('O' in result)
        Assert.assertTrue('P' in result)
        Assert.assertTrue('Q' in result)
        Assert.assertTrue('R' in result)
        Assert.assertFalse('S' in result)
    }

    @Test
    fun remTest()
    {
        var result = interval('Y', 'A') % interval('C', 'R')
        Assert.assertFalse(result.empty)
        Assert.assertFalse('B' in result)
        Assert.assertTrue('C' in result)
        Assert.assertTrue('G' in result)
        Assert.assertTrue('R' in result)
        Assert.assertFalse('S' in result)

        result =  interval('C', 'R') % interval('P', 'W')
        Assert.assertFalse(result.empty)
        Assert.assertFalse('B' in result)
        Assert.assertTrue('C' in result)
        Assert.assertTrue('F' in result)
        Assert.assertTrue('O' in result)
        Assert.assertFalse('P' in result)
        Assert.assertFalse('Q' in result)
        Assert.assertFalse('R' in result)
        Assert.assertTrue('S' in result)
        Assert.assertTrue('U' in result)
        Assert.assertTrue('W' in result)
        Assert.assertFalse('Y' in result)
    }
}