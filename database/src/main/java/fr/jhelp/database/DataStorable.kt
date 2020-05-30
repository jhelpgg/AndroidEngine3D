/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database

import androidx.annotation.Keep
import fr.jhelp.lists.Queue
import fr.jhelp.utilities.extensions.double
import fr.jhelp.utilities.extensions.float
import fr.jhelp.utilities.extensions.int
import fr.jhelp.utilities.extensions.long

/**
 * Object can be store in [Database].
 *
 * Extension must have the empty public constructor (Used by the system for create objects)
 *
 * The class must be preserve from Proguard renaming, use the annotation [Keep] is the simplest way to do it
 */
@Keep
abstract class DataStorable
{
    internal var databaseID = ID_NEW_DATA
    private val data = HashMap<String, Data>()
    private val toRemove = Queue<String>()

    protected fun getInt(key: String, defaultValue: Int = 0): Int
    {
        val data = this.data[key] ?: return defaultValue

        return when (data)
        {
            is DataInt    -> data.value
            is DataLong   -> data.value.toInt()
            is DataFloat  -> data.value.toInt()
            is DataDouble -> data.value.toInt()
            is DataString -> data.value.int(defaultValue)
            is DataObject -> defaultValue
        }
    }

    protected fun getLong(key: String, defaultValue: Long = 0L): Long
    {
        val data = this.data[key] ?: return defaultValue

        return when (data)
        {
            is DataInt    -> data.value.toLong()
            is DataLong   -> data.value
            is DataFloat  -> data.value.toLong()
            is DataDouble -> data.value.toLong()
            is DataString -> data.value.long(defaultValue)
            is DataObject -> defaultValue
        }
    }

    protected fun getFloat(key: String, defaultValue: Float = 0f): Float
    {
        val data = this.data[key] ?: return defaultValue

        return when (data)
        {
            is DataInt    -> data.value.toFloat()
            is DataLong   -> data.value.toFloat()
            is DataFloat  -> data.value
            is DataDouble -> data.value.toFloat()
            is DataString -> data.value.float(defaultValue)
            is DataObject -> defaultValue
        }
    }

    protected fun getDouble(key: String, defaultValue: Double = 0.0): Double
    {
        val data = this.data[key] ?: return defaultValue

        return when (data)
        {
            is DataInt    -> data.value.toDouble()
            is DataLong   -> data.value.toDouble()
            is DataFloat  -> data.value.toDouble()
            is DataDouble -> data.value
            is DataString -> data.value.double(defaultValue)
            is DataObject -> defaultValue
        }
    }

    protected fun getString(key: String, defaultValue: String? = null): String?
    {
        val data = this.data[key] ?: return defaultValue

        return when (data)
        {
            is DataInt    -> data.value.toString()
            is DataLong   -> data.value.toString()
            is DataFloat  -> data.value.toString()
            is DataDouble -> data.value.toString()
            is DataString -> data.value
            is DataObject -> defaultValue
        }
    }

    protected fun <DS : DataStorable> getDataStorable(key: String): DS?
    {
        val data = this.data[key] ?: return null

        return if (data is DataObject)
        {
            data.value as DS?
        }
        else
        {
            null
        }
    }

    protected fun putInt(key: String, value: Int)
    {
        val data = this.data.getOrPut(key) { DataInt(value) }

        when (data)
        {
            is DataInt    -> data.value = value
            is DataLong   -> data.value = value.toLong()
            is DataFloat  -> data.value = value.toFloat()
            is DataDouble -> data.value = value.toDouble()
            is DataString -> data.value = value.toString()
            is DataObject -> throw IllegalArgumentException("Can't store a Int in object")
        }
    }

    protected fun putLong(key: String, value: Long)
    {
        val data = this.data.getOrPut(key) { DataLong(value) }

        when (data)
        {
            is DataInt    -> data.value = value.toInt()
            is DataLong   -> data.value = value
            is DataFloat  -> data.value = value.toFloat()
            is DataDouble -> data.value = value.toDouble()
            is DataString -> data.value = value.toString()
            is DataObject -> throw IllegalArgumentException("Can't store a Long in object")
        }
    }

    protected fun putFloat(key: String, value: Float)
    {
        val data = this.data.getOrPut(key) { DataFloat(value) }

        when (data)
        {
            is DataInt    -> data.value = value.toInt()
            is DataLong   -> data.value = value.toLong()
            is DataFloat  -> data.value = value
            is DataDouble -> data.value = value.toDouble()
            is DataString -> data.value = value.toString()
            is DataObject -> throw IllegalArgumentException("Can't store a Float in object")
        }
    }

    protected fun putDouble(key: String, value: Double)
    {
        val data = this.data.getOrPut(key) { DataDouble(value) }

        when (data)
        {
            is DataInt    -> data.value = value.toInt()
            is DataLong   -> data.value = value.toLong()
            is DataFloat  -> data.value = value.toFloat()
            is DataDouble -> data.value = value
            is DataString -> data.value = value.toString()
            is DataObject -> throw IllegalArgumentException("Can't store a Double in object")
        }
    }

    protected fun putString(key: String, value: String?)
    {
        if (value == null)
        {
            this.toRemove.enqueue(key)
            this.data.remove(key)
            return
        }

        val data = this.data.getOrPut(key) { DataString(value) }

        when (data)
        {
            is DataInt    -> data.value = value.int(0)
            is DataLong   -> data.value = value.long(0L)
            is DataFloat  -> data.value = value.float(0f)
            is DataDouble -> data.value = value.double(0.0)
            is DataString -> data.value = value
            is DataObject -> throw IllegalArgumentException("Can't store a String in object")
        }
    }

    protected fun <DS : DataStorable> putDataStorable(key: String, value: DS?)
    {
        if (value == null)
        {
            this.toRemove.enqueue(key)
            this.data.remove(key)
            return
        }

        val data = this.data.getOrPut(key) { DataObject(value) }

        if (data is DataObject)
        {
            data.value = value
        }
        else
        {
            throw IllegalArgumentException("Can't store an Object in ${data.dataType}")
        }
    }

    protected fun removeKey(key: String)
    {
        this.toRemove.enqueue(key)
        this.data.remove(key)
    }

    protected fun keyDefined(key: String) = key in this.data

    protected val keys: Set<String> get() = this.data.keys

    protected val length get() = this.data.size

    internal fun clear()
    {
        this.data.clear()
    }

    internal fun put(key: String, integer: Long)
    {
        this.putLong(key, integer)
    }

    internal fun put(key: String, number: Double)
    {
        this.putDouble(key, number)
    }

    internal fun put(key: String, text: String)
    {
        this.putString(key, text)
    }

    internal fun <DS : DataStorable> put(key: String, dataStorable: DS)
    {
        this.putDataStorable(key, dataStorable)
    }

    internal fun writeData(database: Database)
    {
        while (this.toRemove.notEmpty)
        {
            database.removeField(this.databaseID, this.toRemove.dequeue())
        }

        for ((key, data) in this.data)
        {
            data.writeValue(database, this.databaseID, key)
        }
    }
}