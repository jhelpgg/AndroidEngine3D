/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.tasks.chain

import fr.jhelp.tasks.launch
import fr.jhelp.tasks.promise.FutureResult
import fr.jhelp.tasks.promise.Promise

/**
 * Value emitter.
 *
 * An emitter provides a flow of elements. They may produce elements an infinite number of times
 */
abstract class Emitter<T : Any>
{
    /**
     * Provide the net element
     *
     * @return Next element or `null` if there no more elements
     */
    abstract fun next(): T?

    /**
     * Emmit, in independent environment, values to a task chain
     *
     * @return Future to be able react when emit is finished (No more elements to emit).
     * Or to cancel the emit (Useful on infinite emitter)
     */
    fun <R : Any> emit(taskChain: TaskChain<T, R>): FutureResult<Unit>
    {
        val promise = Promise<Unit>()
        val future = launch {
            var value = this.next()

            while (value != null && !promise.canceled)
            {
                taskChain.emit(value).waitComplete()
                value = this.next()
            }

            promise.result(Unit)
        }

        promise.register { reason -> future.cancel(reason) }
        return promise.future
    }
}