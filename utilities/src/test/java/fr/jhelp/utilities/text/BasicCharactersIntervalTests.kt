package fr.jhelp.utilities.text

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class BasicCharactersIntervalTests
{
    @Test
    fun formatTest()
    {
        Assertions.assertEquals("[]", EMPTY_CHARACTERS_INTERVAL.format())
        Assertions.assertEquals("[]", interval('Y', 'A').format())
        Assertions.assertEquals("{U}", interval('U', 'U').format())
        Assertions.assertEquals("[C, R]", interval('C', 'R').format())
        Assertions.assertEquals("{\\u0020}", interval(' ', ' ').format(hexadecimalForm = true))
        Assertions.assertEquals("<C-R>", interval('C', 'R').format("<", "-", ">"))
    }

    @Test
    fun emptyTest()
    {
        Assertions.assertTrue(EMPTY_CHARACTERS_INTERVAL.empty)
        Assertions.assertTrue(interval('Y', 'A').empty)
        Assertions.assertFalse(interval('U', 'U').empty)
        Assertions.assertFalse(interval('C', 'R').empty)
    }

    @Test
    fun containsTest()
    {
        Assertions.assertFalse('B' in interval('Y', 'A'))
        Assertions.assertFalse('B' in interval('U', 'U'))
        Assertions.assertTrue('B' in interval('B', 'B'))
        Assertions.assertFalse('B' in interval('C', 'R'))
        Assertions.assertTrue('F' in interval('C', 'R'))
        Assertions.assertFalse('W' in interval('C', 'R'))
    }

    @Test
    fun intersectsTest()
    {
        Assertions.assertFalse(interval('Y', 'A').intersects(interval('C', 'R')))
        Assertions.assertTrue(interval('D', 'D').intersects(interval('C', 'R')))
        Assertions.assertTrue(interval('A', 'D').intersects(interval('C', 'R')))
        Assertions.assertTrue(interval('A', 'Z').intersects(interval('C', 'R')))
        Assertions.assertTrue(interval('G', 'Z').intersects(interval('C', 'R')))
        Assertions.assertFalse(interval('W', 'Z').intersects(interval('C', 'R')))
        Assertions.assertFalse(interval('C', 'H').intersects(interval('I', 'M')))
    }

    @Test
    fun intersectsUnionTest()
    {
        Assertions.assertFalse(interval('Y', 'A').intersectsUnion(interval('C', 'R')))
        Assertions.assertTrue(interval('D', 'D').intersectsUnion(interval('C', 'R')))
        Assertions.assertTrue(interval('A', 'D').intersectsUnion(interval('C', 'R')))
        Assertions.assertTrue(interval('A', 'Z').intersectsUnion(interval('C', 'R')))
        Assertions.assertTrue(interval('G', 'Z').intersectsUnion(interval('C', 'R')))
        Assertions.assertFalse(interval('W', 'Z').intersectsUnion(interval('C', 'R')))
        Assertions.assertTrue(interval('C', 'H').intersectsUnion(interval('I', 'M')))
    }

    @Test
    fun timesTest()
    {
        var result = interval('Y', 'A') * interval('C', 'R')
        Assertions.assertTrue(result.empty)
        result = interval('D', 'D') * interval('C', 'R')
        Assertions.assertFalse(result.empty)
        Assertions.assertEquals('D', result.minimum)
        Assertions.assertEquals('D', result.maximum)
        result = interval('W', 'Z') * interval('C', 'R')
        Assertions.assertTrue(result.empty)
        result = interval('G', 'Z') * interval('C', 'R')
        Assertions.assertFalse(result.empty)
        Assertions.assertEquals('G', result.minimum)
        Assertions.assertEquals('R', result.maximum)
    }

    @Test
    fun plusTest()
    {
        var result = interval('Y', 'A') + interval('C', 'R')
        Assertions.assertFalse(result.empty)
        Assertions.assertFalse('B' in result)
        Assertions.assertTrue('C' in result)
        Assertions.assertTrue('H' in result)
        Assertions.assertTrue('R' in result)
        Assertions.assertFalse('T' in result)

        result = interval('C', 'H') + interval('I', 'M')
        Assertions.assertFalse(result.empty)
        Assertions.assertFalse('B' in result)
        Assertions.assertTrue('C' in result)
        Assertions.assertTrue('F' in result)
        Assertions.assertTrue('H' in result)
        Assertions.assertTrue('I' in result)
        Assertions.assertTrue('K' in result)
        Assertions.assertTrue('M' in result)
        Assertions.assertFalse('P' in result)

        result = interval('C', 'H') + interval('J', 'M')
        Assertions.assertFalse(result.empty)
        Assertions.assertFalse('B' in result)
        Assertions.assertTrue('C' in result)
        Assertions.assertTrue('F' in result)
        Assertions.assertTrue('H' in result)
        Assertions.assertFalse('I' in result)
        Assertions.assertTrue('K' in result)
        Assertions.assertTrue('M' in result)
        Assertions.assertFalse('P' in result)
    }

    @Test
    fun minusTest()
    {
        var result = interval('Y', 'A') - interval('C', 'R')
        Assertions.assertTrue(result.empty)

        result = interval('C', 'R') - interval('Y', 'A')
        Assertions.assertFalse(result.empty)
        Assertions.assertFalse('B' in result)
        Assertions.assertTrue('C' in result)
        Assertions.assertTrue('G' in result)
        Assertions.assertTrue('R' in result)
        Assertions.assertFalse('S' in result)

        result = interval('C', 'R') - interval('G', 'O')
        Assertions.assertFalse(result.empty)
        Assertions.assertFalse('B' in result)
        Assertions.assertTrue('C' in result)
        Assertions.assertTrue('D' in result)
        Assertions.assertTrue('E' in result)
        Assertions.assertTrue('F' in result)
        Assertions.assertFalse('G' in result)
        Assertions.assertFalse('K' in result)
        Assertions.assertFalse('O' in result)
        Assertions.assertTrue('P' in result)
        Assertions.assertTrue('Q' in result)
        Assertions.assertTrue('R' in result)
        Assertions.assertFalse('S' in result)
    }

    @Test
    fun remTest()
    {
        var result = interval('Y', 'A') % interval('C', 'R')
        Assertions.assertFalse(result.empty)
        Assertions.assertFalse('B' in result)
        Assertions.assertTrue('C' in result)
        Assertions.assertTrue('G' in result)
        Assertions.assertTrue('R' in result)
        Assertions.assertFalse('S' in result)

        result = interval('C', 'R') % interval('P', 'W')
        Assertions.assertFalse(result.empty)
        Assertions.assertFalse('B' in result)
        Assertions.assertTrue('C' in result)
        Assertions.assertTrue('F' in result)
        Assertions.assertTrue('O' in result)
        Assertions.assertFalse('P' in result)
        Assertions.assertFalse('Q' in result)
        Assertions.assertFalse('R' in result)
        Assertions.assertTrue('S' in result)
        Assertions.assertTrue('U' in result)
        Assertions.assertTrue('W' in result)
        Assertions.assertFalse('Y' in result)
    }
}