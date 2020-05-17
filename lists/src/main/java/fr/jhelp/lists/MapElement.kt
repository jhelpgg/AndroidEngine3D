package fr.jhelp.lists

internal class MapElement<K : Comparable<K>, V>(val key: K, var value: V?) :
    Comparable<MapElement<K, V>>
{
    override operator fun compareTo(other: MapElement<K, V>) =
        this.key.compareTo(other.key)

    override fun toString() =
        "${this.key}=${this.value}"
}
