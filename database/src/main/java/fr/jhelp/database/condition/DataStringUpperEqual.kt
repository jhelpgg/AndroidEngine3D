/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database.condition

import fr.jhelp.database.COLUMN_ID
import fr.jhelp.database.COLUMN_NAME
import fr.jhelp.database.COLUMN_OBJECT_ID
import fr.jhelp.database.COLUMN_TYPE
import fr.jhelp.database.COLUMN_VALUE_TEXT
import fr.jhelp.database.DataType
import fr.jhelp.database.TABLE_FIELDS

class DataStringUpperEqual(private val field:String, private val value: String) :DataCondition()
{
    private val query =
        "SELECT $COLUMN_ID FROM $TABLE_FIELDS WHERE $COLUMN_OBJECT_ID=? AND $COLUMN_NAME=? AND $COLUMN_TYPE=? AND $COLUMN_VALUE_TEXT>=?"

    override fun nextQuery(parameters: MutableList<String>,
                           executeQuery: (String) -> Long): ConditionResult
    {
        parameters.add(this.field)
        parameters.add(DataType.TEXT.name)
        parameters.add(this.value)

        return if (executeQuery(this.query)>=0L)
        {
            ConditionResult.VALID
        }
        else
        {
            ConditionResult.INVALID
        }
    }
}