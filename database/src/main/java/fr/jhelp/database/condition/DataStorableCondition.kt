/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database.condition

import fr.jhelp.database.COLUMN_NAME
import fr.jhelp.database.COLUMN_OBJECT_ID
import fr.jhelp.database.COLUMN_TYPE
import fr.jhelp.database.COLUMN_VALUE_OBJECT_ID
import fr.jhelp.database.DataType
import fr.jhelp.database.ID_NEW_DATA
import fr.jhelp.database.TABLE_FIELDS

class DataStorableCondition(private val field: String, private val condition: DataCondition) :
    DataCondition()
{
    private var first = true
    private var objectID = ID_NEW_DATA
    private val query =
        "SELECT $COLUMN_VALUE_OBJECT_ID FROM $TABLE_FIELDS WHERE $COLUMN_OBJECT_ID=? AND $COLUMN_NAME=? AND $COLUMN_TYPE=?"


    override fun start()
    {
        this.first = true
        this.objectID = ID_NEW_DATA
        this.condition.start()
    }

    override fun nextQuery(parameters: MutableList<String>,
                           executeQuery: (String) -> Long): ConditionResult
    {
        if (this.first)
        {
            parameters.add(this.field)
            parameters.add(DataType.OBJECT.name)
            this.objectID = executeQuery(this.query)

            if (this.objectID == ID_NEW_DATA)
            {
                return ConditionResult.INVALID
            }

            this.first = false
            return ConditionResult.UNKNOWN
        }

        parameters[0] = this.objectID.toString()
        return this.condition.nextQuery(parameters, executeQuery)
    }
}