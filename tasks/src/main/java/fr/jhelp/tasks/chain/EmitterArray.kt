/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

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