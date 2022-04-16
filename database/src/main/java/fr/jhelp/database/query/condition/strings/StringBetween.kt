/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database.query.condition.strings

import fr.jhelp.database.DataType
import fr.jhelp.database.DatabaseNoSQL
import fr.jhelp.database.DatabaseObject
import fr.jhelp.database.query.condition.PropertyCondition
import kotlin.reflect.KProperty1

internal class StringBetween<DO : DatabaseObject>(property: KProperty1<DO, String>,
                                                  value1: String, value2: String)
    : PropertyCondition<DO, String>(property)
{
    override val type: DataType get() = DataType.STRING
    private val minimum: String
    private val maximum: String

    init
    {
        if (value1 <= value2)
        {
            this.minimum = value1
            this.maximum = value2
        }
        else
        {
            this.minimum = value2
            this.maximum = value1
        }
    }

    override fun whereInternal(stringBuilder: StringBuilder)
    {
        stringBuilder.append("(")
        stringBuilder.append(DatabaseNoSQL.COLUMN_VALUE_TEXT)
        stringBuilder.append(">='")
        stringBuilder.append(this.minimum)
        stringBuilder.append("' AND ")
        stringBuilder.append(DatabaseNoSQL.COLUMN_VALUE_TEXT)
        stringBuilder.append("<='")
        stringBuilder.append(this.maximum)
        stringBuilder.append("')")
    }
}
