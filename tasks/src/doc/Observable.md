# Observable

An [Observable](../main/java/fr/jhelp/tasks/observable/Observable.kt)
is a container of a value to observe its change.

Each time the value change, call the `changeValue` method and all observers
will be called, if the new value meet the condition the registered for.

The method `observe`, register an observer, it's possible to choose the thread type
where the observer is executed. The condition given is a filter to alert the
observer only when value match the condition.

It returns a [Cancelable](../main/java/fr/jhelp/tasks/Cancelable.kt) to be able to stop to observe.

If the current value already match the condition, the observer will be alert "immediately".

Its possible to be alert only once if value match or when next time the value match
with `nextTime` method.

It returns a [FutureResult](../main/java/fr/jhelp/tasks/promise/FutureResult.kt)
to be able chain with the result or cancel the registration before condition match.
See [Promise and future result](PromiseAndFutureResult.md)

The methods `eachTime` and `onEachChange` will emit on a [TaskChain](../main/java/fr/jhelp/tasks/chain/TaskChain.kt)
when value meet condition or on each change. See [Task chain](TaskChain.md)
