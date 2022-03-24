/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.tasks.observable

import fr.jhelp.tasks.ThreadType

class Observable<V : Any> internal constructor(private val parent: ObservableValue<V>)
{
    val value: V get() = this.parent.value

    fun observedBy(task: (V) -> Unit): Observer<V> =
        this.parent.observedBy(ThreadType.SHORT, task)

    fun observedBy(threadType: ThreadType, task: (V) -> Unit): Observer<V> =
        this.parent.observedBy(threadType, task)
}
