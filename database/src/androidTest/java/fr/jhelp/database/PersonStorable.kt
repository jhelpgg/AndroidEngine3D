/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database

import androidx.annotation.Keep
import fr.jhelp.database.condition.AND
import fr.jhelp.database.condition.DataCondition
import fr.jhelp.database.condition.equal
import fr.jhelp.database.condition.lower
import fr.jhelp.database.condition.lowerEqual
import fr.jhelp.database.condition.upper
import fr.jhelp.database.condition.upperEqual
import fr.jhelp.database.condition.where
import kotlin.math.max
import kotlin.math.min

private const val NAME = "name"
private const val AGE = "age"
private const val SIBLING = "sibling"

@Keep
class PersonStorable() : DataStorable()
{
    companion object
    {
        fun nameIs(name: String) = NAME equal name
        fun ageLower(ageMaximum: Int) = AGE lower ageMaximum
        fun ageUpper(ageMinimum: Int) = AGE upper ageMinimum
        fun ageBetween(age1:Int, age2:Int) = AGE upperEqual min(age1,age2) AND (AGE lowerEqual max(age1, age2))
        fun ageIs(age: Int) = AGE equal age
        fun whereSibling(condition: DataCondition) = SIBLING where condition
    }

    constructor(name: String, age: Int) : this()
    {
        this.name(name)
        this.age(age)
    }

    fun name() = this.getString(NAME, "")!!

    fun name(name: String)
    {
        this.putString(NAME, name)
    }

    fun age() = this.getInt(AGE)

    fun age(age: Int)
    {
        this.putInt(AGE, age)
    }

    fun sibling() = this.getDataStorable<PersonStorable>(SIBLING)

    fun sibling(sibling: PersonStorable)
    {
        this.putDataStorable(SIBLING, sibling)
    }

    fun removeSibling()
    {
        this.removeKey(SIBLING)
    }
}