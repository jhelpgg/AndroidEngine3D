/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database.query.condition.enums

import fr.jhelp.database.DataType
import fr.jhelp.database.DatabaseNoSQL
import fr.jhelp.database.DatabaseObject
import fr.jhelp.database.query.condition.PropertyCondition
import fr.jhelp.utilities.checkArgument
import kotlin.reflect.KProperty1

internal class EnumInside<DO : DatabaseObject, E : Enum<E>>(property: KProperty1<DO, E>,
                                                            values: Array<out E>)
    : PropertyCondition<DO, E>(property)
{
    override val type: DataType get() = DataType.ENUM
    private val inCondition: String

    init
    {
        checkArgument(values.isNotEmpty(), "values array must not be empty")

        val stringBuilder = StringBuilder()
        stringBuilder.append(" IN (")
        var afterFirst = false

        for (value in values)
        {
            if (afterFirst)
            {
                stringBuilder.append(", ")
            }

            stringBuilder.append("'")
            stringBuilder.append(value.javaClass.name)
            stringBuilder.append("|")
            stringBuilder.append(value.name)
            stringBuilder.append("'")

            afterFirst = true
        }

        stringBuilder.append(")")
        this.inCondition = stringBuilder.toString()
    }

    override fun whereInternal(stringBuilder: StringBuilder)
    {
        stringBuilder.append(DatabaseNoSQL.COLUMN_VALUE_TEXT)
        stringBuilder.append(this.inCondition)
    }
}
