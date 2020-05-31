/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.tasks

/**
 * Type of thread.
 *
 * Task are executed in specific thread type
 */
sealed class ThreadType
{
    abstract operator fun <R> invoke(task: () -> R)

    abstract operator fun <P, R> invoke(parameter: P, task: (P) -> R)

    abstract operator fun <P1, P2, R> invoke(parameter1: P1, parameter2: P2, task: (P1, P2) -> R)

    abstract operator fun <P1, P2, P3, R> invoke(parameter1: P1, parameter2: P2, parameter3: P3,
                                                 task: (P1, P2, P3) -> R)
}

/**
 * Tasks are played in background. Use this type for tasks not related to network, main thread or IO
 */
object IndependentThread : ThreadType()
{
    override operator fun <R> invoke(task: () -> R)
    {
        parallel(task)
    }

    override operator fun <P, R> invoke(parameter: P, task: (P) -> R)
    {
        parallel(parameter, task)
    }

    override operator fun <P1, P2, R> invoke(parameter1: P1, parameter2: P2, task: (P1, P2) -> R)
    {
        parallel(parameter1, parameter2, task)
    }

    override operator fun <P1, P2, P3, R> invoke(parameter1: P1, parameter2: P2, parameter3: P3,
                                                 task: (P1, P2, P3) -> R)
    {
        parallel(parameter1, parameter2, parameter3, task)
    }
}

/**
 * Play tasks in Main/:UI thread.
 *
 * It is reserved only for operations to do in Main/UI thread like change a text in TextView, hide/show a view, dialog, ...
 *
 * Don't do other operation in this thread
 */
object MainThread : ThreadType()
{
    override operator fun <R> invoke(task: () -> R)
    {
        parallelUI(task)
    }

    override operator fun <P, R> invoke(parameter: P, task: (P) -> R)
    {
        parallelUI(parameter, task)
    }

    override operator fun <P1, P2, R> invoke(parameter1: P1, parameter2: P2, task: (P1, P2) -> R)
    {
        parallelUI(parameter1, parameter2, task)
    }

    override operator fun <P1, P2, P3, R> invoke(parameter1: P1, parameter2: P2, parameter3: P3,
                                                 task: (P1, P2, P3) -> R)
    {
        parallelUI(parameter1, parameter2, parameter3, task)
    }
}

/**
 * Dedicated to al I/:O operations
 */
object IOThread : ThreadType()
{
    override operator fun <R> invoke(task: () -> R)
    {
        parallelIO(task)
    }

    override fun <P, R> invoke(parameter: P, task: (P) -> R)
    {
        parallelIO(parameter, task)
    }

    override operator fun <P1, P2, R> invoke(parameter1: P1, parameter2: P2, task: (P1, P2) -> R)
    {
        parallelIO(parameter1, parameter2, task)
    }

    override operator fun <P1, P2, P3, R> invoke(parameter1: P1, parameter2: P2, parameter3: P3,
                                                 task: (P1, P2, P3) -> R)
    {
        parallelIO(parameter1, parameter2, parameter3, task)
    }
}

/**
 * Do action when Internet connection available.
 *
 * Action will be queued and played when Internet is available. So its recommended for server requests
 *
 * Need [fr.jhelp.tasks.network.NetworkStatusManager] initialized for work
 */
object NetworkThread : ThreadType()
{
    override operator fun <R> invoke(task: () -> R)
    {
        parallelNetwork(task)
    }

    override operator fun <P, R> invoke(parameter: P, task: (P) -> R)
    {
        parallelNetwork(parameter, task)
    }

    override operator fun <P1, P2, R> invoke(parameter1: P1, parameter2: P2, task: (P1, P2) -> R)
    {
        parallelNetwork(parameter1, parameter2, task)
    }

    override operator fun <P1, P2, P3, R> invoke(parameter1: P1, parameter2: P2, parameter3: P3,
                                                 task: (P1, P2, P3) -> R)
    {
        parallelNetwork(parameter1, parameter2, parameter3, task)
    }
}
