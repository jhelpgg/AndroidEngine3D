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
import androidx.annotation.VisibleForTesting
import fr.jhelp.database.query.DatabaseQuery
import fr.jhelp.io.StringInputStream
import fr.jhelp.io.StringOutputStream
import fr.jhelp.io.createFile
import fr.jhelp.io.deleteFull
import fr.jhelp.io.extensions.readDouble
import fr.jhelp.io.extensions.readLong
import fr.jhelp.io.extensions.readString
import fr.jhelp.io.extensions.writeDouble
import fr.jhelp.io.extensions.writeLong
import fr.jhelp.io.extensions.writeString
import fr.jhelp.provided.provided
import fr.jhelp.security.decrypt
import fr.jhelp.security.encrypt
import fr.jhelp.tasks.ThreadType
import fr.jhelp.tasks.chain.Emitter
import fr.jhelp.tasks.chain.EmitterSource
import fr.jhelp.tasks.observable.Observable
import fr.jhelp.tasks.observable.ObservableValue
import fr.jhelp.tasks.parallel
import fr.jhelp.tasks.promise.FutureResult
import fr.jhelp.tasks.promise.Promise
import fr.jhelp.utilities.random
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.reflect.jvm.kotlinFunction

/**
 * It supposed application [Context] is provide by [fr.jhelp.provided.provideSingle] or [fr.jhelp.provided.provideMultiple]
 */
object DatabaseNoSQL
{
    internal const val NO_DATABASE_ID = -1L

    private const val NOSQL_DIRECTORY = "nosql"

    internal const val TABLE_OBJECTS = "Objects"
    internal const val COLUMN_ID = "ID"
    internal const val COLUMN_CLASS_NAME = "ClassName"

    internal const val TABLE_FIELDS = "Fields"
    internal const val COLUMN_NAME = "FieldName"
    internal const val COLUMN_TYPE = "Type"
    internal const val COLUMN_VALUE_INTEGER = "ValueInteger"
    internal const val COLUMN_VALUE_NUMBER = "ValueNumber"
    internal const val COLUMN_VALUE_TEXT = "ValueText"
    internal const val COLUMN_OBJECT_ID = "ObjectID"
    internal const val databaseName = "NoSQL"
    internal const val suffix = "_data_"

    private fun newDataFileName(): String =
        "$databaseName$suffix${random(0L, Long.MAX_VALUE - 1L)}"

    private val closedObservableValue = ObservableValue<Boolean>(false)
    val closedObservable: Observable<Boolean> = this.closedObservableValue.observable
    private val alive = AtomicBoolean(true)
    private val waiting = AtomicBoolean(false)
    private val saving = AtomicBoolean(false)
    private val dataDirty = AtomicBoolean(false)
    private val lock = Object()
    private val context by provided<Context>()
    private lateinit var databaseFile: File
    private lateinit var promiseClose: Promise<Unit>
    private var databaseFuture: FutureResult<SQLiteDatabase> =
        {
            val database = SQLiteDatabase.create(null)
            this.loadDatabase(database)
            database
        }.parallel(ThreadType.IO)


    fun open()
    {
        if (this.closedObservableValue.value && this.alive.compareAndSet(false, true))
        {
            this.closedObservableValue.value = false
            this.waiting.set(false)
            this.saving.set(false)
            this.dataDirty.set(false)
            this.databaseFuture =
                {
                    val database = SQLiteDatabase.create(null)
                    this.loadDatabase(database)
                    database
                }.parallel(ThreadType.IO)
        }
    }

    fun store(databaseObject: DatabaseObject): FutureResult<Unit> =
        this.databaseFuture.and(ThreadType.IO) { database ->
            this.store(databaseObject, database)
        }

    inline fun <reified DO : DatabaseObject> query(subQuery: DatabaseQuery<DO>.() -> Unit)
            : Emitter<DO>
    {
        val query = DatabaseQuery<DO>(DO::class.java)
        query.subQuery()
        return this.executeQuery(DO::class.java, query)
    }

    fun remove(databaseObject: DatabaseObject): FutureResult<Unit> =
        this.databaseFuture.and(ThreadType.IO) { database ->
            database.delete(TABLE_OBJECTS, "$COLUMN_ID=?",
                            arrayOf(databaseObject.databaseId.toString()))
        }

    fun <DO : DatabaseObject> executeQuery(objectClass: Class<DO>,
                                           query: DatabaseQuery<DO>): EmitterSource<DO>
    {
        val emitter: EmitterSource<DO> = EmitterSource<DO>()

        this.databaseFuture.and(ThreadType.IO) { database ->
            val cursorID = database.rawQuery(query.query(), null)

            try
            {
                while (cursorID.moveToNext())
                {
                    val id = cursorID.getLong(0)
                    val constructorObject = objectClass.constructors[0]
                    val parametersKotlin = constructorObject.kotlinFunction!!.parameters
                    val parametersJava = constructorObject.parameters
                    val values = Array<Any>(parametersKotlin.size) { index ->
                        this.getValue(database, parametersKotlin[index].name!!,
                                      dataTypeFrom(parametersJava[index].type), id)
                    }

                    val databaseObject = constructorObject.newInstance(*values) as DO
                    databaseObject.databaseId = id
                    emitter.enqueue(databaseObject)
                }
            }
            catch (exception: Exception)
            {
                exception.printStackTrace()
            }
            finally
            {
                cursorID.close()
            }

            emitter.finish()
        }

        return emitter
    }

    private fun getValue(database: SQLiteDatabase, parameterName: String, dataType: DataType,
                         id: Long): Any =
        when (dataType)
        {
            DataType.BOOLEAN -> this.getBoolean(database, parameterName, id)
            DataType.INT     -> this.getInt(database, parameterName, id)
            DataType.LONG    -> this.getLong(database, parameterName, id)
            DataType.FLOAT   -> this.getFloat(database, parameterName, id)
            DataType.DOUBLE  -> this.getDouble(database, parameterName, id)
            DataType.STRING  -> this.getString(database, parameterName, id)
            DataType.ENUM    -> this.getEnum(database, parameterName, id)
            DataType.OBJECT  -> this.getObject(database, parameterName, id)
        }

    private fun getBoolean(database: SQLiteDatabase, parameterName: String, id: Long): Boolean
    {
        var value = false
        val cursor = database.query(TABLE_FIELDS,
                                    arrayOf(COLUMN_VALUE_INTEGER),
                                    "$COLUMN_NAME=? AND $COLUMN_OBJECT_ID=?",
                                    arrayOf(parameterName, id.toString()),
                                    null, null, null, "1")

        if (cursor.moveToNext())
        {
            value = cursor.getInt(0) == 1
        }

        cursor.close()
        return value
    }

    private fun getInt(database: SQLiteDatabase, parameterName: String, id: Long): Int
    {
        var value = 0
        val cursor = database.query(TABLE_FIELDS,
                                    arrayOf(COLUMN_VALUE_INTEGER),
                                    "$COLUMN_NAME=? AND $COLUMN_OBJECT_ID=?",
                                    arrayOf(parameterName, id.toString()),
                                    null, null, null, "1")

        if (cursor.moveToNext())
        {
            value = cursor.getInt(0)
        }

        cursor.close()
        return value
    }

    private fun getLong(database: SQLiteDatabase, parameterName: String, id: Long): Long
    {
        var value = 0L
        val cursor = database.query(TABLE_FIELDS,
                                    arrayOf(COLUMN_VALUE_INTEGER),
                                    "$COLUMN_NAME=? AND $COLUMN_OBJECT_ID=?",
                                    arrayOf(parameterName, id.toString()),
                                    null, null, null, "1")

        if (cursor.moveToNext())
        {
            value = cursor.getLong(0)
        }

        cursor.close()
        return value
    }

    private fun getFloat(database: SQLiteDatabase, parameterName: String, id: Long): Float
    {
        var value = 0f
        val cursor = database.query(TABLE_FIELDS,
                                    arrayOf(COLUMN_VALUE_NUMBER),
                                    "$COLUMN_NAME=? AND $COLUMN_OBJECT_ID=?",
                                    arrayOf(parameterName, id.toString()),
                                    null, null, null, "1")

        if (cursor.moveToNext())
        {
            value = cursor.getFloat(0)
        }

        cursor.close()
        return value
    }

    private fun getDouble(database: SQLiteDatabase, parameterName: String, id: Long): Double
    {
        var value = 0.0
        val cursor = database.query(TABLE_FIELDS,
                                    arrayOf(COLUMN_VALUE_NUMBER),
                                    "$COLUMN_NAME=? AND $COLUMN_OBJECT_ID=?",
                                    arrayOf(parameterName, id.toString()),
                                    null, null, null, "1")

        if (cursor.moveToNext())
        {
            value = cursor.getDouble(0)
        }

        cursor.close()
        return value
    }

    private fun getString(database: SQLiteDatabase, parameterName: String, id: Long): String
    {
        var value = ""
        val cursor = database.query(TABLE_FIELDS,
                                    arrayOf(COLUMN_VALUE_TEXT),
                                    "$COLUMN_NAME=? AND $COLUMN_OBJECT_ID=?",
                                    arrayOf(parameterName, id.toString()),
                                    null, null, null, "1")

        if (cursor.moveToNext())
        {
            value = cursor.getString(0)
        }

        cursor.close()
        return value
    }

    private fun getEnum(database: SQLiteDatabase, parameterName: String, id: Long): Enum<*>
    {
        var value: Enum<*>? = null
        val cursor = database.query(TABLE_FIELDS,
                                    arrayOf(COLUMN_VALUE_TEXT),
                                    "$COLUMN_NAME=? AND $COLUMN_OBJECT_ID=?",
                                    arrayOf(parameterName, id.toString()),
                                    null, null, null, "1")

        if (cursor.moveToNext())
        {
            val enumValue = cursor.getString(0)
            val index = enumValue.indexOf('|')

            if (index > 0)
            {
                val enumClass = Class.forName(enumValue.substring(0, index))
                val name = enumValue.substring(index + 1)
                value = enumClass
                    .getMethod("valueOf", String::class.java)
                    .invoke(null, name) as Enum<*>
            }
        }

        cursor.close()
        return value!!
    }

    private fun getObject(database: SQLiteDatabase, parameterName: String, id: Long): DatabaseObject
    {
        var value: DatabaseObject? = null
        val cursor = database.query(TABLE_FIELDS,
                                    arrayOf(COLUMN_VALUE_INTEGER, COLUMN_VALUE_TEXT),
                                    "$COLUMN_NAME=? AND $COLUMN_OBJECT_ID=?",
                                    arrayOf(parameterName, id.toString()),
                                    null, null, null, "1")

        try
        {
            if (cursor.moveToNext())
            {
                val objectID = cursor.getLong(0)
                val className = cursor.getString(1)
                val objectClass = Class.forName(className)
                val constructorObject = objectClass.constructors[0]
                val parametersKotlin = constructorObject.kotlinFunction!!.parameters
                val parametersJava = constructorObject.parameters
                val values = Array<Any>(parametersKotlin.size) { index ->
                    this.getValue(database, parametersKotlin[index].name!!,
                                  dataTypeFrom(parametersJava[index].type), objectID)
                }

                value = constructorObject.newInstance(*values) as DatabaseObject
                value.databaseId = objectID
            }
        }
        catch (exception: Exception)
        {
            exception.printStackTrace()
        }
        finally
        {
            cursor.close()
        }

        return value!!
    }

    private fun store(databaseObject: DatabaseObject, database: SQLiteDatabase)
    {
        val (className, primaries, others) = databaseObject.collectInfo()

        if (databaseObject.databaseId == NO_DATABASE_ID && primaries.isNotEmpty())
        {
            val request = StringBuilder()
            request.append(COLUMN_CLASS_NAME)
            request.append("='")
            request.append(databaseObject.javaClass.name)
            request.append("'")

            for (primary in primaries)
            {
                request.append(" AND ")
                request.append(COLUMN_ID)
                request.append(" IN (")
                primary.select(request)
                request.append(")")
            }

            val cursor = database.query(TABLE_OBJECTS, arrayOf(COLUMN_ID),
                                        request.toString(),
                                        null, null, null, null,
                                        "1")

            if (cursor.moveToNext())
            {
                databaseObject.databaseId = cursor.getLong(0)
            }

            cursor.close()
        }

        database.beginTransaction()

        try
        {
            if (databaseObject.databaseId == NO_DATABASE_ID)
            {
                val contentValues = ContentValues()
                contentValues.put(COLUMN_CLASS_NAME, className)
                database.insert(TABLE_OBJECTS, null, contentValues)
                contentValues.clear()

                val cursorId =
                    database.query(TABLE_OBJECTS, arrayOf(COLUMN_ID), null, null, null, null,
                                   "$COLUMN_ID DESC", "1")

                if (cursorId.moveToNext())
                {
                    databaseObject.databaseId = cursorId.getLong(0)
                }

                cursorId.close()

                for (primary in primaries)
                {
                    contentValues.clear()
                    primary.putIn(contentValues)
                    contentValues.put(COLUMN_OBJECT_ID, databaseObject.databaseId)
                    database.insert(TABLE_FIELDS, null, contentValues)
                }

                for (other in others)
                {
                    contentValues.clear()
                    other.putIn(contentValues)
                    contentValues.put(COLUMN_OBJECT_ID, databaseObject.databaseId)
                    database.insert(TABLE_FIELDS, null, contentValues)
                }
            }
            else
            {
                val contentValues = ContentValues()

                for (other in others)
                {
                    contentValues.clear()
                    other.putIn(contentValues)
                    database.update(TABLE_FIELDS, contentValues,
                                    "$COLUMN_NAME=? AND $COLUMN_OBJECT_ID=?",
                                    arrayOf(other.fieldName, databaseObject.databaseId.toString()))
                }
            }

            database.setTransactionSuccessful()
        }
        finally
        {
            database.endTransaction()
        }

        this.wakeup()
    }

    fun close()
    {
        if (this.alive.compareAndSet(true, false))
        {
            this.databaseFuture.waitComplete()
            this.promiseClose = Promise<Unit>()
            this.wakeup()
            this.promiseClose.future.waitComplete()
        }
    }

    private fun wakeup()
    {
        if (this.saving.compareAndSet(false, true))
        {
            this.databaseFuture.and(ThreadType.IO, this::saveDatabase)
            return
        }

        this.dataDirty.set(true)

        if (this.waiting.get())
        {
            synchronized(this.lock)
            {
                this.lock.notify()
            }
        }
    }

    private fun loadDatabase(database: SQLiteDatabase)
    {
        this.databaseFile = File(this.context.filesDir, "$NOSQL_DIRECTORY/${this.databaseName}")
        this.databaseFile.createFile()
        this.createTables(database)
        this.fillDatabase(database)
    }

    private fun createTables(database: SQLiteDatabase)
    {
        database.execSQL("PRAGMA foreign_keys = ON")

        database.execSQL(
            """
            CREATE TABLE $TABLE_OBJECTS 
            (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CLASS_NAME TEXT NOT NULL
            )
            """.trimIndent())

        database.execSQL(
            """
            CREATE TABLE $TABLE_FIELDS 
            (
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_TYPE TEXT NOT NULL,
                $COLUMN_VALUE_INTEGER INTEGER DEFAULT 0,
                $COLUMN_VALUE_NUMBER NUMBER DEFAULT 0,
                $COLUMN_VALUE_TEXT TEXT DEFAULT ''  NOT NULL,
                $COLUMN_OBJECT_ID INTEGER NOT NULL,
                FOREIGN KEY($COLUMN_OBJECT_ID) REFERENCES $TABLE_OBJECTS($COLUMN_ID) ON DELETE CASCADE         
            )
            """.trimIndent())
    }

    private fun fillDatabase(database: SQLiteDatabase)
    {
        if (this.databaseFile.length() == 0L)
        {
            return
        }

        try
        {
            val stringOutputStream = StringOutputStream()
            decrypt({ FileInputStream(this.databaseFile) }, { stringOutputStream })
            val fileName = stringOutputStream.string

            if (fileName.isEmpty())
            {
                throw IllegalStateException("File name is empty in non empty file !!!")
            }

            val fileData =
                File(this.context.filesDir, "$NOSQL_DIRECTORY/${stringOutputStream.string}")

            if (!fileData.exists())
            {
                throw IllegalStateException("Warning $fileName was removed")
            }

            val directory = File(this.context.filesDir, NOSQL_DIRECTORY)
            val nameHeader = "${this.databaseName}$suffix"
            val dataName = fileData.name
            val files =
                directory.listFiles { _, name -> name != dataName && name.startsWith(nameHeader) }
                ?: emptyArray()

            for (file in files)
            {
                file.deleteFull()
            }

            val byteArrayOutputStream = ByteArrayOutputStream()
            decrypt({ FileInputStream(fileData) },
                    { byteArrayOutputStream })
            fillDatabase(ByteArrayInputStream(byteArrayOutputStream.toByteArray()), database)
        }
        catch (exception: Exception)
        {
            // TODO think about action if failed
            exception.printStackTrace()
        }
    }

    private fun fillDatabase(inputStream: InputStream, database: SQLiteDatabase)
    {
        var id = inputStream.readLong()

        while (id != NO_DATABASE_ID)
        {
            database.beginTransaction()

            try
            {
                val contentValues = ContentValues()
                contentValues.put(COLUMN_ID, id)
                contentValues.put(COLUMN_CLASS_NAME, inputStream.readString())
                database.insert(TABLE_OBJECTS, null, contentValues)

                var fieldName = inputStream.readString()

                while (fieldName.isNotEmpty())
                {
                    contentValues.clear()
                    contentValues.put(COLUMN_NAME, fieldName)
                    contentValues.put(COLUMN_TYPE, inputStream.readString())
                    contentValues.put(COLUMN_VALUE_INTEGER, inputStream.readLong())
                    contentValues.put(COLUMN_VALUE_NUMBER, inputStream.readDouble())
                    contentValues.put(COLUMN_VALUE_TEXT, inputStream.readString())
                    contentValues.put(COLUMN_OBJECT_ID, id)
                    database.insert(TABLE_FIELDS, null, contentValues)

                    fieldName = inputStream.readString()
                }

                database.setTransactionSuccessful()
            }
            catch (exception: Exception)
            {
                exception.printStackTrace()
            }
            finally
            {
                database.endTransaction()
            }

            id = inputStream.readLong()
        }

        inputStream.close()
    }

    private fun saveDatabase(database: SQLiteDatabase)
    {
        while (this.alive.get() || this.dataDirty.get())
        {
            this.dataDirty.set(false)

            try
            {
                this.realSave(database)
            }
            catch (exception: Exception)
            {
                exception.printStackTrace()
            }

            if (!this.dataDirty.get() && this.alive.get())
            {
                synchronized(this.lock)
                {
                    this.waiting.set(true)
                    this.lock.wait()
                    this.waiting.set(false)
                }
            }
        }

        database.close()
        this.closedObservableValue.value = true
        this.promiseClose.result(Unit)
    }

    private fun realSave(database: SQLiteDatabase)
    {
        val newFile = File(this.context.filesDir, "$NOSQL_DIRECTORY/${this.newDataFileName()}")
        val byteArrayOutputStream = ByteArrayOutputStream()
        this.save(byteArrayOutputStream, database)
        encrypt({ ByteArrayInputStream(byteArrayOutputStream.toByteArray()) },
                { FileOutputStream(newFile) })

        encrypt({ StringInputStream(newFile.name) },
                { FileOutputStream(this.databaseFile) })

        val directory = File(this.context.filesDir, NOSQL_DIRECTORY)
        val nameHeader = "${this.databaseName}$suffix"
        val dataName = newFile.name
        val files =
            directory.listFiles { _, name -> name != dataName && name.startsWith(nameHeader) }
            ?: emptyArray()

        for (file in files)
        {
            file.deleteFull()
        }
    }

    private fun save(outputStream: OutputStream, database: SQLiteDatabase)
    {
        val cursorObject = database.query(TABLE_OBJECTS,
                                          arrayOf(COLUMN_ID, COLUMN_CLASS_NAME),
                                          null, null, null, null, null)

        while (cursorObject.moveToNext())
        {
            val objectID = cursorObject.getLong(0)
            val className = cursorObject.getString(1)
            outputStream.writeLong(objectID)
            outputStream.writeString(className)

            val cursorField = database.query(TABLE_FIELDS,
                                             arrayOf(COLUMN_NAME, COLUMN_TYPE, COLUMN_VALUE_INTEGER,
                                                     COLUMN_VALUE_NUMBER, COLUMN_VALUE_TEXT),
                                             "$COLUMN_OBJECT_ID=?",
                                             arrayOf(objectID.toString()),
                                             null, null, null)

            while (cursorField.moveToNext())
            {
                outputStream.writeString(cursorField.getString(0))
                outputStream.writeString(cursorField.getString(1))
                outputStream.writeLong(cursorField.getLong(2))
                outputStream.writeDouble(cursorField.getDouble(3))
                outputStream.writeString(cursorField.getString(4))
            }

            cursorField.close()
            outputStream.writeString("")
        }

        cursorObject.close()

        for (k in 0 until 512)
        {
            outputStream.writeLong(NO_DATABASE_ID)
        }

        outputStream.flush()
        outputStream.close()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun dump()
    {
        this.databaseFuture.and { database ->
            println("=== DUMP $TABLE_OBJECTS ===")
            var cursor =
                database.query(TABLE_OBJECTS, arrayOf(COLUMN_ID, COLUMN_CLASS_NAME), null, null,
                               null, null, null)

            while (cursor.moveToNext())
            {
                print(cursor.getLong(0))
                print(" | ")
                println(cursor.getString(1))
            }

            cursor.close()
            println()
            println("=== DUMP $TABLE_FIELDS ===")
            cursor = database.query(TABLE_FIELDS, arrayOf(COLUMN_NAME,
                                                          COLUMN_TYPE,
                                                          COLUMN_VALUE_INTEGER,
                                                          COLUMN_VALUE_NUMBER,
                                                          COLUMN_VALUE_TEXT,
                                                          COLUMN_OBJECT_ID),
                                    null, null, null, null, null)

            while (cursor.moveToNext())
            {
                print(cursor.getString(0))
                print(" | ")
                print(cursor.getString(1))
                print(" | ")
                print(cursor.getLong(2))
                print(" | ")
                print(cursor.getDouble(3))
                print(" | ")
                print(cursor.getString(4))
                print(" | ")
                println(cursor.getLong(5))
            }

            cursor.close()
        }
    }
}
