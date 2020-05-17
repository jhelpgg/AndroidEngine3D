package fr.jhelp.lists

/**
 * Iterator that transform elements exposed to outside
 * @param iterator Iterable reference
 * @param action Action to apply for transform elements
 */
class TransformedIterator<P, R>(private val iterator: Iterator<P>, private val action: (P) -> R) :
    Iterator<R>
{
    override fun hasNext() = this.iterator.hasNext()

    override fun next() = this.action(this.iterator.next())
}