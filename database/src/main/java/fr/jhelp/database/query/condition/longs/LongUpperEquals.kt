/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database.query.condition.longs

import fr.jhelp.database.DataType
import fr.jhelp.database.DatabaseNoSQL
import fr.jhelp.database.DatabaseObject
import fr.jhelp.database.query.condition.PropertyCondition
import kotlin.reflect.KProperty1

internal class LongUpperEquals<DO : DatabaseObject>(property: KProperty1<DO, Long>,
                                                    private val value: Long)
    : PropertyCondition<DO, Long>(property)
{
    override val type: DataType get() = DataType.LONG

    override fun whereInternal(stringBuilder: StringBuilder)
    {
        stringBuilder.append(DatabaseNoSQL.COLUMN_VALUE_INTEGER)
        stringBuilder.append(">=")
        stringBuilder.append(this.value)
    }
}