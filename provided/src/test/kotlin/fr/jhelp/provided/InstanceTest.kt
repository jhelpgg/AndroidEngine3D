/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.provided

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class InstanceTest(val number: Int) : InterfaceTest
{
    val called = AtomicBoolean(false)
    val callCount = AtomicInteger(0)

    override fun test(): Int
    {
        this.called.set(true)
        this.callCount.incrementAndGet()
        println("This is a test : $number")
        return this.number
    }

    override fun callCount(): Int = this.callCount.get()
}