package fr.jhelp.lists

class ComparableMap<K : Comparable<K>, V>() :
    Iterable<Pair<K, V>>
{
    private val map = sortedArray<MapElement<K, V>>(true)

    val size get() = this.map.size

    val empty get() = this.map.empty

    val notEmpty get() = this.map.notEmpty

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

    operator fun set(key: K, value: V) =
        this.put(key, value)

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

    operator fun contains(key: K) =
        this.map.contains(MapElement<K, V>(key, null))

    override fun iterator(): Iterator<Pair<K, V>> =
        this.map.iterator().transform { mapElement -> Pair(mapElement.key, mapElement.value!!) }

    fun keys(): Iterable<K> =
        this.map.transform { mapElement -> mapElement.key }

    fun values(): Iterable<V> =
        this.map.transform { mapElement -> mapElement.value!! }

    fun clear()
    {
        this.map.clear()
    }

    override fun toString(): String =
        this.map.toString()
}