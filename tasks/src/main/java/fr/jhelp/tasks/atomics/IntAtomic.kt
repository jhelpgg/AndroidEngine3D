package fr.jhelp.tasks.atomics

import java.util.concurrent.atomic.AtomicInteger
import kotlin.reflect.KProperty

class IntAtomic(initialValue : Int)
{
    private val atomicInteger = AtomicInteger(initialValue)

    operator fun getValue(thisRef : Any, property : KProperty<*>) : Int = this.atomicInteger.get()

    operator fun setValue(thisRef : Any, property : KProperty<*>, value : Int)
    {
        this.atomicInteger.set(value)
    }
}
