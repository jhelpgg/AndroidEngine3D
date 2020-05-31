# Promise and future result

When a task compute something in background,, it makes a promise do have a result in future.

Here it this way too see things that is implemented.

1. [Make a promise](#make-a-promise)
2. [Chain to future result](#chain-to-future-result)
3. [Cancellation management](#cancellation-management)
   1. [`canceled` example](#canceled-example)
   2. [`register` example](#register-example)

### Make a promise

Imagine a process that compute something :
* Fibonacci number
* Request to a server and wait the answer
* ...

This process , make the promise to obtain a result.
To do this, it creates privately a [Promise](../main/java/fr/jhelp/tasks/promise/Promise.kt)
and share the associates [FutureResult](../main/java/fr/jhelp/tasks/promise/FutureResult.kt)
to any process interested by the result.

When the result succeed, the process call the [Promise.result](../main/java/fr/jhelp/tasks/promise/Promise.kt#L49)
method. If fail it call the [Promise.error](../main/java/fr/jhelp/tasks/promise/Promise.kt#L58)
method.

### Chain to future result

A process interested by the result, received a [FutureResult](../main/java/fr/jhelp/tasks/promise/FutureResult.kt)

it can chain one or more task, after result complete.

If have a process that compute a number, the Fibonacci by example.
The process share a `FutureResult`. When result is computed, we want print it on a `TextView`

````kotlin
import fr.jhelp.tasks.parallel
import fr.jhelp.tasks.promise.FutureResult
import fr.jhelp.tasks.promise.Promise

fun computeNumber(seed: Int): FutureResult<Int>
{
    val promise = Promise<Int>()

    parallel(seed, promise)
    { start, promiseForAnswer ->
        var value = start
        // Compute the value
        // ...
        // 
        promiseForAnswer.result(value)
    }

    return promise.future
}
````

And

````kotlin
import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import fr.jhelp.tasks.MainThread

class MyActivity : Activity()
{
    private lateinit var textView : TextView
    
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main)
        this.textView = this.findViewById(R.id.textView)
        
        //
        
        computeNumber(128).and(MainThread) {value -> this.textView.text = "Value=$value"}
    }
}
````

The chaining is made by the `and` method. Like in the example,
we can specify the `ThreadType` where the task will be played.
If not specified, `IndependentThread` will be used. See [Thread type and tasks](ThreadTypeAndTasks.md)

Since the method returns a `FutureResult` its possible to chain to do something else after and so on.

Chaining methods are :

| Method name | Description |
|:---:|-----|
| and | The task is executed if result succeed |
| andIf | The task is executed if result succeed and match given condition |
| then | <p>The task is executed on completion, that is to say : succeed, failed or canceled.<br/> The FutureResult is on parameter to be able do things depends on completion status</p> |

Its possible to react on error with `onError` or on cancel with `onCancel`

The method `cancel` tries to cancel the task.
It will work well if cancellation is done properly

### Cancellation management

If possible, its a good idea to be able cancel a process associated with a `Promise`.

`Promise` provides two ways to react at cancellation request:

| Way | Description |
|:---:|-----|
| `canceled` field | Indicates at any time if cancel is requested |
| `register` method | Register a listener to be alert when cancellation is requested |

##### `canceled` example

If computing base on a loop, add `canceled` test on each loop::

````kotlin
while(condition && !promise.canceled)
{
   // ...
}
````

##### `register` example

When result depends from external, use the action to cancel the request,
by example in client/server, close the connection.

````kotlin
promise.register {reason -> this.socket.close() }
````