package fr.jhelp.tasks.chain

/**
 * [Emitter] based on iterator.
 *
 * It will emit all iterator elements once
 */
class EmitterIterator<T : Any>(private val iterator: Iterator<T>) : Emitter<T>()
{
    override fun next(): T?
    {
        if (this.iterator.hasNext())
        {
            return this.iterator.next()
        }

        return null
    }
}