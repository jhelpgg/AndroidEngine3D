/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.tasks.strand

import fr.jhelp.tasks.IOThread
import fr.jhelp.tasks.IndependentThread
import fr.jhelp.tasks.MainThread
import fr.jhelp.tasks.NetworkThread
import fr.jhelp.tasks.ThreadType
import fr.jhelp.tasks.launch
import fr.jhelp.tasks.launchIO
import fr.jhelp.tasks.launchNetwork
import fr.jhelp.tasks.launchUI
import fr.jhelp.tasks.promise.Promise
import fr.jhelp.tasks.taskQueue.TaskQueue
import fr.jhelp.utilities.isVoid
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

internal class StrandHandler<I>(private val instance: I,
                                private val threadType: ThreadType) :
    InvocationHandler
{
    private val taskQueue = TaskQueue({ this::task }, this::parameterProvider)

    private fun task(strandHandlerElement: StrandHandlerElement)
    {
        val action =
            {
                try
                {
                    strandHandlerElement.promise.result(this.callMethod(strandHandlerElement.method,
                                                                        strandHandlerElement.args))
                }
                catch (exception: Exception)
                {
                    strandHandlerElement.promise.error(exception)
                }
            }

        when (this.threadType)
        {
            IndependentThread -> launch(action)
            IOThread          -> launchIO(action)
            MainThread        -> launchUI(action)
            NetworkThread     -> launchNetwork(action)
        }.waitComplete()
    }

    private fun parameterProvider(strandHandlerElement: StrandHandlerElement) =
        strandHandlerElement

    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any
    {
        synchronized(this.taskQueue)
        {
            val promise = Promise<Any>()
            this.taskQueue.enqueue(StrandHandlerElement(method!!, args, promise))
            return promise.future()
        }
    }

    private fun callMethod(method: Method, args: Array<out Any>?): Any
    {
        val result = if (args == null)
        {
            method.invoke(this.instance)
        }
        else
        {
            method.invoke(this.instance, *args)
        }

        return if (method.returnType.isVoid) Unit
        else result
    }
}

@Suppress("UNCHECKED_CAST")
internal fun <I> strandCaller(interfaceType: Class<I>, instance: I, threadType: ThreadType): I
{
    if (!interfaceType.isInterface)
    {
        throw IllegalArgumentException("interfaceType must represents an interface")
    }

    return Proxy.newProxyInstance(interfaceType.classLoader, arrayOf(interfaceType),
                                  StrandHandler(instance, threadType)) as I
}