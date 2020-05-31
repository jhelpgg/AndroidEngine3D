# Async tools

Some extension are provides in [Async](../main/java/fr/jhelp/tasks/Async.kt)

1. [forEachAsync](#foreachasync)
2. [emit](#emit)

### forEachAsync

The extension `forEachAsync` on `Iterable` or `Array`, execute an action
on each element in given thread type.

### emit

The extension `emit` on `Iterator`, `Iterable`, `Enumeration` or `Array`,
emits all values in a given [TaskChain](../main/java/fr/jhelp/tasks/chain/TaskChain.kt).
See [Task chain](TaskChain.md)
