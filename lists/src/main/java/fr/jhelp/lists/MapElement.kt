/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.lists

internal class MapElement<K : Comparable<K>, V>(val key: K, var value: V?) :
    Comparable<MapElement<K, V>>
{
    override operator fun compareTo(other: MapElement<K, V>) =
        this.key.compareTo(other.key)

    override fun toString() =
        "${this.key}=${this.value}"
}
