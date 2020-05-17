package fr.jhelp.lists

/**
 * Iterable that filter elements exposed to outside
 * @param iterable Iterable reference
 * @param filter Filter applied on element to know if they are visible or not
 */
class FilteredIterable<T : Any>(private val iterable: Iterable<T>,
                                private val filter: (T) -> Boolean) : Iterable<T>
{
    override fun iterator(): Iterator<T> =
        FilteredIterator(this.iterable.iterator(), this.filter)
}