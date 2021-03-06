/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.lists

/**
 * Iterator that filter elements exposed to outside
 * @param iterator Iterable reference
 * @param filter Filter applied on element to know if they are visible or not
 */
class FilteredIterator<T : Any>(private val iterator: Iterator<T>,
                                private val filter: (T) -> Boolean) : Iterator<T>
{
    private var hasNext = true
    private var searchNext = true
    private lateinit var current: T

    private fun foundNext()
    {
        if (this.hasNext && this.searchNext)
        {
            this.searchNext = false
            var element: T

            while (this.iterator.hasNext())
            {
                element = this.iterator.next()

                if (this.filter(element))
                {
                    this.current = element
                    return
                }
            }

            this.hasNext = false
        }
    }

    override fun hasNext(): Boolean
    {
        this.foundNext()
        return this.hasNext
    }

    override fun next(): T
    {
        this.foundNext()

        if (!this.hasNext)
        {
            throw NoSuchElementException("No more element to iterate")
        }

        this.searchNext = true
        return this.current
    }
}