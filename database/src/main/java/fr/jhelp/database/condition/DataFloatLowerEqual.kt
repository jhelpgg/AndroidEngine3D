/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database.condition

import fr.jhelp.database.COLUMN_NAME
import fr.jhelp.database.COLUMN_TYPE
import fr.jhelp.database.COLUMN_VALUE_NUMBER
import fr.jhelp.database.DataType

class DataFloatLowerEqual(field:String, value: Float) :DataCondition(field)
{
    private val condition = "$COLUMN_NAME=? AND $COLUMN_TYPE=? AND $COLUMN_VALUE_NUMBER<=?"
    private val value = value.toString()

    override fun condition() = this.condition

    override fun fill(arguments: MutableList<String>)
    {
        arguments.add(this.field)
        arguments.add(DataType.NUMBER.name)
        arguments.add(this.value)
    }
}