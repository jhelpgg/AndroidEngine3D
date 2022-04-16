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
import fr.jhelp.utilities.checkArgument
import fr.jhelp.utilities.extensions.string
import kotlin.reflect.KProperty1

internal class DoubleOutside<DO : DatabaseObject>(property: KProperty1<DO, Double>,
                                                  values: DoubleArray)
    : PropertyCondition<DO, Double>(property)
{
    override val type: DataType get() = DataType.DOUBLE
    private val inCondition = values.string(header = " IN (", footer = ")")

    init
    {
        checkArgument(values.isNotEmpty(), "values array must not be empty")
    }

    override fun whereInternal(stringBuilder: StringBuilder)
    {
        stringBuilder.append("NOT(")
        stringBuilder.append(DatabaseNoSQL.COLUMN_VALUE_NUMBER)
        stringBuilder.append(this.inCondition)
        stringBuilder.append(")")
    }
}
