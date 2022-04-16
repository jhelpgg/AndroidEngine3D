/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database

import android.app.Application
import fr.jhelp.provided.provideSingle
import fr.jhelp.tasks.promise.FutureResultStatus
import fr.jhelp.testor.cleanForNextTests
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DatabaseNoSQLTests
{
    companion object
    {
        @JvmStatic
        @BeforeAll
        fun prepareTests()
        {
            val application = Application()
            val applicationContext = application.applicationContext
            provideSingle { applicationContext }
        }
    }

    @BeforeEach
    fun startOneTest()
    {
        DatabaseNoSQL.open()
    }

    @AfterEach
    fun endOneTest()
    {
        cleanForNextTests()
    }

    @Test
    fun simpleWriteRead()
    {
        val address = AddressDatabaseObject("Backer Street", 21)
        val person = PersonDatabaseObject("Holmes", 1845, address)
        var future = DatabaseNoSQL.store(person)
        future.waitComplete()

        when (future.status())
        {
            FutureResultStatus.CANCELED ->
                Assertions.fail<Unit>("Store canceled because : ${future.cancelReason()}")
            FutureResultStatus.FAILED   ->
            {
                future.error()?.printStackTrace()
                Assertions.fail<Unit>("Store failed because : ${future.error()}")
            }
            else                        -> Unit
        }

        //

        var emitterPerson = DatabaseNoSQL.query<PersonDatabaseObject> { }
        var person2 = emitterPerson.next()
        Assertions.assertNotNull(person2)
        Assertions.assertEquals("Holmes", person2!!.name)
        Assertions.assertEquals(1845, person2.birthYear)
        var address2 = person2.address
        Assertions.assertEquals("Backer Street", address2.street)
        Assertions.assertEquals(21, address2.number)

        var emitterAddress = DatabaseNoSQL.query<AddressDatabaseObject> { }
        var address3 = emitterAddress.next()
        Assertions.assertNotNull(address3)
        Assertions.assertEquals("Backer Street", address3!!.street)
        Assertions.assertEquals(21, address3.number)

        //

        DatabaseNoSQL.close()
        DatabaseNoSQL.open()

        //

        emitterPerson = DatabaseNoSQL.query<PersonDatabaseObject> { }
        person2 = emitterPerson.next()
        Assertions.assertNotNull(person2)
        Assertions.assertEquals("Holmes", person2!!.name)
        Assertions.assertEquals(1845, person2.birthYear)
        address2 = person2.address
        Assertions.assertEquals("Backer Street", address2.street)
        Assertions.assertEquals(21, address2.number)

        emitterAddress = DatabaseNoSQL.query<AddressDatabaseObject> { }
        address3 = emitterAddress.next()
        Assertions.assertNotNull(address3)
        Assertions.assertEquals("Backer Street", address3!!.street)
        Assertions.assertEquals(21, address3.number)

        //

        future = DatabaseNoSQL.remove(person)
        future.waitComplete()

        when (future.status())
        {
            FutureResultStatus.CANCELED ->
                Assertions.fail<Unit>("Remove person canceled because : ${future.cancelReason()}")
            FutureResultStatus.FAILED   ->
            {
                future.error()?.printStackTrace()
                Assertions.fail<Unit>("Remove person failed because : ${future.error()}")
            }
            else                        -> Unit
        }

        emitterPerson = DatabaseNoSQL.query<PersonDatabaseObject> { }
        person2 = emitterPerson.next()
        Assertions.assertNull(person2)

        emitterAddress = DatabaseNoSQL.query<AddressDatabaseObject> { }
        address3 = emitterAddress.next()
        Assertions.assertNotNull(address3)
        Assertions.assertEquals("Backer Street", address3!!.street)
        Assertions.assertEquals(21, address3.number)

        //

        future = DatabaseNoSQL.remove(address)
        future.waitComplete()

        when (future.status())
        {
            FutureResultStatus.CANCELED ->
                Assertions.fail<Unit>("Remove address canceled because : ${future.cancelReason()}")
            FutureResultStatus.FAILED   ->
            {
                future.error()?.printStackTrace()
                Assertions.fail<Unit>("Remove address  failed because : ${future.error()}")
            }
            else                        -> Unit
        }

        emitterAddress = DatabaseNoSQL.query<AddressDatabaseObject> { }
        address3 = emitterAddress.next()
        Assertions.assertNull(address3)

        //

        DatabaseNoSQL.close()
    }

    @Test
    fun queryTests()
    {
        val addressBaker = AddressDatabaseObject("Baker", 21)
        val addressSpace = AddressDatabaseObject("Space", 7777777)
        val addressHell = AddressDatabaseObject("Hell", 666)
        val personHolmes = PersonDatabaseObject("Holmes", 1845, addressBaker)
        val personDandy = PersonDatabaseObject("Dandy", 2345, addressSpace)
        val personDevil = PersonDatabaseObject("Devil", -10_000, addressHell)
        val personCobra = PersonDatabaseObject("Cobra", 2345, addressSpace)
        personHolmes.save().waitComplete()
        personDandy.save().waitComplete()
        personDevil.save().waitComplete()
        personCobra.save().waitComplete()

        // Get all persons born at 2345

        var emitterPerson = DatabaseNoSQL.query<PersonDatabaseObject> {
            PersonDatabaseObject::birthYear EQUALS 2345
        }

        var person = emitterPerson.next()
        Assertions.assertNotNull(person)
        Assertions.assertEquals(personDandy, person)
        person = emitterPerson.next()
        Assertions.assertNotNull(person)
        Assertions.assertEquals(personCobra, person)

        Assertions.assertEquals(addressSpace, person!!.address)

        Assertions.assertNull(emitterPerson.next())

        // Test about address change

        person.address = addressHell

        emitterPerson = DatabaseNoSQL.query<PersonDatabaseObject> {
            PersonDatabaseObject::birthYear EQUALS 2345
        }

        person = emitterPerson.next()
        Assertions.assertNotNull(person)
        Assertions.assertEquals(personDandy, person)
        person = emitterPerson.next()
        Assertions.assertNotNull(person)
        Assertions.assertEquals(personCobra, person)

        Assertions.assertNull(emitterPerson.next())

        Assertions.assertEquals(addressHell, person!!.address)

        // Restore address for next tests

        person.address = addressSpace

        // Get all persons who lives in space

        emitterPerson = DatabaseNoSQL.query<PersonDatabaseObject> {
            PersonDatabaseObject::address EQUALS addressSpace
        }

        person = emitterPerson.next()
        Assertions.assertNotNull(person)
        Assertions.assertEquals(personDandy, person)
        person = emitterPerson.next()
        Assertions.assertNotNull(person)
        Assertions.assertEquals(personCobra, person)

        Assertions.assertNull(emitterPerson.next())

        // Get person who live at street number 666

        emitterPerson = DatabaseNoSQL.query<PersonDatabaseObject> {
            PersonDatabaseObject::address.SELECT {
                AddressDatabaseObject::number EQUALS 666
            }
        }

        person = emitterPerson.next()
        Assertions.assertNotNull(person)
        Assertions.assertEquals(personDevil, person)

        Assertions.assertNull(emitterPerson.next())

        // Get person  where born after JC but before 2000

        emitterPerson = DatabaseNoSQL.query<PersonDatabaseObject> {
            PersonDatabaseObject::birthYear.BETWEEN(0, 1999)
        }

        person = emitterPerson.next()
        Assertions.assertNotNull(person)
        Assertions.assertEquals(personHolmes, person)

        Assertions.assertNull(emitterPerson.next())

        // Get person  where born after JC but before 2000

        emitterPerson = DatabaseNoSQL.query<PersonDatabaseObject> {
            AND {
                and { PersonDatabaseObject::birthYear UPPER 0 }
                and { PersonDatabaseObject::birthYear LOWER 2000 }
            }
        }

        person = emitterPerson.next()
        Assertions.assertNotNull(person)
        Assertions.assertEquals(personHolmes, person)

        Assertions.assertNull(emitterPerson.next())

        DatabaseNoSQL.close()
    }
}
