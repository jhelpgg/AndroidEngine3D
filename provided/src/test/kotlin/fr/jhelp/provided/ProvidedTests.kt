/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.provided

import org.junit.Assert
import org.junit.Test

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
        provideSingle { instance as InterfaceTest }
        Assert.assertEquals(42, this.value.test())
        Assert.assertTrue(instance.called.get())
    }

    @Test
    fun overrideShare()
    {
        val instance1 = InstanceTest(42)
        val instance2 = InstanceTest(73)
        provideSingle { instance1 as InterfaceTest }
        Assert.assertEquals(42, this.value.test())
        Assert.assertTrue(instance1.called.get())
        Assert.assertFalse(instance2.called.get())

        instance1.called.set(false)
        provideSingle { instance2 as InterfaceTest }
        Assert.assertEquals(73, this.value.test())
        Assert.assertFalse(instance1.called.get())
        Assert.assertTrue(instance2.called.get())
    }

    @Test
    fun multiShare()
    {
        val instance1 = InstanceTest(42)
        val instance2 = InstanceTest(73)
        provideSingle("42") { instance1 as InterfaceTest }
        provideSingle("73") { instance2 as InterfaceTest }
        Assert.assertEquals(42, this.value42.test())
        Assert.assertTrue(instance1.called.get())
        Assert.assertEquals(73, this.value73.test())
        Assert.assertTrue(instance2.called.get())
        Assert.assertEquals(42, this.value42.test())
        Assert.assertEquals(73, this.value73.test())
    }

    @Test
    fun issueTest()
    {
        forget<InstanceTest>()

        try
        {
            this.value.test()
            Assert.fail("Should throw IllegalArgumentException since no shared instance")
        }
        catch (exception: IllegalArgumentException)
        {
            // That's what expected
        }
    }

    @Test
    fun singletonMultipleTest()
    {
        provideSingle("Single") { InstanceTest(42) as InterfaceTest }
        provideMultiple("Multiple") { InstanceTest(73) as InterfaceTest }

        Assert.assertEquals(42, this.valueSingle.test())
        var value = this.valueMultiple
        Assert.assertEquals(73, value.test())
        Assert.assertEquals(1, this.valueSingle.callCount())
        Assert.assertEquals(1, value.callCount())

        Assert.assertEquals(42, this.valueSingle.test())
        value = this.valueMultiple
        Assert.assertEquals(73, value.test())
        Assert.assertEquals(2, this.valueSingle.callCount())
        Assert.assertEquals(1, value.callCount())
    }
}