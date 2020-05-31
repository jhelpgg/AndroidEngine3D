# Task chain

A [TaskChain](../main/java/fr/jhelp/tasks/chain/TaskChain.kt) is link of chain
of tasks.

It contains the task to do and the `ThreadType`where play the task.

`TaskChain` can be combine in very complex way, loop is possible by example.

But remember to not do infinite, it must always exists a exit time.
A condition that no more task to do.

The main difference with `Promise and FutureResult` (See [Promise and future result](PromiseAndFutureResult.md))
is the chain can be executed several time and if tasks are side effect free, in "same time".

To start the chaining, call `emit` method. Thi method returns a `FutureResult`.
The `FutureResult` is complete when no more task to do, and will contains the result of the `TaskChain`,
not th final result.

To chain use the method `and` to indicates an action to do when the associated action
finished.

The method `andIf` does the same, except the action is executed only if given condition on result meet.
