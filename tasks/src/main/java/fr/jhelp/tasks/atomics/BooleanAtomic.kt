package fr.jhelp.tasks.atomics

import java.util.concurrent.atomic.AtomicBoolean
import kotlin.reflect.KProperty

class BooleanAtomic(initialValue : Boolean)
{
    private val atomicBoolean = AtomicBoolean(initialValue)

    operator fun getValue(thisRef : Any, property : KProperty<*>) : Boolean = this.atomicBoolean.get()

    operator fun setValue(thisRef : Any, property : KProperty<*>, value : Boolean)
    {
        this.atomicBoolean.set(value)
    }
}
