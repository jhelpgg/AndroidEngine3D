/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database.list

import androidx.annotation.Keep
import fr.jhelp.database.DataStorable

private const val SIZE = "size"

@Keep
class DataList<DS : DataStorable> : DataStorable(), Iterable<DS>
{
    init
    {
        this.putInt(SIZE, 0)
    }

    val size get() = this.getInt(SIZE)

    val empty get() = this.size == 0

    val notEmpty get() = this.size != 0

    operator fun get(index: Int): DS
    {
        this.checkIndex(index)
        return this.getDataStorable<DS>(index.toString())!!
    }

    operator fun set(index: Int, element: DS)
    {
        this.checkIndex(index)
        this.putDataStorable(index.toString(), element)
    }

    fun add(element: DS)
    {
        val size = this.size
        this.putDataStorable(size.toString(), element)
        this.putInt(SIZE, size + 1)
    }

    fun remove(index: Int)
    {
        this.checkIndex(index)
        val size = this.size

        for (i in index until size - 1)
        {
            this.putDataStorable<DS>(i.toString(), this.getDataStorable<DS>((i + 1).toString()))
        }

        this.removeKey((size - 1).toString())
        this.putInt(SIZE, size - 1)
    }

    fun insert(index: Int, element: DS)
    {
        this.checkIndex(index)
        val size = this.size

        for (i in size downTo index + 1)
        {
            this.putDataStorable<DS>(i.toString(), this.getDataStorable<DS>((i - 1).toString()))
        }

        this.putDataStorable(index.toString(), element)
        this.putInt(SIZE, size + 1)
    }

    override fun iterator(): Iterator<DS> =
        DataListIterator(this)

    private fun checkIndex(index: Int)
    {
        if (index < 0 || index >= this.size)
        {
            throw IndexOutOfBoundsException("$index not inside [0, ${this.size}[")
        }
    }
}