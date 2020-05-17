package fr.jhelp.tasks.observable

import android.util.SparseArray
import androidx.core.util.valueIterator
import fr.jhelp.tasks.Cancelable
import fr.jhelp.tasks.IndependentThread
import fr.jhelp.tasks.ThreadType
import fr.jhelp.tasks.chain.TaskChain
import fr.jhelp.tasks.promise.FutureResult
import fr.jhelp.tasks.promise.Promise
import fr.jhelp.utilities.ALWAYS_TRUE
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

private val NEXT_ID = AtomicInteger(0)

/**
 * Observable contains a value to observe its change.
 *
 * Can register observers alerted each time value change and meet some condition.
 *
 * It is recommended to use an immutable value, to avoid someone change the value without alert the observable
 */
class Observable<V : Any>(value: V)
{
    private val value = AtomicReference(value)
    private val observers = SparseArray<ObservableElement<V>>()

    /**
     * Current value
     */
    fun value() = this.value.get()

    /**
     * Change the embed value and alert observers
     */
    fun changeValue(newValue: V)
    {
        this.value.set(newValue)

        synchronized(this.observers)
        {
            val iterator = this.observers.valueIterator()
            while (iterator.hasNext())
            {
                val observer = iterator.next()

                if (observer.matcher(newValue))
                {
                    observer.threadType(newValue, observer.observer)
                }
            }
        }
    }

    /**
     * Register an observer
     */
    private fun observe(id: Int, threadType: ThreadType, matcher: (V) -> Boolean,
                        observer: (V) -> Unit)
    {
        synchronized(this.observers)
        {
            this.observers.put(id, ObservableElement(threadType, matcher, observer))
        }

        val value = this.value.get()

        if (matcher(value))
        {
            threadType(value, observer)
        }
    }

    fun observe(observer: (V) -> Unit) =
        this.observe(IndependentThread, ALWAYS_TRUE, observer)

    fun observe(matcher: (V) -> Boolean, observer: (V) -> Unit) =
        this.observe(IndependentThread, matcher, observer)

    fun observe(threadType: ThreadType, observer: (V) -> Unit) =
        this.observe(threadType, ALWAYS_TRUE, observer)

    /**
     * Register an observer too be alert if change match some condition
     *
     * If the current value match the condition, the observer alert immediately for the first time.
     *
     * @param threadType Environment where play the action.
     * @param matcher Condition that value must match to alert the observer.
     * @param observer Observer to register
     * @return Cancelable to be able stop the observer to observe changes
     */
    fun observe(threadType: ThreadType, matcher: (V) -> Boolean, observer: (V) -> Unit): Cancelable
    {
        val id = NEXT_ID.getAndIncrement()
        this.observe(id, threadType, matcher, observer)
        return ObserveCanceler(this, id)
    }

    /**
     * Unregister an observer
     */
    internal fun stopObserve(id: Int)
    {
        synchronized(this.observers)
        {
            this.observers.remove(id)
        }
    }

    /**
     * Alert next time, even now, value matcher given condition
     *
     * @param matcher Condition to trigger the alert
     * @return Future on the value at the moment. To react when the moment happen or unregister the alert before it happen
     */
    fun nextTime(matcher: (V) -> Boolean): FutureResult<V>
    {
        val promise = Promise<V>()
        val id = NEXT_ID.getAndIncrement()
        val observer =
            { value: V ->
                this.stopObserve(id)
                promise.result(value)
            }

        this.observe(id, IndependentThread, matcher, observer)
        promise.register { this.stopObserve(id) }
        return promise.future
    }

    /**
     * Emit the value on given task chain each time given condition meet
     *
     * @param matcher condition to trigger the emit
     * @param taskChain Task chain to start on given condition
     * @return Cancelable to unregister the emit
     */
    fun <R : Any> eachTime(matcher: (V) -> Boolean, taskChain: TaskChain<V, R>) =
        this.observe(matcher, { value -> taskChain.emit(value) })

    /**
     * Emit each change oon given task chain
     *
     * @param taskChain Task chain to start on each change
     * @return Cancelable to unregister the emit
     */
    fun <R : Any> onEachChange(taskChain: TaskChain<V, R>) =
        this.eachTime(ALWAYS_TRUE, taskChain)
}