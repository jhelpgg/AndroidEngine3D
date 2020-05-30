/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database.map

import fr.jhelp.database.DataStorable

internal class DataMapIterator<DS : DataStorable>(private val map: DataMap<DS>) :
    Iterator<Pair<String, DS>>
{
    private val keys = this.map.keys().iterator()

    override fun hasNext() = this.keys.hasNext()

    override fun next(): Pair<String, DS>
    {
        val key = this.keys.next()
        return Pair<String, DS>(key, this.map[key]!!)
    }
}