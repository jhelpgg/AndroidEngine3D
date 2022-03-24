/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.tasks.observable

import fr.jhelp.tasks.ThreadType

class Observer<V : Any> internal constructor(private val parent: ObservableValue<V>,
                                             private val threadType: ThreadType,
                                             private val task: (V) -> Unit)
{
    private val observeObservableValue = ObservableValue<Boolean>(true)
    val observeObservable: Observable<Boolean> = this.observeObservableValue.observable

    fun stopObserve()
    {
        this.observeObservableValue.value = false
        this.parent.stopObserve(this)
    }

    internal fun publish(value: V)
    {
        this.threadType.parallel(value, this.task)
    }
}
