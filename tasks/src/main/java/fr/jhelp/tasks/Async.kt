package fr.jhelp.tasks

import fr.jhelp.tasks.chain.EmitterArray
import fr.jhelp.tasks.chain.EmitterEnumeration
import fr.jhelp.tasks.chain.EmitterIterator
import fr.jhelp.tasks.chain.TaskChain
import fr.jhelp.tasks.promise.FutureResult
import fr.jhelp.tasks.promise.Promise
import java.util.Enumeration
import java.util.concurrent.atomic.AtomicInteger

/**
 * Iterate over all elements in separate thread.
 *
 * The given action is played on given execution environment for each element
 *
 * @param threadType : execution environment where the action is played.
 * If not specified, execute independent environment
 * @param action Action to play on each element
 * @return future to be able chain with something else when all action are done.
 * Or to cancel execution at any time
 */
fun <T> Iterable<T>.forEachAsync(threadType: ThreadType = IndependentThread,
                                 action: (T) -> Unit): FutureResult<Iterable<T>>
{
    val promise = Promise<Iterable<T>>()
    val counter = AtomicInteger(this.count())

    val future = launch(this, threadType, action) { iterable, thread, task ->
        for (element in iterable)
        {
            thread(iterable, element, task) { result, value, taskToDo ->
                try
                {
                    if (!promise.canceled)
                    {
                        taskToDo(value)
                    }
                }
                catch (exception: Exception)
                {
                    promise.error(exception)
                }

                if (counter.decrementAndGet() == 0)
                {
                    promise.result(result)
                }
            }
        }
    }

    promise.register { reason -> future.cancel(reason) }
    return promise.future
}

/**
 * Iterate over all elements in separate thread.
 *
 * The given action is played on given execution environment for each element
 *
 * @param threadType : execution environment where the action is played.
 * If not specified, execute independent environment
 * @param action Action to play on each element
 * @return future to be able chain with something else when all action are done.
 * Or to cancel execution at any time
 */
fun <T> Array<T>.forEachAsync(threadType: ThreadType = IndependentThread,
                              action: (T) -> Unit): FutureResult<Array<T>>
{
    val promise = Promise<Array<T>>()
    val counter = AtomicInteger(this.size)

    val future = launch(this, threadType, action) { array, thread, task ->
        for (element in array)
        {
            thread(array, element, task) { result, value, taskToDo ->
                try
                {
                    if (!promise.canceled)
                    {
                        taskToDo(value)
                    }
                }
                catch (exception: Exception)
                {
                    promise.error(exception)
                }

                if (counter.decrementAndGet() == 0)
                {
                    promise.result(result)
                }
            }
        }
    }

    promise.register { reason -> future.cancel(reason) }
    return promise.future
}

/**
 * Emit each element to on task chain.
 *
 * The method returns immediately due emit is do in independent environment
 * @param taskChain Task chain where emit elements
 * @return Future to be able react when emit finished or to cancel it
 */
fun <P : Any, R : Any> Iterator<P>.emit(taskChain: TaskChain<P, R>) =
    EmitterIterator(this).emit(taskChain)

/**
 * Emit each element to on task chain.
 *
 * The method returns immediately due emit is do in independent environment
 * @param taskChain Task chain where emit elements
 * @return Future to be able react when emit finished or to cancel it
 */
fun <P : Any, R : Any> Iterable<P>.emit(taskChain: TaskChain<P, R>) =
    EmitterIterator(this.iterator()).emit(taskChain)

/**
 * Emit each element to on task chain.
 *
 * The method returns immediately due emit is do in independent environment
 * @param taskChain Task chain where emit elements
 * @return Future to be able react when emit finished or to cancel it
 */
fun <P : Any, R : Any> Enumeration<P>.emit(taskChain: TaskChain<P, R>) =
    EmitterEnumeration(this).emit(taskChain)

/**
 * Emit each element to on task chain.
 *
 * The method returns immediately due emit is do in independent environment
 * @param taskChain Task chain where emit elements
 * @return Future to be able react when emit finished or to cancel it
 */
fun <P : Any, R : Any> Array<P>.emit(taskChain: TaskChain<P, R>) =
    EmitterArray(this).emit(taskChain)

fun (() -> Unit).cancellable() =
    object : Cancelable
    {
        override fun cancel()
        {
            this@cancellable()
        }
    }