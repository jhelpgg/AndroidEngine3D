/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database

internal sealed class Data(val dataType: DataType)
{
    abstract fun writeValue(database: Database, databaseId: Long, key: String)
    abstract fun readValue(database: Database, databaseId: Long, key: String)
}

internal class DataInt(var value: Int) : Data(DataType.INTEGER)
{
    override fun writeValue(database: Database, databaseId: Long, key: String)
    {
        database.write(databaseId, key, this.value.toLong())
    }

    override fun readValue(database: Database, databaseId: Long, key: String)
    {
        this.value = database.readInteger(databaseId, key, 0L).toInt()
    }
}

internal class DataLong(var value: Long) : Data(DataType.INTEGER)
{
    override fun writeValue(database: Database, databaseId: Long, key: String)
    {
        database.write(databaseId, key, this.value)
    }

    override fun readValue(database: Database, databaseId: Long, key: String)
    {
        this.value = database.readInteger(databaseId, key, 0L)
    }
}

internal class DataFloat(var value: Float) : Data(DataType.NUMBER)
{
    override fun writeValue(database: Database, databaseId: Long, key: String)
    {
        database.write(databaseId, key, this.value.toDouble())
    }

    override fun readValue(database: Database, databaseId: Long, key: String)
    {
        this.value = database.readNumber(databaseId, key, 0.0).toFloat()
    }
}

internal class DataDouble(var value: Double) : Data(DataType.NUMBER)
{
    override fun writeValue(database: Database, databaseId: Long, key: String)
    {
        database.write(databaseId, key, this.value)
    }

    override fun readValue(database: Database, databaseId: Long, key: String)
    {
        this.value = database.readNumber(databaseId, key, 0.0)
    }
}

internal class DataString(var value: String?) : Data(DataType.TEXT)
{
    override fun writeValue(database: Database, databaseId: Long, key: String)
    {
        this.value?.let { value -> database.write(databaseId, key, value) }
    }

    override fun readValue(database: Database, databaseId: Long, key: String)
    {
        this.value = database.readString(databaseId, key)
    }
}

internal class DataObject(var value: DataStorable?) : Data(DataType.OBJECT)
{
    override fun writeValue(database: Database, databaseId: Long, key: String)
    {
        this.value?.let { value -> database.write(databaseId, key, value) }
    }

    override fun readValue(database: Database, databaseId: Long, key: String)
    {
        this.value = database.readObject(databaseId, key)
    }
}