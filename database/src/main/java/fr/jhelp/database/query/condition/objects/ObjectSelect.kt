/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database.query.condition.objects

import fr.jhelp.database.DataType
import fr.jhelp.database.DatabaseNoSQL
import fr.jhelp.database.DatabaseObject
import fr.jhelp.database.query.condition.DatabaseCondition
import fr.jhelp.database.query.condition.PropertyCondition
import kotlin.reflect.KProperty1

internal class ObjectSelect<DO : DatabaseObject, DOP : DatabaseObject>(
    property: KProperty1<DO, DOP>,
    private val value: DatabaseCondition,
    objectClass: Class<DOP>)
    : PropertyCondition<DO, DOP>(property)
{
    override val type: DataType get() = DataType.OBJECT
    private val className = objectClass.name

    override fun whereInternal(stringBuilder: StringBuilder)
    {
        stringBuilder.append(DatabaseNoSQL.COLUMN_VALUE_INTEGER)
        stringBuilder.append(" IN (SELECT ")
        stringBuilder.append(DatabaseNoSQL.COLUMN_ID)
        stringBuilder.append(" FROM ")
        stringBuilder.append(DatabaseNoSQL.TABLE_OBJECTS)
        stringBuilder.append(" WHERE ")
        stringBuilder.append(DatabaseNoSQL.COLUMN_CLASS_NAME)
        stringBuilder.append("='")
        stringBuilder.append(this.className)
        stringBuilder.append("' AND ")
        stringBuilder.append(DatabaseNoSQL.COLUMN_ID)
        stringBuilder.append(" IN (SELECT ")
        stringBuilder.append(DatabaseNoSQL.COLUMN_OBJECT_ID)
        stringBuilder.append(" FROM ")
        stringBuilder.append(DatabaseNoSQL.TABLE_FIELDS)
        stringBuilder.append(" WHERE ")
        this.value.where(stringBuilder)
        stringBuilder.append(")")
        stringBuilder.append(")")
    }
}