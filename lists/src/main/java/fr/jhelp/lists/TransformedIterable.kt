package fr.jhelp.lists

/**
 * Iterable that transform elements exposed to outside
 * @param iterable Iterable reference
 * @param action Action to apply for transform elements
 */
class TransformedIterable<P, R>(private val iterable: Iterable<P>, private val action: (P) -> R) :
    Iterable<R>
{
    override fun iterator() =
        TransformedIterator(this.iterable.iterator(), this.action)
}