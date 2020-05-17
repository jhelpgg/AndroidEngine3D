package fr.jhelp.tasks

import fr.jhelp.tasks.delay.Delay
import fr.jhelp.tasks.network.NetworkStatusQueue
import fr.jhelp.tasks.promise.FutureResult
import fr.jhelp.tasks.promise.Promise
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun <R> parallel(task: () -> R)
{
    otherScope.launch {
        withContext(Dispatchers.Default)
        {
            task.suspended()()
        }
    }
}

fun <P, R> parallel(parameter: P, task: (P) -> R)
{
    otherScope.launch {
        withContext(Dispatchers.Default)
        {
            task.suspended()(parameter)
        }
    }
}

fun <P1, P2, R> parallel(parameter1: P1, parameter2: P2, task: (P1, P2) -> R)
{
    otherScope.launch {
        withContext(Dispatchers.Default)
        {
            task.suspended()(parameter1, parameter2)
        }
    }
}

fun <P1, P2, P3, R> parallel(parameter1: P1, parameter2: P2, parameter3: P3,
                             task: (P1, P2, P3) -> R)
{
    otherScope.launch {
        withContext(Dispatchers.Default)
        {
            task.suspended()(parameter1, parameter2, parameter3)
        }
    }
}

fun <R> parallelUI(task: () -> R)
{
    mainScope.launch {
        withContext(Dispatchers.Main)
        {
            task.suspended()()
        }
    }
}

fun <P, R> parallelUI(parameter: P, task: (P) -> R)
{
    mainScope.launch {
        withContext(Dispatchers.Main)
        {
            task.suspended()(parameter)
        }
    }
}

fun <P1, P2, R> parallelUI(parameter1: P1, parameter2: P2, task: (P1, P2) -> R)
{
    mainScope.launch {
        withContext(Dispatchers.Main)
        {
            task.suspended()(parameter1, parameter2)
        }
    }
}

fun <P1, P2, P3, R> parallelUI(parameter1: P1, parameter2: P2, parameter3: P3,
                               task: (P1, P2, P3) -> R)
{
    mainScope.launch {
        withContext(Dispatchers.Main)
        {
            task.suspended()(parameter1, parameter2, parameter3)
        }
    }
}

fun <R> parallelIO(task: () -> R)
{
    ioScope.launch {
        withContext(Dispatchers.IO)
        {
            task.suspended()()
        }
    }
}

fun <P, R> parallelIO(parameter: P, task: (P) -> R)
{
    ioScope.launch {
        withContext(Dispatchers.IO)
        {
            task.suspended()(parameter)
        }
    }
}

fun <P1, P2, R> parallelIO(parameter1: P1, parameter2: P2, task: (P1, P2) -> R)
{
    ioScope.launch {
        withContext(Dispatchers.IO)
        {
            task.suspended()(parameter1, parameter2)
        }
    }
}

fun <P1, P2, P3, R> parallelIO(parameter1: P1, parameter2: P2, parameter3: P3,
                               task: (P1, P2, P3) -> R)
{
    ioScope.launch {
        withContext(Dispatchers.IO)
        {
            task.suspended()(parameter1, parameter2, parameter3)
        }
    }
}

/**
 * Do action when Internet connection available.
 *
 * Need [fr.jhelp.tasks.network.NetworkStatusManager] initialized for work
 */
fun <R> parallelNetwork(task: () -> R)
{
    NetworkStatusQueue.add { task() }
}

/**
 * Do action when Internet connection available.
 *
 * Need [fr.jhelp.tasks.network.NetworkStatusManager] initialized for work
 */
fun <P, R> parallelNetwork(parameter: P, task: (P) -> R)
{
    NetworkStatusQueue.add { task(parameter) }
}

/**
 * Do action when Internet connection available.
 *
 * Need [fr.jhelp.tasks.network.NetworkStatusManager] initialized for work
 */
fun <P1, P2, R> parallelNetwork(parameter1: P1, parameter2: P2, task: (P1, P2) -> R)
{
    NetworkStatusQueue.add { task(parameter1, parameter2) }
}

/**
 * Do action when Internet connection available.
 *
 * Need [fr.jhelp.tasks.network.NetworkStatusManager] initialized for work
 */
fun <P1, P2, P3, R> parallelNetwork(parameter1: P1, parameter2: P2, parameter3: P3,
                                    task: (P1, P2, P3) -> R)
{
    NetworkStatusQueue.add { task(parameter1, parameter2, parameter3) }
}

fun <R : Any> launch(task: () -> R): FutureResult<R>
{
    val promise = Promise<R>()
    val taskToPlay =
        {
            try
            {
                promise.result(task())
            }
            catch (exception: Exception)
            {
                promise.error(exception)
            }
        }

    val job = otherScope.launch {
        withContext(Dispatchers.Default)
        {
            taskToPlay.suspended()()
        }
    }

    promise.register { reason -> job.cancel(CancellationException(reason)) }
    return promise.future
}

fun <P, R : Any> launch(parameter: P, task: (P) -> R): FutureResult<R>
{
    val promise = Promise<R>()
    val taskToPlay =
        { param: P ->
            try
            {
                promise.result(task(param))
            }
            catch (exception: Exception)
            {
                promise.error(exception)
            }
        }

    val job = otherScope.launch {
        withContext(Dispatchers.Default)
        {
            taskToPlay.suspended()(parameter)
        }
    }

    promise.register { reason -> job.cancel(CancellationException(reason)) }
    return promise.future
}

fun <P1, P2, R : Any> launch(parameter1: P1, parameter2: P2, task: (P1, P2) -> R): FutureResult<R>
{
    val promise = Promise<R>()
    val taskToPlay =
        { param1: P1, param2: P2 ->
            try
            {
                promise.result(task(param1, param2))
            }
            catch (exception: Exception)
            {
                promise.error(exception)
            }
        }

    val job = otherScope.launch {
        withContext(Dispatchers.Default)
        {
            taskToPlay.suspended()(parameter1, parameter2)
        }
    }

    promise.register { reason -> job.cancel(CancellationException(reason)) }
    return promise.future
}

fun <P1, P2, P3, R : Any> launch(parameter1: P1, parameter2: P2, parameter3: P3,
                                 task: (P1, P2, P3) -> R): FutureResult<R>
{
    val promise = Promise<R>()
    val taskToPlay =
        { param1: P1, param2: P2, param3: P3 ->
            try
            {
                promise.result(task(param1, param2, param3))
            }
            catch (exception: Exception)
            {
                promise.error(exception)
            }
        }

    val job = otherScope.launch {
        withContext(Dispatchers.Default)
        {
            taskToPlay.suspended()(parameter1, parameter2, parameter3)
        }
    }

    promise.register { reason -> job.cancel(CancellationException(reason)) }
    return promise.future
}

fun <R : Any> launchUI(task: () -> R): FutureResult<R>
{
    val promise = Promise<R>()
    val taskToPlay =
        {
            try
            {
                promise.result(task())
            }
            catch (exception: Exception)
            {
                promise.error(exception)
            }
        }

    val job = mainScope.launch {
        withContext(Dispatchers.Main)
        {
            taskToPlay.suspended()()
        }
    }

    promise.register { reason -> job.cancel(CancellationException(reason)) }
    return promise.future
}

fun <P, R : Any> launchUI(parameter: P, task: (P) -> R): FutureResult<R>
{
    val promise = Promise<R>()
    val taskToPlay =
        { param: P ->
            try
            {
                promise.result(task(param))
            }
            catch (exception: Exception)
            {
                promise.error(exception)
            }
        }

    val job = mainScope.launch {
        withContext(Dispatchers.Main)
        {
            taskToPlay.suspended()(parameter)
        }
    }

    promise.register { reason -> job.cancel(CancellationException(reason)) }
    return promise.future
}

fun <P1, P2, R : Any> launchUI(parameter1: P1, parameter2: P2, task: (P1, P2) -> R): FutureResult<R>
{
    val promise = Promise<R>()
    val taskToPlay =
        { param1: P1, param2: P2 ->
            try
            {
                promise.result(task(param1, param2))
            }
            catch (exception: Exception)
            {
                promise.error(exception)
            }
        }

    val job = mainScope.launch {
        withContext(Dispatchers.Main)
        {
            taskToPlay.suspended()(parameter1, parameter2)
        }
    }

    promise.register { reason -> job.cancel(CancellationException(reason)) }
    return promise.future
}

fun <P1, P2, P3, R : Any> launchUI(parameter1: P1, parameter2: P2, parameter3: P3,
                                   task: (P1, P2, P3) -> R): FutureResult<R>
{
    val promise = Promise<R>()
    val taskToPlay =
        { param1: P1, param2: P2, param3: P3 ->
            try
            {
                promise.result(task(param1, param2, param3))
            }
            catch (exception: Exception)
            {
                promise.error(exception)
            }
        }

    val job = mainScope.launch {
        withContext(Dispatchers.Main)
        {
            taskToPlay.suspended()(parameter1, parameter2, parameter3)
        }
    }

    promise.register { reason -> job.cancel(CancellationException(reason)) }
    return promise.future
}

fun <R : Any> launchIO(task: () -> R): FutureResult<R>
{
    val promise = Promise<R>()
    val taskToPlay =
        {
            try
            {
                promise.result(task())
            }
            catch (exception: Exception)
            {
                promise.error(exception)
            }
        }

    val job = ioScope.launch {
        withContext(Dispatchers.IO)
        {
            taskToPlay.suspended()()
        }
    }

    promise.register { reason -> job.cancel(CancellationException(reason)) }
    return promise.future
}

fun <P, R : Any> launchIO(parameter: P, task: (P) -> R): FutureResult<R>
{
    val promise = Promise<R>()
    val taskToPlay =
        { param: P ->
            try
            {
                promise.result(task(param))
            }
            catch (exception: Exception)
            {
                promise.error(exception)
            }
        }

    val job = ioScope.launch {
        withContext(Dispatchers.IO)
        {
            taskToPlay.suspended()(parameter)
        }
    }

    promise.register { reason -> job.cancel(CancellationException(reason)) }
    return promise.future
}

fun <P1, P2, R : Any> launchIO(parameter1: P1, parameter2: P2, task: (P1, P2) -> R): FutureResult<R>
{
    val promise = Promise<R>()
    val taskToPlay =
        { param1: P1, param2: P2 ->
            try
            {
                promise.result(task(param1, param2))
            }
            catch (exception: Exception)
            {
                promise.error(exception)
            }
        }

    val job = ioScope.launch {
        withContext(Dispatchers.IO)
        {
            taskToPlay.suspended()(parameter1, parameter2)
        }
    }

    promise.register { reason -> job.cancel(CancellationException(reason)) }
    return promise.future
}

fun <P1, P2, P3, R : Any> launchIO(parameter1: P1, parameter2: P2, parameter3: P3,
                                   task: (P1, P2, P3) -> R): FutureResult<R>
{
    val promise = Promise<R>()
    val taskToPlay =
        { param1: P1, param2: P2, param3: P3 ->
            try
            {
                promise.result(task(param1, param2, param3))
            }
            catch (exception: Exception)
            {
                promise.error(exception)
            }
        }

    val job = ioScope.launch {
        withContext(Dispatchers.IO)
        {
            taskToPlay.suspended()(parameter1, parameter2, parameter3)
        }
    }

    promise.register { reason -> job.cancel(CancellationException(reason)) }
    return promise.future
}

/**
 * Do action when Internet connection available.
 *
 * Need [fr.jhelp.tasks.network.NetworkStatusManager] initialized for work
 */
fun <R : Any> launchNetwork(task: () -> R): FutureResult<R>
{
    val promise = Promise<R>()
    val cancelable =
        NetworkStatusQueue.add {
            try
            {
                promise.result(task())
            }
            catch (exception: Exception)
            {
                promise.error(exception)
            }
        }

    promise.register { cancelable.cancel() }
    return promise.future
}

/**
 * Do action when Internet connection available.
 *
 * Need [fr.jhelp.tasks.network.NetworkStatusManager] initialized for work
 */
fun <P, R : Any> launchNetwork(parameter: P, task: (P) -> R): FutureResult<R>
{
    val promise = Promise<R>()
    val cancelable =
        NetworkStatusQueue.add {
            try
            {
                promise.result(task(parameter))
            }
            catch (exception: Exception)
            {
                promise.error(exception)
            }
        }

    promise.register { cancelable.cancel() }
    return promise.future
}

/**
 * Do action when Internet connection available.
 *
 * Need [fr.jhelp.tasks.network.NetworkStatusManager] initialized for work
 */
fun <P1, P2, R : Any> launchNetwork(parameter1: P1, parameter2: P2,
                                    task: (P1, P2) -> R): FutureResult<R>
{
    val promise = Promise<R>()
    val cancelable =
        NetworkStatusQueue.add {
            try
            {
                promise.result(task(parameter1, parameter2))
            }
            catch (exception: Exception)
            {
                promise.error(exception)
            }
        }

    promise.register { cancelable.cancel() }
    return promise.future
}

/**
 * Do action when Internet connection available.
 *
 * Need [fr.jhelp.tasks.network.NetworkStatusManager] initialized for work
 */
fun <P1, P2, P3, R : Any> launchNetwork(parameter1: P1, parameter2: P2, parameter3: P3,
                                        task: (P1, P2, P3) -> R): FutureResult<R>
{
    val promise = Promise<R>()
    val cancelable =
        NetworkStatusQueue.add {
            try
            {
                promise.result(task(parameter1, parameter2, parameter3))
            }
            catch (exception: Exception)
            {
                promise.error(exception)
            }
        }

    promise.register { cancelable.cancel() }
    return promise.future
}

fun <R : Any> delay(milliseconds: Long, action: () -> R): FutureResult<R> =
    Delay.delay(IndependentThread, milliseconds, action)

fun <P, R : Any> delay(milliseconds: Long, parameter: P, action: (P) -> R): FutureResult<R> =
    Delay.delay(IndependentThread, milliseconds, parameter, action)

fun <P1, P2, R : Any> delay(milliseconds: Long, parameter1: P1, parameter2: P2,
                            action: (P1, P2) -> R): FutureResult<R> =
    Delay.delay(IndependentThread, milliseconds, parameter1, parameter2, action)

fun <P1, P2, P3, R : Any> delay(milliseconds: Long, parameter1: P1, parameter2: P2, parameter3: P3,
                                action: (P1, P2, P3) -> R): FutureResult<R> =
    Delay.delay(IndependentThread, milliseconds, parameter1, parameter2, parameter3, action)

fun <R : Any> delayUI(milliseconds: Long, action: () -> R): FutureResult<R> =
    Delay.delay(MainThread, milliseconds, action)

fun <P, R : Any> delayUI(milliseconds: Long, parameter: P, action: (P) -> R): FutureResult<R> =
    Delay.delay(MainThread, milliseconds, parameter, action)

fun <P1, P2, R : Any> delayUI(milliseconds: Long, parameter1: P1, parameter2: P2,
                              action: (P1, P2) -> R): FutureResult<R> =
    Delay.delay(MainThread, milliseconds, parameter1, parameter2, action)

fun <P1, P2, P3, R : Any> delayUI(milliseconds: Long, parameter1: P1, parameter2: P2,
                                  parameter3: P3,
                                  action: (P1, P2, P3) -> R): FutureResult<R> =
    Delay.delay(MainThread, milliseconds, parameter1, parameter2, parameter3, action)

fun <R : Any> delayIO(milliseconds: Long, action: () -> R): FutureResult<R> =
    Delay.delay(IOThread, milliseconds, action)

fun <P, R : Any> delayIO(milliseconds: Long, parameter: P, action: (P) -> R): FutureResult<R> =
    Delay.delay(IOThread, milliseconds, parameter, action)

fun <P1, P2, R : Any> delayIO(milliseconds: Long, parameter1: P1, parameter2: P2,
                              action: (P1, P2) -> R): FutureResult<R> =
    Delay.delay(IOThread, milliseconds, parameter1, parameter2, action)

fun <P1, P2, P3, R : Any> delayIO(milliseconds: Long, parameter1: P1, parameter2: P2,
                                  parameter3: P3,
                                  action: (P1, P2, P3) -> R): FutureResult<R> =
    Delay.delay(IOThread, milliseconds, parameter1, parameter2, parameter3, action)

/**
 * Do action when Internet connection available.
 *
 * Need [fr.jhelp.tasks.network.NetworkStatusManager] initialized for work
 */
fun <R : Any> delayNetwork(milliseconds: Long, action: () -> R): FutureResult<R> =
    Delay.delay(NetworkThread, milliseconds, action)

/**
 * Do action when Internet connection available.
 *
 * Need [fr.jhelp.tasks.network.NetworkStatusManager] initialized for work
 */
fun <P, R : Any> delayNetwork(milliseconds: Long, parameter: P, action: (P) -> R): FutureResult<R> =
    Delay.delay(NetworkThread, milliseconds, parameter, action)

/**
 * Do action when Internet connection available.
 *
 * Need [fr.jhelp.tasks.network.NetworkStatusManager] initialized for work
 */
fun <P1, P2, R : Any> delayNetwork(milliseconds: Long, parameter1: P1, parameter2: P2,
                            action: (P1, P2) -> R): FutureResult<R> =
    Delay.delay(NetworkThread, milliseconds, parameter1, parameter2, action)

/**
 * Do action when Internet connection available.
 *
 * Need [fr.jhelp.tasks.network.NetworkStatusManager] initialized for work
 */
fun <P1, P2, P3, R : Any> delayNetwork(milliseconds: Long, parameter1: P1, parameter2: P2, parameter3: P3,
                                action: (P1, P2, P3) -> R): FutureResult<R> =
    Delay.delay(NetworkThread, milliseconds, parameter1, parameter2, parameter3, action)

// Coroutines management

private val mainScope = MainScope()
private val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
private val otherScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

private fun <R> (() -> R).suspended(): suspend () -> R =
    { this() }

private fun <P, R> ((P) -> R).suspended(): suspend (P) -> R =
    { p -> this(p) }

private fun <P1, P2, R> ((P1, P2) -> R).suspended(): suspend (P1, P2) -> R =
    { p1, p2 -> this(p1, p2) }

private fun <P1, P2, P3, R> ((P1, P2, P3) -> R).suspended(): suspend (P1, P2, P3) -> R =
    { p1, p2, p3 -> this(p1, p2, p3) }

