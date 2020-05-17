package fr.jhelp.tasks.chain

/**
 * [Emitter] based on array.
 *
 * It will emit all array elements once
 */
class EmitterArray<T : Any>(private val array: Array<T>) : Emitter<T>()
{
    private var index = 0;

    override fun next(): T?
    {
        if (this.index >= this.array.size)
        {
            return null
        }

        return this.array[this.index++]
    }
}