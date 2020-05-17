package fr.jhelp.lists

internal class QueueIterator<T>(private var current: QueueElement<T>?) : Iterator<T>
{
    override fun hasNext() = this.current != null

    @Throws(NoSuchElementException::class)
    override fun next(): T
    {
        val current = this.current
                      ?: throw NoSuchElementException("No more element to iterate")
        this.current = current.next
        return current.element
    }
}