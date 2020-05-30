/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database

import androidx.annotation.Keep
import fr.jhelp.database.condition.DataIntLower
import fr.jhelp.database.condition.DataIntUpper
import fr.jhelp.database.condition.DataStringEqual

private const val NAME = "name"
private const val AGE = "age"
private const val SIBLING = "sibling"

@Keep
class PersonStorable() : DataStorable()
{
    companion object
    {
        fun nameIs(name:String) = DataStringEqual(NAME, name)
        fun ageLower(ageMaximum:Int) = DataIntLower(AGE, ageMaximum)
        fun ageUpper(ageMinimum:Int) = DataIntUpper(AGE, ageMinimum)
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