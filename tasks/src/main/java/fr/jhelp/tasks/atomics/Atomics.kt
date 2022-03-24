package fr.jhelp.tasks.atomics

fun <T : Any> atomic(initialValue: T) = ReferenceAtomic<T>(initialValue)

fun atomic(initialValue: Boolean) = BooleanAtomic(initialValue)

fun atomic(initialValue: Int) = IntAtomic(initialValue)
