/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

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
    fun test()
    {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val database = Database(appContext, "test")

        val person = PersonStorable("Arthur", 42)
        database.store("P", person)
        database.read<PersonStorable>("P")
            .and { person2 ->
                Assert.assertEquals(person.name(), person2.name())
                Assert.assertEquals(person.age(), person2.age())
            }
            .onError { exception -> Assert.fail(exception.toString()) }
            .waitComplete()

        person.sibling(PersonStorable("Baby D'jo", 1))
        database.store("P", person)
        database.read<PersonStorable>("P")
            .and { person2 ->
                Assert.assertEquals(person.name(), person2.name())
                Assert.assertEquals(person.age(), person2.age())
                val sibling = person.sibling()
                val sibling2 = person2.sibling()
                Assert.assertNotNull(sibling)
                Assert.assertNotNull(sibling2)
                Assert.assertEquals(sibling!!.name(), sibling2!!.name())
                Assert.assertEquals(sibling.age(), sibling2.age())
            }
            .onError { exception -> Assert.fail(exception.toString()) }
            .waitComplete()

        database.delete("P")

        val error = AtomicBoolean(false)
        database.read<PersonStorable>("P")
            .and { Assert.fail("Should hve nobody here") }
            .onError { error.set(true) }
            .waitComplete()

        Assert.assertTrue(error.get())

        database.shutdown().waitComplete()
    }
}
