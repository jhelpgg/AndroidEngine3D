/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database.query.condition.floats

import fr.jhelp.database.DataType
import fr.jhelp.database.DatabaseNoSQL
import fr.jhelp.database.DatabaseObject
import fr.jhelp.database.query.condition.PropertyCondition
import kotlin.math.max
import kotlin.math.min
import kotlin.reflect.KProperty1

internal class FloatBetween<DO : DatabaseObject>(property: KProperty1<DO, Float>,
                                                 value1: Float, value2: Float)
    : PropertyCondition<DO, Float>(property)
{
    override val type: DataType get() = DataType.FLOAT
    private val minimum = min(value1, value2)
    private val maximum = max(value1, value2)

    override fun whereInternal(stringBuilder: StringBuilder)
    {
        stringBuilder.append("(")
        stringBuilder.append(DatabaseNoSQL.COLUMN_VALUE_NUMBER)
        stringBuilder.append(">=")
        stringBuilder.append(this.minimum)
        stringBuilder.append(" AND ")
        stringBuilder.append(DatabaseNoSQL.COLUMN_VALUE_NUMBER)
        stringBuilder.append("<=")
        stringBuilder.append(this.maximum)
        stringBuilder.append(")")
    }
}
