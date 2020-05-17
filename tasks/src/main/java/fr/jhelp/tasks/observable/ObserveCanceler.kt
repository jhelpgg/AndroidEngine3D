package fr.jhelp.tasks.observable

import fr.jhelp.tasks.Cancelable

internal class ObserveCanceler<V : Any>(private val observable: Observable<V>,
                                        private val id: Int) : Cancelable
{
    override fun cancel()
    {
        this.observable.stopObserve(this.id)
    }
}