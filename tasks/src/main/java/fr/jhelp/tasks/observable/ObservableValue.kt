/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.tasks.observable

import fr.jhelp.tasks.Mutex
import fr.jhelp.tasks.ThreadType

class ObservableValue<V : Any>(startValue: V)
{
    private val observers = ArrayList<Observer<V>>()
    private val mutex = Mutex()
    private var valuePrivate: V = startValue
    var value: V
        get() = this.mutex { this.valuePrivate }
        set(value)
        {
            this.mutex {
                this.valuePrivate = value
                this.valueChanged(value)
            }
        }
    val observable: Observable<V> = Observable<V>(this)

    internal fun observedBy(threadType: ThreadType, task: (V) -> Unit): Observer<V>
    {
        val observer = Observer<V>(this, threadType, task)
        synchronized(this.observers) { this.observers.add(observer) }
        threadType.parallel(this.value, task)
        return observer
    }

    internal fun stopObserve(observer: Observer<V>)
    {
        synchronized(this.observers) { this.observers.remove(observer) }
    }

    private fun valueChanged(newValue: V)
    {
        synchronized(this.observers)
        {
            for (observer in this.observers)
            {
                observer.publish(newValue)
            }
        }
    }
}
