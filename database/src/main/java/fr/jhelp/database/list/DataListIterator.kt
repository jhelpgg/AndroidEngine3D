/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database.list

import fr.jhelp.database.DataStorable

internal class DataListIterator<DS:DataStorable>(private val list:DataList<DS>) : Iterator<DS>
{
    private var index = 0

    override fun hasNext() = this.index < this.list.size

    override fun next(): DS
    {
        if(this.index>=this.list.size)
        {
            throw NoSuchElementException("No more element to iterate")
        }

        return this.list[this.index]
    }
}