/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package android.content

import kotlin.math.max
import kotlin.math.min

class ContentValues(size: Int = 128)
{
    private val content = HashMap<String, Any>(max(16, min(4096, size)))
    internal val serialized = HashMap<String, String>()

    constructor(contentValues: ContentValues) : this(contentValues.size())
    {
        this.content.putAll(contentValues.content)
    }

    fun put(key: String, value: String)
    {
        this.content[key] = value
        this.serialized[key] = "'$value'"
    }

    fun putAll(other: ContentValues)
    {
        this.content.putAll(other.content)
        this.serialized.putAll(other.serialized)
    }

    fun put(key: String, value: Byte)
    {
        this.content[key] = value
        this.serialized[key] = value.toString()
    }

    fun put(key: String, value: java.lang.Byte)
    {
        this.content[key] = value
        this.serialized[key] = value.toString()
    }

    fun put(key: String, value: Short)
    {
        this.content[key] = value
        this.serialized[key] = value.toString()
    }

    fun put(key: String, value: java.lang.Short)
    {
        this.content[key] = value
        this.serialized[key] = value.toString()
    }

    fun put(key: String, value: Int)
    {
        this.content[key] = value
        this.serialized[key] = value.toString()
    }

    fun put(key: String, value: java.lang.Integer)
    {
        this.content[key] = value
        this.serialized[key] = value.toString()
    }

    fun put(key: String, value: Long)
    {
        this.content[key] = value
        this.serialized[key] = value.toString()
    }

    fun put(key: String, value: java.lang.Long)
    {
        this.content[key] = value
        this.serialized[key] = value.toString()
    }

    fun put(key: String, value: Float)
    {
        this.content[key] = value
        this.serialized[key] = value.toString()
    }

    fun put(key: String, value: java.lang.Float)
    {
        this.content[key] = value
        this.serialized[key] = value.toString()
    }

    fun put(key: String, value: Double)
    {
        this.content[key] = value
        this.serialized[key] = value.toString()
    }

    fun put(key: String, value: java.lang.Double)
    {
        this.content[key] = value
        this.serialized[key] = value.toString()
    }

    fun put(key: String, value: Boolean)
    {
        this.content[key] = value
        this.serialized[key] = if (value) "1" else "0"
    }

    fun put(key: String, value: java.lang.Boolean)
    {
        this.content[key] = value
        this.serialized[key] = if (value.booleanValue()) "1" else "0"
    }

    fun size(): Int = this.content.size

    fun isEmpty(): Boolean = this.content.isEmpty()

    fun remove(key: String)
    {
        this.content.remove(key)
        this.serialized.remove(key)
    }

    fun clear()
    {
        this.content.clear()
        this.serialized.clear()
    }

    fun containsKey(key: String): Boolean = this.content.containsKey(key)

    fun get(key: String): Any? =
        this.content[key]

    fun getAsString(key: String): String? =
        this.content[key]?.toString()

    fun <N : Number> getAsNumber(key: String,
                                 numberToN: Number.() -> N,
                                 stringToN: String.() -> N): N?
    {
        val value = this.content[key] ?: return null

        if (value is Number)
        {
            return value.numberToN()
        }

        return try
        {
            value.toString().stringToN()
        }
        catch (_: Exception)
        {
            null
        }
    }

    fun getAsLong(key: String): Long? =
        this.getAsNumber(key, Number::toLong, String::toLong)

    fun getAsInt(key: String): Int? =
        this.getAsNumber(key, Number::toInt, String::toInt)

    fun getAsShort(key: String): Short? =
        this.getAsNumber(key, Number::toShort, String::toShort)

    fun getAsByte(key: String): Byte? =
        this.getAsNumber(key, Number::toByte, String::toByte)

    fun getAsFloat(key: String): Float? =
        this.getAsNumber(key, Number::toFloat, String::toFloat)

    fun getAsDouble(key: String): Double? =
        this.getAsNumber(key, Number::toDouble, String::toDouble)

    fun getAsBoolean(key: String): Boolean?
    {
        val value = this.content[key] ?: return null

        return when (value)
        {
            is Boolean      -> value
            is CharSequence ->
            {
                val string = value.toString()
                string.equals("TRUE", true) || string == "1"
            }
            is Number       -> value.toInt() != 0
            else            -> null
        }
    }

    fun valueSet(): Set<Map.Entry<String, Any>> =
        this.content.entries

    fun keySet(): Set<String> =
        this.content.keys
}
