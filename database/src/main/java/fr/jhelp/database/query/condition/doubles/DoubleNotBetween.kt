/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database.query.condition.doubles

import fr.jhelp.database.DataType
import fr.jhelp.database.DatabaseNoSQL
import fr.jhelp.database.DatabaseObject
import fr.jhelp.database.query.condition.PropertyCondition
import kotlin.math.max
import kotlin.math.min
import kotlin.reflect.KProperty1

internal class DoubleNotBetween<DO : DatabaseObject>(property: KProperty1<DO, Double>,
                                                     value1: Double, value2: Double)
    : PropertyCondition<DO, Double>(property)
{
    override val type: DataType get() = DataType.DOUBLE
    private val minimum = min(value1, value2)
    private val maximum = max(value1, value2)

    override fun whereInternal(stringBuilder: StringBuilder)
    {
        stringBuilder.append("(")
        stringBuilder.append(DatabaseNoSQL.COLUMN_VALUE_NUMBER)
        stringBuilder.append("<")
        stringBuilder.append(this.minimum)
        stringBuilder.append(" OR ")
        stringBuilder.append(DatabaseNoSQL.COLUMN_VALUE_NUMBER)
        stringBuilder.append(">")
        stringBuilder.append(this.maximum)
        stringBuilder.append(")")
    }
}
