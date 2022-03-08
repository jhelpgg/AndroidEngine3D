package fr.jhelp.tasks.atomics

inline fun <reified T:Any> atomic(initialValue : T) = ReferenceAtomic<T>(initialValue)

fun atomic(initialValue : Boolean) = BooleanAtomic(initialValue)

fun atomic(initialValue : Int) = IntAtomic(initialValue)
