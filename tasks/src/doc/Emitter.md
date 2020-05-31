# Emitter

An [Emitter](../main/java/fr/jhelp/tasks/chain/Emitter.kt) is an object
that can emits value one after other.

Just implements the method `next` to provide the next element.
The emitter can be "infinite" by never returns `null`

It can be emits on a [TaskChain](../main/java/fr/jhelp/tasks/chain/TaskChain.kt)
whit the method `emit`.

This method returns a [FutureResult](../main/java/fr/jhelp/tasks/promise/FutureResult.kt)
to be able cancel emitting (Useful for "infinite" emitter), or know when emitting is finished.  
See [Task chain](TaskChain.md) and [Promise and future result](PromiseAndFutureResult.md)

To emits all object in `Array`, `Iterator` or `Enumeration`, can use the implementation:
[EmitterArray](../main/java/fr/jhelp/tasks/chain/EmitterArray.kt),
[EmitterIterator](../main/java/fr/jhelp/tasks/chain/EmitterIterator.kt),
[EmitterEnumeration](../main/java/fr/jhelp/tasks/chain/EmitterEnumeration.kt).

Or one of extension defines in [Async](../main/java/fr/jhelp/tasks/Async.kt),
see [Async tools](Async.md)
