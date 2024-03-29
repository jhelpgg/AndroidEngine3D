/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.tasks.chain

import fr.jhelp.tasks.ThreadType
import fr.jhelp.tasks.promise.FutureResult
import fr.jhelp.tasks.promise.join

/**
 * Chain of tasks.
 *
 * The chain can be simple as do this, then that, then ...
 *
 * But it can be more complex. And chain part can be follow by several parts,
 * the result will be dispatch on all of them.
 *
 * Some can be execute only on particular condition.
 *
 * > /!\ WARNING /!\
 *
 * > Developers are free to do loop, it is there responsibility to assure that loops are not infinite
 *
 * @param threadType Environment where execute the action. If not set, independent environment will be used
 * @param action Action to play by the task
 */
class TaskChain<P : Any, R : Any>(private val threadType: ThreadType = ThreadType.SHORT,
                                  private val action: (P) -> R)
{
    private val continuations = ArrayList<TaskChain<R, *>>()

    /**
     * Emit value on the chain starting on this point
     *
     * @return Future to able to react when emit is finished and get this action result
     */
    fun emit(parameter: P): FutureResult<R>
    {
        val future = threadType.parallel(parameter, this.action)
        val list = ArrayList<FutureResult<*>>()
        list.add(future)

        synchronized(this.continuations)
        {
            for (continuation in this.continuations)
            {
                list.add(future.and { result -> continuation.emit(result)() })
            }
        }

        return list.join().then { future() }
    }

    /**
     * Chain a task chain to call after action execution when emit
     *
     * @return Chained task
     */
    fun <R1 : Any> and(continuation: TaskChain<R, R1>): TaskChain<R, R1>
    {
        synchronized(this.continuations)
        {
            this.continuations.add(continuation)
        }

        return continuation
    }


    /**
     * Chain a task chain to call after action execution when emit
     *
     * @param threadType Environment where play the action.
     * @param action Action to do on  chained task chain
     * The same environment as this one is used if not specified
     * @return Chained task
     */
    fun <R1 : Any> and(threadType: ThreadType = this.threadType, action: (R) -> R1) =
        this.and(TaskChain(threadType, action))

    /**
     * Chain an task chain to call after action execution when emit if the given condition match
     *
     * @return Chained task
     */
    fun <R1 : Any> andIf(condition: (R) -> Boolean, taskChain: TaskChain<R, R1>) =
        this.and { value ->
            if (condition(value))
            {
                taskChain.emit(value)()
            }
            else
            {
                throw IllegalArgumentException("Not accepted value : $value")
            }
        }

    /**
     * Chain an task chain to call after action execution when emit if the given condition match
     *
     * The action will used an [IndependentThread] thread type to execute
     *
     * @param condition Condition to execute the action
     * @param action Action to do on  chained task chain
     * The same environment as this one is used if not specified
     * @return Chained task
     */
    fun <R1 : Any> andIf(condition: (R) -> Boolean, action: (R) -> R1) =
        this.andIf(ThreadType.SHORT, condition, action)

    /**
     * Chain an task chain to call after action execution when emit if the given condition match
     *
     * @param threadType Environment where play the action.
     * @param condition Condition to execute the action
     * @param action Action to do on  chained task chain
     * The same environment as this one is used if not specified
     * @return Chained task
     */
    fun <R1 : Any> andIf(threadType: ThreadType, condition: (R) -> Boolean, action: (R) -> R1) =
        this.andIf(condition, TaskChain(threadType, action))

}