/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database.query.condition

import fr.jhelp.database.DataType
import fr.jhelp.database.DatabaseNoSQL
import fr.jhelp.database.DatabaseObject
import kotlin.reflect.KProperty1

internal abstract class PropertyCondition<DO : DatabaseObject, T : Any>(
    property: KProperty1<DO, T>) : DatabaseCondition
{
    private val fieldName = property.name
    protected abstract val type: DataType

    override fun where(stringBuilder: StringBuilder)
    {
        stringBuilder.append(DatabaseNoSQL.COLUMN_NAME)
        stringBuilder.append("='")
        stringBuilder.append(this.fieldName)
        stringBuilder.append("'")
        stringBuilder.append(" AND ")
        stringBuilder.append(DatabaseNoSQL.COLUMN_TYPE)
        stringBuilder.append("='")
        stringBuilder.append(this.type.name)
        stringBuilder.append("'")
        stringBuilder.append(" AND ")
        this.whereInternal(stringBuilder)
    }

    protected abstract fun whereInternal(stringBuilder: StringBuilder)
}
