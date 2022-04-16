/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database

import android.content.ContentValues

internal sealed class DataValue<V : Any>(val fieldName: String, val type: DataType, val value: V,
                                         val primary: Boolean)
{
    fun putIn(contentValues: ContentValues)
    {
        contentValues.put(DatabaseNoSQL.COLUMN_NAME, this.fieldName)
        contentValues.put(DatabaseNoSQL.COLUMN_TYPE, this.type.name)
        this.putInValue(contentValues)
    }

    fun select(operator: String = "="): String
    {
        val stringBuilder = StringBuilder()
        this.select(stringBuilder, operator)
        return stringBuilder.toString()
    }


    fun select(stringBuilder: StringBuilder, operator: String = "=")
    {
        stringBuilder.append("SELECT (")
        stringBuilder.append(DatabaseNoSQL.COLUMN_OBJECT_ID)
        stringBuilder.append(") FROM ")
        stringBuilder.append(DatabaseNoSQL.TABLE_FIELDS)
        stringBuilder.append(" WHERE ")
        this.where(stringBuilder, operator)
    }

    fun where(stringBuilder: StringBuilder, operator: String = "=")
    {
        this.whereHeader(stringBuilder)
        this.whereValue(stringBuilder, operator)
    }

    protected abstract fun putInValue(contentValues: ContentValues)

    protected abstract fun whereValue(stringBuilder: StringBuilder, operator: String)

    protected fun whereHeader(stringBuilder: StringBuilder)
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
    }
}

internal class DataBoolean(fieldName: String, value: Boolean) :
    DataValue<Boolean>(fieldName, DataType.BOOLEAN, value, false)
{
    override fun putInValue(contentValues: ContentValues)
    {
        contentValues.put(DatabaseNoSQL.COLUMN_VALUE_INTEGER, if (this.value) 1 else 0)
    }

    override fun whereValue(stringBuilder: StringBuilder, operator: String)
    {
        stringBuilder.append(DatabaseNoSQL.COLUMN_VALUE_INTEGER)
        stringBuilder.append("=")
        stringBuilder.append(if (this.value) 1 else 0)
    }
}

internal class DataInt(fieldName: String, value: Int, primary: Boolean) :
    DataValue<Int>(fieldName, DataType.INT, value, primary)
{
    override fun putInValue(contentValues: ContentValues)
    {
        contentValues.put(DatabaseNoSQL.COLUMN_VALUE_INTEGER, this.value)
    }

    override fun whereValue(stringBuilder: StringBuilder, operator: String)
    {
        stringBuilder.append(DatabaseNoSQL.COLUMN_VALUE_INTEGER)
        stringBuilder.append(operator)
        stringBuilder.append(this.value)
    }

    fun whereOr(stringBuilder: StringBuilder,
                operatorThis: String,
                otherValue: Int, operatorOther: String)
    {
        this.whereHeader(stringBuilder)
        stringBuilder.append("(")
        this.whereValue(stringBuilder, operatorThis)
        stringBuilder.append(" OR ")
        stringBuilder.append(DatabaseNoSQL.COLUMN_VALUE_INTEGER)
        stringBuilder.append(operatorOther)
        stringBuilder.append(otherValue)
        stringBuilder.append(")")
    }
}

internal class DataLong(fieldName: String, value: Long, primary: Boolean) :
    DataValue<Long>(fieldName, DataType.LONG, value, primary)
{
    override fun putInValue(contentValues: ContentValues)
    {
        contentValues.put(DatabaseNoSQL.COLUMN_VALUE_INTEGER, this.value)
    }

    override fun whereValue(stringBuilder: StringBuilder, operator: String)
    {
        stringBuilder.append(DatabaseNoSQL.COLUMN_VALUE_INTEGER)
        stringBuilder.append(operator)
        stringBuilder.append(this.value)
    }

    fun whereOr(stringBuilder: StringBuilder,
                operatorThis: String,
                otherValue: Long, operatorOther: String)
    {
        this.whereHeader(stringBuilder)
        stringBuilder.append("(")
        this.whereValue(stringBuilder, operatorThis)
        stringBuilder.append(" OR ")
        stringBuilder.append(DatabaseNoSQL.COLUMN_VALUE_INTEGER)
        stringBuilder.append(operatorOther)
        stringBuilder.append(otherValue)
        stringBuilder.append(")")
    }
}

internal class DataFloat(fieldName: String, value: Float) :
    DataValue<Float>(fieldName, DataType.FLOAT, value, false)
{
    override fun putInValue(contentValues: ContentValues)
    {
        contentValues.put(DatabaseNoSQL.COLUMN_VALUE_NUMBER, this.value)
    }

    override fun whereValue(stringBuilder: StringBuilder, operator: String)
    {
        stringBuilder.append(DatabaseNoSQL.COLUMN_VALUE_NUMBER)
        stringBuilder.append(operator)
        stringBuilder.append(this.value)
    }

    fun whereOr(stringBuilder: StringBuilder,
                operatorThis: String,
                otherValue: Float, operatorOther: String)
    {
        this.whereHeader(stringBuilder)
        stringBuilder.append("(")
        this.whereValue(stringBuilder, operatorThis)
        stringBuilder.append(" OR ")
        stringBuilder.append(DatabaseNoSQL.COLUMN_VALUE_NUMBER)
        stringBuilder.append(operatorOther)
        stringBuilder.append(otherValue)
        stringBuilder.append(")")
    }
}

internal class DataDouble(fieldName: String, value: Double) :
    DataValue<Double>(fieldName, DataType.DOUBLE, value, false)
{
    override fun putInValue(contentValues: ContentValues)
    {
        contentValues.put(DatabaseNoSQL.COLUMN_VALUE_NUMBER, this.value)
    }

    override fun whereValue(stringBuilder: StringBuilder, operator: String)
    {
        stringBuilder.append(DatabaseNoSQL.COLUMN_VALUE_NUMBER)
        stringBuilder.append(operator)
        stringBuilder.append(this.value)
    }

    fun whereOr(stringBuilder: StringBuilder,
                operatorThis: String,
                otherValue: Double, operatorOther: String)
    {
        this.whereHeader(stringBuilder)
        stringBuilder.append("(")
        this.whereValue(stringBuilder, operatorThis)
        stringBuilder.append(" OR ")
        stringBuilder.append(DatabaseNoSQL.COLUMN_VALUE_NUMBER)
        stringBuilder.append(operatorOther)
        stringBuilder.append(otherValue)
        stringBuilder.append(")")
    }
}

internal class DataString(fieldName: String, value: String, primary: Boolean) :
    DataValue<String>(fieldName, DataType.STRING, value, primary)
{
    override fun putInValue(contentValues: ContentValues)
    {
        contentValues.put(DatabaseNoSQL.COLUMN_VALUE_TEXT, this.value)
    }

    override fun whereValue(stringBuilder: StringBuilder, operator: String)
    {
        stringBuilder.append(DatabaseNoSQL.COLUMN_VALUE_TEXT)
        stringBuilder.append(operator)
        stringBuilder.append("'")
        stringBuilder.append(this.value)
        stringBuilder.append("'")
    }

    fun whereOr(stringBuilder: StringBuilder,
                operatorThis: String,
                otherValue: String, operatorOther: String)
    {
        this.whereHeader(stringBuilder)
        stringBuilder.append("(")
        this.whereValue(stringBuilder, operatorThis)
        stringBuilder.append(" OR ")
        stringBuilder.append(DatabaseNoSQL.COLUMN_VALUE_TEXT)
        stringBuilder.append(operatorOther)
        stringBuilder.append("'")
        stringBuilder.append(otherValue)
        stringBuilder.append("'")
        stringBuilder.append(")")
    }
}

internal class DataEnum(fieldName: String, className: String, value: String, primary: Boolean) :
    DataValue<Pair<String, String>>(fieldName, DataType.ENUM, Pair(className, value), primary)
{
    override fun putInValue(contentValues: ContentValues)
    {
        contentValues.put(DatabaseNoSQL.COLUMN_VALUE_TEXT,
                          "${this.value.first}|${this.value.second}")
    }

    override fun whereValue(stringBuilder: StringBuilder, operator: String)
    {
        stringBuilder.append(DatabaseNoSQL.COLUMN_VALUE_TEXT)
        stringBuilder.append("='")
        stringBuilder.append(this.value.first)
        stringBuilder.append("|")
        stringBuilder.append(this.value.second)
        stringBuilder.append("'")
    }
}

internal class DataObject(fieldName: String, className: String, value: Long) :
    DataValue<Pair<String, Long>>(fieldName, DataType.OBJECT, Pair(className, value), false)
{
    override fun putInValue(contentValues: ContentValues)
    {
        contentValues.put(DatabaseNoSQL.COLUMN_VALUE_INTEGER, this.value.second)
        contentValues.put(DatabaseNoSQL.COLUMN_VALUE_TEXT, this.value.first)
    }

    override fun whereValue(stringBuilder: StringBuilder, operator: String)
    {
        stringBuilder.append(DatabaseNoSQL.COLUMN_VALUE_INTEGER)
        stringBuilder.append("=")
        stringBuilder.append(this.value.second)

        stringBuilder.append(" AND ")

        stringBuilder.append(DatabaseNoSQL.COLUMN_VALUE_TEXT)
        stringBuilder.append("='")
        stringBuilder.append(this.value.first)
        stringBuilder.append("'")
    }
}
