/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.provided

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class ProvidedTests
{
    val value by provided<InterfaceTest>()
    val value42 by provided<InterfaceTest>("42")
    val value73 by provided<InterfaceTest>("73")
    val valueSingle by provided<InterfaceTest>("Single")
    val valueMultiple by provided<InterfaceTest>("Multiple")

    @Test
    fun oneTimeShare()
    {
        val instance = InstanceTest(42)
        provideSingle<InterfaceTest> { instance }
        Assertions.assertEquals(42, this.value.test())
        Assertions.assertTrue(instance.called.get())
    }

    @Test
    fun overrideShare()
    {
        val instance1 = InstanceTest(42)
        val instance2 = InstanceTest(73)
        provideSingle<InterfaceTest> { instance1 }
        Assertions.assertEquals(42, this.value.test())
        Assertions.assertTrue(instance1.called.get())
        Assertions.assertFalse(instance2.called.get())

        instance1.called.set(false)
        provideSingle<InterfaceTest> { instance2 }
        Assertions.assertEquals(73, this.value.test())
        Assertions.assertFalse(instance1.called.get())
        Assertions.assertTrue(instance2.called.get())
    }

    @Test
    fun multiShare()
    {
        val instance1 = InstanceTest(42)
        val instance2 = InstanceTest(73)
        provideSingle<InterfaceTest>("42") { instance1 }
        provideSingle<InterfaceTest>("73") { instance2 }
        Assertions.assertEquals(42, this.value42.test())
        Assertions.assertTrue(instance1.called.get())
        Assertions.assertEquals(73, this.value73.test())
        Assertions.assertTrue(instance2.called.get())
        Assertions.assertEquals(42, this.value42.test())
        Assertions.assertEquals(73, this.value73.test())
    }

    @Test
    fun issueTest()
    {
        forget<InstanceTest>()

        try
        {
            this.value.test()
            Assertions.fail("Should throw IllegalArgumentException since no shared instance")
        }
        catch (exception: IllegalArgumentException)
        {
            // That's what expected
        }
    }

    @Test
    fun singletonMultipleTest()
    {
        provideSingle<InterfaceTest>("Single") { InstanceTest(42) }
        provideMultiple<InterfaceTest>("Multiple") { InstanceTest(73) }

        Assertions.assertEquals(42, this.valueSingle.test())
        var value = this.valueMultiple
        Assertions.assertEquals(73, value.test())
        Assertions.assertEquals(1, this.valueSingle.callCount())
        Assertions.assertEquals(1, value.callCount())

        Assertions.assertEquals(42, this.valueSingle.test())
        value = this.valueMultiple
        Assertions.assertEquals(73, value.test())
        Assertions.assertEquals(2, this.valueSingle.callCount())
        Assertions.assertEquals(1, value.callCount())
    }
}