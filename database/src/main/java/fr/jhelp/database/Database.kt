/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import fr.jhelp.database.condition.ConditionResult
import fr.jhelp.database.condition.DataCondition
import fr.jhelp.lists.Queue
import fr.jhelp.utilities.logError

internal const val TABLE_OBJECTS = "Objects"
internal const val TABLE_FIELDS = "Fields"

internal const val COLUMN_ID = "ID"
internal const val COLUMN_CLASS_NAME = "ClassName"
internal const val COLUMN_NAME = "Name"
internal const val COLUMN_OBJECT_ID = "ObjectID"
internal const val COLUMN_TYPE = "Type"
internal const val COLUMN_VALUE_INTEGER = "ValueInteger"
internal const val COLUMN_VALUE_NUMBER = "ValueNumber"
internal const val COLUMN_VALUE_TEXT = "ValueText"
internal const val COLUMN_VALUE_OBJECT_ID = "ValueObjectID"

private val SELECT_ID = arrayOf(COLUMN_ID)
private val SELECT_ID_CLASS_NAME = arrayOf(COLUMN_ID, COLUMN_CLASS_NAME)
private val SELECT_TYPE_VALUE_INTEGER = arrayOf(COLUMN_TYPE, COLUMN_VALUE_INTEGER)
private val SELECT_TYPE_VALUE_NUMBER = arrayOf(COLUMN_TYPE, COLUMN_VALUE_NUMBER)
private val SELECT_TYPE_VALUE_TEXT = arrayOf(COLUMN_TYPE, COLUMN_VALUE_TEXT)
private val SELECT_TYPE_VALUE_OBJECT_ID = arrayOf(COLUMN_TYPE, COLUMN_VALUE_OBJECT_ID)
private val SELECT_CLASS_NAME = arrayOf(COLUMN_CLASS_NAME)
private val SELECT_VALUE_OBJECT_ID = arrayOf(COLUMN_VALUE_OBJECT_ID)
private val SELECT_NAME = arrayOf(COLUMN_NAME)
private val SELECT_NAME_TYPE_VALUES =
    arrayOf(COLUMN_NAME, COLUMN_TYPE, COLUMN_VALUE_INTEGER, COLUMN_VALUE_NUMBER, COLUMN_VALUE_TEXT,
            COLUMN_VALUE_OBJECT_ID)

private const val NAME_TYPE_VALUES_INDEX_NAME = 0
private const val NAME_TYPE_VALUES_INDEX_TYPE = 1
private const val NAME_TYPE_VALUES_INDEX_INTEGER = 2
private const val NAME_TYPE_VALUES_INDEX_NUMBER = 3
private const val NAME_TYPE_VALUES_INDEX_TEXT = 4
private const val NAME_TYPE_VALUES_INDEX_OBJECT_ID = 5

private const val WHERE_ID = "$COLUMN_ID=?"
private const val WHERE_OBJECT_ID = "$COLUMN_OBJECT_ID=?"
private const val WHERE_NAME = "$COLUMN_NAME=?"
private const val WHERE_OBJECT_ID_AND_NAME = "$COLUMN_OBJECT_ID=? AND $COLUMN_NAME=?"
private const val WHERE_TYPE_AND_VALUE_OBJECT_ID = "$COLUMN_TYPE=? AND $COLUMN_VALUE_OBJECT_ID=?"
private const val WHERE_OBJECT_ID_AND_TYPE = "$COLUMN_OBJECT_ID=? AND $COLUMN_TYPE=?"

internal const val ID_NEW_DATA = -1L

class Database(context: Context, name: String)
{
    private val database: SQLiteDatabase =
        context.openOrCreateDatabase(name, Context.MODE_PRIVATE, null)

    init
    {
        this.createTables()
    }

    fun <DS : DataStorable> store(key: String, dataStorable: DS)
    {
        if (key.isEmpty())
        {
            throw IllegalArgumentException("Key must not be empty")
        }

        this.updateID(dataStorable)

        if (dataStorable.databaseID == ID_NEW_DATA)
        {
            var id = ID_NEW_DATA
            var className = ""
            val cursor = this.database.query(TABLE_OBJECTS, SELECT_ID_CLASS_NAME,
                                             WHERE_NAME, arrayOf(key),
                                             null, null, null,
                                             "1")

            if (cursor.moveToNext())
            {
                id = cursor.getLong(0)
                className = cursor.getString(1)
            }

            cursor.close()

            if (id != ID_NEW_DATA && className != dataStorable.javaClass.name)
            {
                throw IllegalArgumentException(
                    "Try to update a ${dataStorable.javaClass.name} on a $className")
            }

            dataStorable.databaseID = id
        }

        this.storeIntern(key, dataStorable)
    }

    fun <DS : DataStorable> read(key: String): DS?
    {
        if (key.isEmpty())
        {
            return null
        }

        var id = ID_NEW_DATA
        var className = ""
        val cursor = this.database.query(TABLE_OBJECTS, SELECT_ID_CLASS_NAME,
                                         WHERE_NAME, arrayOf(key),
                                         null, null, null,
                                         "1")
        if (cursor.moveToNext())
        {
            id = cursor.getLong(0)
            className = cursor.getString(1)
        }

        cursor.close()

        if (id == ID_NEW_DATA)
        {
            return null
        }

        return try
        {
            val dataStorable = Class.forName(className).newInstance() as DS
            dataStorable.databaseID = id
            this.fill(dataStorable)
            dataStorable
        }
        catch (_: Exception)
        {
            null
        }

    }

    fun delete(key: String)
    {
        if (key.isEmpty())
        {
            return
        }

        var id = ID_NEW_DATA

        var cursor = this.database.query(TABLE_OBJECTS, SELECT_ID,
                                         WHERE_NAME, arrayOf(key),
                                         null, null, null,
                                         "1")

        if (cursor.moveToNext())
        {
            id = cursor.getLong(0)
        }

        cursor.close()

        if (id == ID_NEW_DATA)
        {
            return
        }

        val references = Queue<Long>()

        cursor = this.database.query(TABLE_FIELDS, SELECT_VALUE_OBJECT_ID,
                                     WHERE_OBJECT_ID_AND_TYPE,
                                     arrayOf(id.toString(), DataType.OBJECT.name),
                                     null, null, null)

        while (cursor.moveToNext())
        {
            references.enqueue(cursor.getLong(0))
        }

        cursor.close()

        this.database.delete(TABLE_OBJECTS, WHERE_ID, arrayOf(id.toString()))
        this.database.delete(TABLE_FIELDS, WHERE_OBJECT_ID, arrayOf(id.toString()))
        this.deleteIfNoMoreReferenced(references)
    }

    fun <DS : DataStorable> select(dataStorableClass: Class<DS>, where: DataCondition,
                                   collector: (DS) -> Unit)
    {
        val mainCursor = this.database.query(TABLE_OBJECTS, SELECT_ID,
                                             "$COLUMN_CLASS_NAME=? AND $COLUMN_NAME!=?",
                                             arrayOf(dataStorableClass.name, ""),
                                             null, null, null)

        while (mainCursor.moveToNext())
        {
            val id = mainCursor.getLong(0)
            val idString = id.toString()
            where.start()
            var result: ConditionResult

            do
            {
                val arguments = ArrayList<String>()
                arguments.add(idString)
                result = where.nextQuery(arguments) { query ->
                    this.testQuery(query, arguments.toTypedArray())
                }
            }
            while (result == ConditionResult.UNKNOWN)

            if (result == ConditionResult.VALID)
            {
                collector(this.readObject<DS>(id)!!)
            }
        }

        mainCursor.close()
    }

    private fun testQuery(query: String, arguments: Array<String>): Long
    {
        var result = ID_NEW_DATA
        val cursor = this.database.rawQuery(query, arguments)


        if (cursor.moveToNext())
        {
            result = cursor.getLong(0)
        }

        cursor.close()
        return result
    }

    fun close()
    {
        this.database.close()
    }

    internal fun readInteger(databaseID: Long, key: String, defaultValue: Long): Long
    {
        var value = defaultValue
        val cursor = this.database.query(TABLE_FIELDS, SELECT_TYPE_VALUE_INTEGER,
                                         WHERE_OBJECT_ID_AND_NAME,
                                         arrayOf(databaseID.toString(), key),
                                         null, null, null,
                                         "1")

        if (cursor.moveToNext())
        {
            val dataType = DataType.valueOf(cursor.getString(0))

            if (dataType == DataType.INTEGER)
            {
                value = cursor.getLong(1)
            }
        }

        cursor.close()
        return value
    }

    internal fun readNumber(databaseID: Long, key: String, defaultValue: Double): Double
    {
        var value = defaultValue
        val cursor = this.database.query(TABLE_FIELDS, SELECT_TYPE_VALUE_NUMBER,
                                         WHERE_OBJECT_ID_AND_NAME,
                                         arrayOf(databaseID.toString(), key),
                                         null, null, null,
                                         "1")

        if (cursor.moveToNext())
        {
            val dataType = DataType.valueOf(cursor.getString(0))

            if (dataType == DataType.NUMBER)
            {
                value = cursor.getDouble(1)
            }
        }

        cursor.close()
        return value
    }

    internal fun readString(databaseID: Long, key: String): String?
    {
        var value: String? = null
        val cursor = this.database.query(TABLE_FIELDS, SELECT_TYPE_VALUE_TEXT,
                                         WHERE_OBJECT_ID_AND_NAME,
                                         arrayOf(databaseID.toString(), key),
                                         null, null, null,
                                         "1")

        if (cursor.moveToNext())
        {
            val dataType = DataType.valueOf(cursor.getString(0))

            if (dataType == DataType.TEXT)
            {
                value = cursor.getString(1)
            }
        }

        cursor.close()
        return value
    }

    internal fun <DS : DataStorable> readObject(databaseID: Long, key: String): DS?
    {
        var id = ID_NEW_DATA
        val cursor = this.database.query(TABLE_FIELDS, SELECT_TYPE_VALUE_OBJECT_ID,
                                         WHERE_OBJECT_ID_AND_NAME,
                                         arrayOf(databaseID.toString(), key),
                                         null, null, null,
                                         "1")

        if (cursor.moveToNext())
        {
            val dataType = DataType.valueOf(cursor.getString(0))

            if (dataType == DataType.OBJECT)
            {
                id = cursor.getLong(1)
            }
        }

        cursor.close()

        if (id == ID_NEW_DATA)
        {
            return null
        }

        return this.readObject(id)
    }

    private fun <DS : DataStorable> readObject(databaseID: Long): DS?
    {
        var className = ""
        val cursor = this.database.query(TABLE_OBJECTS, SELECT_CLASS_NAME,
                                         WHERE_ID, arrayOf(databaseID.toString()),
                                         null, null, null,
                                         "1")

        if (cursor.moveToNext())
        {
            className = cursor.getString(0)
        }

        cursor.close()

        if (className.isEmpty())
        {
            return null
        }

        return try
        {
            val dataStorable = Class.forName(className).newInstance() as DS
            dataStorable.databaseID = databaseID
            this.fill(dataStorable)
            dataStorable
        }
        catch (exception: Exception)
        {
            logError(exception) { "Fail to create object" }
            null
        }
    }

    internal fun write(databaseID: Long, key: String, value: Long)
    {
        val contentValues = ContentValues()
        contentValues.put(COLUMN_OBJECT_ID, databaseID)
        contentValues.put(COLUMN_NAME, key)
        contentValues.put(COLUMN_TYPE, DataType.INTEGER.name)
        contentValues.put(COLUMN_VALUE_INTEGER, value)
        contentValues.put(COLUMN_VALUE_NUMBER, 0.0)
        contentValues.put(COLUMN_VALUE_TEXT, "")
        contentValues.put(COLUMN_VALUE_OBJECT_ID, ID_NEW_DATA)

        val fieldID = this.filedID(databaseID, key)

        if (fieldID == ID_NEW_DATA)
        {
            this.database.insert(TABLE_FIELDS, null, contentValues)
        }
        else
        {
            this.database.update(TABLE_FIELDS, contentValues,
                                 WHERE_ID, arrayOf(fieldID.toString()))
        }
    }

    internal fun write(databaseID: Long, key: String, value: Double)
    {
        val contentValues = ContentValues()
        contentValues.put(COLUMN_OBJECT_ID, databaseID)
        contentValues.put(COLUMN_NAME, key)
        contentValues.put(COLUMN_TYPE, DataType.NUMBER.name)
        contentValues.put(COLUMN_VALUE_INTEGER, 0)
        contentValues.put(COLUMN_VALUE_NUMBER, value)
        contentValues.put(COLUMN_VALUE_TEXT, "")
        contentValues.put(COLUMN_VALUE_OBJECT_ID, ID_NEW_DATA)

        val fieldID = this.filedID(databaseID, key)

        if (fieldID == ID_NEW_DATA)
        {
            this.database.insert(TABLE_FIELDS, null, contentValues)
        }
        else
        {
            this.database.update(TABLE_FIELDS, contentValues,
                                 WHERE_ID, arrayOf(fieldID.toString()))
        }
    }

    internal fun write(databaseID: Long, key: String, value: String)
    {
        val contentValues = ContentValues()
        contentValues.put(COLUMN_OBJECT_ID, databaseID)
        contentValues.put(COLUMN_NAME, key)
        contentValues.put(COLUMN_TYPE, DataType.TEXT.name)
        contentValues.put(COLUMN_VALUE_INTEGER, 0)
        contentValues.put(COLUMN_VALUE_NUMBER, 0.0)
        contentValues.put(COLUMN_VALUE_TEXT, value)
        contentValues.put(COLUMN_VALUE_OBJECT_ID, ID_NEW_DATA)

        val fieldID = this.filedID(databaseID, key)

        if (fieldID == ID_NEW_DATA)
        {
            this.database.insert(TABLE_FIELDS, null, contentValues)
        }
        else
        {
            this.database.update(TABLE_FIELDS, contentValues,
                                 WHERE_ID, arrayOf(fieldID.toString()))
        }
    }

    internal fun <DS : DataStorable> write(databaseID: Long, key: String, value: DS)
    {
        this.updateIdForField(databaseID, key, value)
        this.storeIntern("", value)

        val contentValues = ContentValues()
        contentValues.put(COLUMN_OBJECT_ID, databaseID)
        contentValues.put(COLUMN_NAME, key)
        contentValues.put(COLUMN_TYPE, DataType.OBJECT.name)
        contentValues.put(COLUMN_VALUE_INTEGER, 0)
        contentValues.put(COLUMN_VALUE_NUMBER, 0.0)
        contentValues.put(COLUMN_VALUE_TEXT, "")
        contentValues.put(COLUMN_VALUE_OBJECT_ID, value.databaseID)

        val fieldID = this.filedID(databaseID, key)

        if (fieldID == ID_NEW_DATA)
        {
            this.database.insert(TABLE_FIELDS, null, contentValues)
        }
        else
        {
            this.database.update(TABLE_FIELDS, contentValues,
                                 WHERE_ID, arrayOf(fieldID.toString()))
        }
    }

    internal fun removeField(databaseID: Long, key: String)
    {
        val references = Queue<Long>()
        val cursor = this.database.query(TABLE_FIELDS, SELECT_TYPE_VALUE_OBJECT_ID,
                                         WHERE_OBJECT_ID_AND_NAME,
                                         arrayOf(databaseID.toString(), key),
                                         null, null, null,
                                         "1")

        if (cursor.moveToNext())
        {
            val type = DataType.valueOf(cursor.getString(0))

            if (type == DataType.OBJECT)
            {
                val id = cursor.getLong(1)

                if (id != ID_NEW_DATA)
                {
                    references.enqueue(id)
                }
            }
        }

        cursor.close()

        this.database.delete(TABLE_FIELDS,
                             WHERE_OBJECT_ID_AND_NAME,
                             arrayOf(databaseID.toString(), key))

        this.deleteIfNoMoreReferenced(references)
    }

    private fun <DS : DataStorable> storeIntern(name: String, dataStorable: DS)
    {
        if (dataStorable.databaseID == ID_NEW_DATA)
        {
            val contentValues = ContentValues()
            contentValues.put(COLUMN_CLASS_NAME, dataStorable.javaClass.name)
            contentValues.put(COLUMN_NAME, name)
            dataStorable.databaseID = this.database.insert(TABLE_OBJECTS, null, contentValues)
        }

        try
        {
            dataStorable.writeData(this)
        }
        catch (exception: Exception)
        {
            logError(exception) { "Failed to associate $name with $dataStorable" }
        }
    }

    private fun <DS : DataStorable> fill(dataStorable: DS)
    {
        val toResolve = Queue<Pair<String, Long>>()
        dataStorable.clear()
        val cursor = this.database.query(TABLE_FIELDS, SELECT_NAME_TYPE_VALUES,
                                         WHERE_OBJECT_ID,
                                         arrayOf(dataStorable.databaseID.toString()),
                                         null, null, null)

        while (cursor.moveToNext())
        {
            val key = cursor.getString(NAME_TYPE_VALUES_INDEX_NAME)
            val type = DataType.valueOf(cursor.getString(NAME_TYPE_VALUES_INDEX_TYPE))

            when (type)
            {
                DataType.INTEGER ->
                    dataStorable.put(key, cursor.getLong(NAME_TYPE_VALUES_INDEX_INTEGER))
                DataType.NUMBER  ->
                    dataStorable.put(key, cursor.getDouble(NAME_TYPE_VALUES_INDEX_NUMBER))
                DataType.TEXT    ->
                    dataStorable.put(key, cursor.getString(NAME_TYPE_VALUES_INDEX_TEXT))
                DataType.OBJECT  ->
                    toResolve.enqueue(Pair(key, cursor.getLong(NAME_TYPE_VALUES_INDEX_OBJECT_ID)))
            }
        }

        cursor.close()

        while (toResolve.notEmpty)
        {
            val (key, id) = toResolve.dequeue()
            val parameter: DS? = this.readObject(id)

            if (parameter != null)
            {
                dataStorable.put(key, parameter)
            }
        }
    }

    private fun filedID(databaseID: Long, key: String): Long
    {
        val cursor = this.database.query(TABLE_FIELDS, SELECT_ID,
                                         WHERE_OBJECT_ID_AND_NAME,
                                         arrayOf(databaseID.toString(), key),
                                         null, null, null,
                                         "1")
        val id =
            if (cursor.moveToNext())
            {
                cursor.getLong(0)
            }
            else
            {
                ID_NEW_DATA
            }

        cursor.close()
        return id
    }

    private fun createTables()
    {
        this.database.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_OBJECTS " +
                              "($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                              "$COLUMN_CLASS_NAME TEXT, " +
                              "$COLUMN_NAME TEXT)")

        this.database.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_FIELDS " +
                              "($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                              "$COLUMN_OBJECT_ID INTEGER, " +
                              "$COLUMN_NAME TEXT, " +
                              "$COLUMN_TYPE TEXT, " +
                              "$COLUMN_VALUE_INTEGER INTEGER, " +
                              "$COLUMN_VALUE_NUMBER NUMBER, " +
                              "$COLUMN_VALUE_TEXT TEXT," +
                              "$COLUMN_VALUE_OBJECT_ID INTEGER)")
    }

    private fun <DS : DataStorable> updateIdForField(databaseID: Long, key: String, value: DS)
    {
        this.updateID(value)

        if (value.databaseID != ID_NEW_DATA)
        {
            return
        }

        val cursor = this.database.query(TABLE_FIELDS, SELECT_VALUE_OBJECT_ID,
                                         WHERE_OBJECT_ID_AND_NAME,
                                         arrayOf(databaseID.toString(), key),
                                         null, null, null,
                                         "1")
        if (cursor.moveToNext())
        {
            value.databaseID = cursor.getLong(0)
        }

        cursor.close()
    }

    private fun updateID(dataStorable: DataStorable)
    {
        val id = dataStorable.databaseID

        if (id == ID_NEW_DATA)
        {
            return
        }

        val cursor = this.database.query(TABLE_OBJECTS, SELECT_ID,
                                         WHERE_ID, arrayOf(id.toString()),
                                         null, null, null,
                                         "1")
        val noMoreExists = !cursor.moveToNext()
        cursor.close()

        if (noMoreExists)
        {
            dataStorable.databaseID = ID_NEW_DATA
            this.database.delete(TABLE_FIELDS, WHERE_OBJECT_ID, arrayOf(id.toString()))
        }
    }

    private fun deleteIfNoMoreReferenced(references: Queue<Long>)
    {
        while (references.notEmpty)
        {
            val id = references.dequeue()

            if (this.noMoreReferenced(id))
            {
                this.delete(id, references)
            }
        }
    }

    private fun noMoreReferenced(id: Long): Boolean
    {
        var name = ""
        var cursor = this.database.query(TABLE_OBJECTS, SELECT_NAME,
                                         WHERE_ID, arrayOf(id.toString()),
                                         null, null, null,
                                         "1")
        if (cursor.moveToNext())
        {
            name = cursor.getString(0)
        }

        cursor.close()

        if (name.isNotEmpty())
        {
            return false
        }

        cursor = this.database.query(TABLE_FIELDS, SELECT_ID,
                                     WHERE_TYPE_AND_VALUE_OBJECT_ID,
                                     arrayOf(DataType.OBJECT.name, id.toString()),
                                     null, null, null,
                                     "1")
        val noMoreReferenced = !cursor.moveToNext()
        cursor.close()
        return noMoreReferenced
    }

    private fun delete(id: Long, referencesToUpdate: Queue<Long>)
    {
        this.database.delete(TABLE_OBJECTS, WHERE_ID, arrayOf(id.toString()))

        val cursor = this.database.query(TABLE_FIELDS, SELECT_VALUE_OBJECT_ID,
                                         WHERE_OBJECT_ID_AND_TYPE,
                                         arrayOf(id.toString(), DataType.OBJECT.name),
                                         null, null, null)

        while (cursor.moveToNext())
        {
            referencesToUpdate.enqueue(cursor.getLong(0))
        }

        cursor.close()
        this.database.delete(TABLE_FIELDS, WHERE_OBJECT_ID, arrayOf(id.toString()))
    }
}