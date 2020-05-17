package fr.jhelp.lists

fun <C : Comparable<C>> sortedArray(unique: Boolean = false) =
    SortedArray<C>(NaturalComparator(), unique)

class SortedArray<T>(private val comparator: Comparator<T>, val unique: Boolean = false) :
    Iterable<T>
{
    private val array = ArrayList<T>()

    val size get() = this.array.size

    val empty get() = this.array.isEmpty()

    val notEmpty get() = this.array.isNotEmpty()

    fun add(element: T): Boolean
    {
        val index = this.indexInsert(element)

        return when
        {
            index >= this.array.size                                                ->
                this.array.add(element)
            this.unique && this.comparator.compare(this.array[index], element) == 0 ->
                false
            else                                                                    ->
            {
                this.array.add(index, element)
                true
            }
        }
    }

    fun addAll(iterable: Iterable<T>)
    {
        for (element in iterable)
        {
            this.add(element)
        }
    }

    operator fun get(index: Int) =
        this.array[index]

    operator fun contains(element: T) =
        this.indexOf(element) >= 0

    fun containsAll(iterable: Iterable<T>): Boolean
    {
        for (element in iterable)
        {
            if (element !in this)
            {
                return false
            }
        }

        return true
    }

    fun indexOf(element: T): Int
    {
        val index = this.indexInsert(element)

        if (index < this.size && this.comparator.compare(this.array[index], element) == 0)
        {
            return index
        }

        return -1
    }

    fun remove(element: T): Boolean
    {
        val index = this.indexInsert(element)

        if (index < this.size && this.comparator.compare(this.array[index], element) == 0)
        {
            this.array.removeAt(index)
            return true
        }

        return false
    }

    fun removeAt(index: Int) =
        this.array.removeAt(index)

    fun removeIf(condition: (T) -> Boolean)
    {
        val iterator = this.array.iterator()

        while (iterator.hasNext())
        {
            if (condition(iterator.next()))
            {
                iterator.remove()
            }
        }
    }

    fun clear()
    {
        this.array.clear()
    }

    override fun iterator(): Iterator<T> =
        this.array.iterator()

    private fun indexInsert(element: T): Int
    {
        if (this.array.isEmpty())
        {
            return 0
        }

        var max = this.array.size - 1
        var comparison = this.comparator.compare(element, this.array[max])

        if (comparison > 0)
        {
            return this.size
        }

        if (comparison == 0)
        {
            return max
        }

        comparison = this.comparator.compare(element, this.array[0])

        if (comparison <= 0)
        {
            return 0
        }

        var min = 0

        while (min + 1 < max)
        {
            val middle = (min + max) / 2
            comparison = this.comparator.compare(element, this.array[middle])

            when
            {
                comparison == 0 -> return middle
                comparison < 0  -> max = middle
                else            -> min = middle
            }
        }

        return max
    }

    override fun toString(): String =
        this.array.toString()
}