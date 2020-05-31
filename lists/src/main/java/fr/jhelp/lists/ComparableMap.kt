/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.lists

/**
 * Optimized map for [Comparable] keys
 */
class ComparableMap<K : Comparable<K>, V>() :
    Iterable<Pair<K, V>>
{
    private val map = sortedArray<MapElement<K, V>>(true)

    val size get() = this.map.size

    val empty get() = this.map.empty

    val notEmpty get() = this.map.notEmpty

    /**
     * Defines/modify a key/value association
     */
    fun put(key: K, value: V)
    {
        val mapElement = MapElement(key, value)
        val index = this.map.indexOf(mapElement)

        if (index >= 0)
        {
            this.map[index].value = value
            return
        }

        this.map.add(mapElement)
    }

    /**
     * Defines/modify a key/value association
     */
    operator fun set(key: K, value: V) =
        this.put(key, value)

    /**
     * Obtain value at given key, or `null` if key not exists
     */
    operator fun get(key: K): V?
    {
        val mapElement = MapElement<K, V>(key, null)
        val index = this.map.indexOf(mapElement)

        if (index >= 0)
        {
            return this.map[index].value
        }

        return null
    }

    /**
     * Obtain value at given key, or use given creator to create value , associate it to the key and return the value
     */
    fun getOrPut(key: K, creator: () -> V): V
    {
        var value = this[key]

        if (value != null)
        {
            return value
        }

        value = creator()
        this[key] = value
        return value
    }

    /**
     * Remove a key
     *
     * @return Deleted value, or `null`if no association to the key
     */
    fun remove(key: K): V?
    {
        val mapElement = MapElement<K, V>(key, null)
        val index = this.map.indexOf(mapElement)

        if (index >= 0)
        {
            val value = this.map[index].value
            this.map.removeAt(index)
            return value
        }

        return null
    }

    /**
     * Indicates is a key is defined
     */
    operator fun contains(key: K) =
        this.map.contains(MapElement<K, V>(key, null))

    /**
     * Iterator on key/value
     */
    override fun iterator(): Iterator<Pair<K, V>> =
        this.map.iterator().transform { mapElement -> Pair(mapElement.key, mapElement.value!!) }

    /**
     * Iterable on keys
     */
    fun keys(): Iterable<K> =
        this.map.transform { mapElement -> mapElement.key }

    /**
     * Iterable on values
     */
    fun values(): Iterable<V> =
        this.map.transform { mapElement -> mapElement.value!! }

    /**
     * Clear the map
     */
    fun clear()
    {
        this.map.clear()
    }

    /**
     * String representation
     */
    override fun toString(): String =
        this.map.toString()
}