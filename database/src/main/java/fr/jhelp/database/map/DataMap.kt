/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database.map

import androidx.annotation.Keep
import fr.jhelp.database.DataStorable

@Keep
class DataMap<DS : DataStorable> : DataStorable(), Iterable<Pair<String, DS>>
{
    val size get() = this.length

    val empty get() = this.length == 0

    val notEmpty get() = this.length != 0

    fun keys() = this.keys

    operator fun get(key: String) =
        this.getDataStorable<DS>(key)

    operator fun set(key: String, value: DS)
    {
        this.putDataStorable(key, value)
    }

    fun remove(key: String)
    {
        this.removeKey(key)
    }

    fun getOrPut(key: String, creator: () -> DS): DS
    {
        var value = this.getDataStorable<DS>(key)

        if (value == null)
        {
            value = creator()
            this.putDataStorable(key, value)
        }

        return value
    }

    override fun iterator(): Iterator<Pair<String, DS>> =
        DataMapIterator(this)
}