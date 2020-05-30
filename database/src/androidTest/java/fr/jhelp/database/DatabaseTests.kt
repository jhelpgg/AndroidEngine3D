/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.atomic.AtomicBoolean

@RunWith(AndroidJUnit4::class)
class DatabaseTests
{
    @Test
    fun simpleTest()
    {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val database = Database(appContext, "test")

        val person = PersonStorable("Arthur", 42)
        database.store("P", person)
        var person2 = database.read<PersonStorable>("P")
        Assert.assertNotNull(person2)
        Assert.assertEquals(person.name(), person2!!.name())
        Assert.assertEquals(person.age(), person2.age())

        person.sibling(PersonStorable("Baby D'jo", 1))
        database.store("P", person)
        person2 = database.read<PersonStorable>("P")
        Assert.assertNotNull(person2)
        Assert.assertEquals(person.name(), person2!!.name())
        Assert.assertEquals(person.age(), person2.age())
        var sibling = person.sibling()
        var sibling2 = person2.sibling()
        Assert.assertNotNull(sibling)
        Assert.assertNotNull(sibling2)
        Assert.assertEquals(sibling!!.name(), sibling2!!.name())
        Assert.assertEquals(sibling.age(), sibling2.age())
        val entered = AtomicBoolean(false)

        database.select(PersonStorable::class.java, PersonStorable.nameIs("Arthur"))
        { person3 ->
            Assert.assertNotNull(person3)
            Assert.assertEquals(person.name(), person3.name())
            Assert.assertEquals(person.age(), person3.age())
            sibling = person.sibling()
            sibling2 = person3.sibling()
            Assert.assertNotNull(sibling)
            Assert.assertNotNull(sibling2)
            Assert.assertEquals(sibling!!.name(), sibling2!!.name())
            Assert.assertEquals(sibling!!.age(), sibling2!!.age())
            entered.set(true)
        }

        Assert.assertTrue(entered.get())
        entered.set(false)

        database.select(PersonStorable::class.java,
                        PersonStorable.ageBetween(30, 50))
        { person3 ->
            Assert.assertNotNull(person3)
            Assert.assertEquals(person.name(), person3.name())
            Assert.assertEquals(person.age(), person3.age())
            sibling = person.sibling()
            sibling2 = person3.sibling()
            Assert.assertNotNull(sibling)
            Assert.assertNotNull(sibling2)
            Assert.assertEquals(sibling!!.name(), sibling2!!.name())
            Assert.assertEquals(sibling!!.age(), sibling2!!.age())
            entered.set(true)
        }

        Assert.assertTrue(entered.get())
        entered.set(false)
        database.select(PersonStorable::class.java,
                        PersonStorable.ageLower(20))
        {
            entered.set(true)
        }

        Assert.assertFalse(entered.get())
        entered.set(false)
        database.select(PersonStorable::class.java,
                        PersonStorable.whereSibling(PersonStorable.ageIs(1)))
        { person3 ->
            Assert.assertNotNull(person3)
            Assert.assertEquals(person.name(), person3.name())
            Assert.assertEquals(person.age(), person3.age())
            sibling = person.sibling()
            sibling2 = person3.sibling()
            Assert.assertNotNull(sibling)
            Assert.assertNotNull(sibling2)
            Assert.assertEquals(sibling!!.name(), sibling2!!.name())
            Assert.assertEquals(sibling!!.age(), sibling2!!.age())
            entered.set(true)
        }

        Assert.assertTrue(entered.get())

        database.delete("P")
        person2 = database.read<PersonStorable>("P")
        Assert.assertNull(person2)
        database.close()
    }
}
