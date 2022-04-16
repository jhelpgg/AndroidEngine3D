/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database

import fr.jhelp.lists.transform
import fr.jhelp.tasks.promise.FutureResult
import java.lang.reflect.Modifier
import java.util.Objects
import kotlin.reflect.jvm.kotlinFunction

abstract class DatabaseObject
{
    internal var databaseId = DatabaseNoSQL.NO_DATABASE_ID

    fun save(): FutureResult<Unit> =
        DatabaseNoSQL.store(this)

    protected fun update()
    {
        if (this.databaseId != DatabaseNoSQL.NO_DATABASE_ID)
        {
            DatabaseNoSQL.store(this).waitComplete()
        }
    }

    internal fun obtainDatabaseId(): Long
    {
        if (this.databaseId == DatabaseNoSQL.NO_DATABASE_ID)
        {
            DatabaseNoSQL.store(this).waitComplete()
        }

        return this.databaseId
    }

    internal fun collectInfo(): DatabaseObjectInfo
    {
        val primaryValues = ArrayList<DataValue<*>>()
        val otherValues = ArrayList<DataValue<*>>()
        val constructorObject = this.javaClass.constructors[0]
        val parametersKotlin = constructorObject.kotlinFunction!!.parameters

        for (name in parametersKotlin.transform { parameter -> parameter.name!! })
        {
            val field = this.javaClass.getDeclaredField(name)
            field.isAccessible = true
            val primary = field.getAnnotation(PrimaryKey::class.java) != null
            val isFinal = Modifier.isFinal(field.modifiers)
            val fieldType = field.type
            val fieldName = field.name

            if (primary && !isFinal)
            {
                throw IllegalStateException(
                    "The field ${field.name} of database object ${this.javaClass.name} is marked primary but not final (val)")
            }

            val dataValue =
                when
                {
                    fieldType == Boolean::class.java                       ->
                        if (primary)
                        {
                            throw IllegalStateException(
                                "The field ${field.name} of database object ${this.javaClass.name} is Boolean and marked as primary, that is not possible (Choose Int, Long, String or Enum for primary key)")
                        }
                        else
                        {
                            DataBoolean(fieldName, field.getBoolean(this))
                        }
                    fieldType == Int::class.java                           ->
                        DataInt(fieldName, field.getInt(this), primary)
                    fieldType == Long::class.java                          ->
                        DataLong(fieldName, field.getLong(this), primary)
                    fieldType == Float::class.java                         ->
                        if (primary)
                        {
                            throw IllegalStateException(
                                "The field ${field.name} of database object ${this.javaClass.name} is Float and marked as primary, that is not possible (Choose Int, Long, String or Enum for primary key)")
                        }
                        else
                        {
                            DataFloat(fieldName, field.getFloat(this))
                        }
                    fieldType == Double::class.java                        ->
                        if (primary)
                        {
                            throw IllegalStateException(
                                "The field ${field.name} of database object ${this.javaClass.name} is Double and marked as primary, that is not possible (Choose Int, Long, String or Enum for primary key)")
                        }
                        else
                        {
                            DataDouble(fieldName, field.getDouble(this))
                        }
                    fieldType == String::class.java                        ->
                        DataString(fieldName, field.get(this) as String, primary)
                    fieldType.isEnum                                       ->
                        DataEnum(fieldName, fieldType.name,
                                 (field.get(this) as Enum<*>).name,
                                 primary)
                    DatabaseObject::class.java.isAssignableFrom(fieldType) ->
                        if (primary)
                        {
                            throw IllegalStateException(
                                "The field ${field.name} of database object ${this.javaClass.name} is ${fieldType.name} and marked as primary, that is not possible (Choose Int, Long, String or Enum for primary key)")
                        }
                        else
                        {
                            DataObject(fieldName, fieldType.name,
                                       (field.get(this) as DatabaseObject).obtainDatabaseId())
                        }
                    else                                                   ->
                        throw IllegalStateException(
                            "Database objects don't support type ${fieldType.name} for ${field.name} on ${this.javaClass.name}")
                }

            if (primary)
            {
                primaryValues.add(dataValue)
            }
            else
            {
                otherValues.add(dataValue)
            }
        }

        return DatabaseObjectInfo(this.javaClass.name, primaryValues, otherValues)
    }

    final override fun equals(other: Any?): Boolean
    {
        if (this === other)
        {
            return true
        }

        if (null == other || other !is DatabaseObject || other.javaClass != this.javaClass)
        {
            return false
        }

        val primaryThis = this.collectInfo().primaryValues
        val primaryOther = other.collectInfo().primaryValues

        for (index in primaryThis.indices)
        {
            if (primaryThis[index].value != primaryOther[index].value)
            {
                return false
            }
        }

        return true
    }

    final override fun hashCode(): Int =
        Objects.hash(*this.collectInfo()
            .primaryValues
            .map { primary -> primary.value }
            .toTypedArray())

    override fun toString(): String
    {
        val stringBuilder = StringBuilder()
        stringBuilder.append(this.javaClass.name)
        stringBuilder.append(" [")

        val collectInfo = this.collectInfo()
        var afterFirst = false

        for (primary in collectInfo.primaryValues)
        {
            if (afterFirst)
            {
                stringBuilder.append(" ; ")
            }

            stringBuilder.append(primary.fieldName)
            stringBuilder.append("=")
            stringBuilder.append(primary.value)

            afterFirst = true
        }

        for (other in collectInfo.otherValues)
        {
            if (afterFirst)
            {
                stringBuilder.append(" ; ")
            }

            stringBuilder.append(other.fieldName)
            stringBuilder.append("=")
            stringBuilder.append(other.value)

            afterFirst = true
        }

        stringBuilder.append("]")

        return stringBuilder.toString()
    }
}