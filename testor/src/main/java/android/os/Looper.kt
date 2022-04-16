/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package android.os

import java.util.concurrent.PriorityBlockingQueue
import kotlin.math.max

class Looper private constructor(private val thread: Thread)
{
    companion object
    {
        private val loopers = HashMap<Long, Looper>()

        private val theMainLooper: Looper = Looper.myLooper()

        @JvmStatic
        fun prepare() = Unit

        @JvmStatic
        fun prepareMainLooper() = Unit

        @JvmStatic
        fun getMainLooper(): Looper = Looper.theMainLooper

        @JvmStatic
        fun loop()
        {
            Looper.myLooper().doLoop(false)
        }

        fun loopAllNow()
        {
            for (looper in Looper.loopers.values)
            {
                looper.doLoop(true)
            }
        }

        @JvmStatic
        fun myLooper(): Looper
        {
            val thread = Thread.currentThread()
            return Looper.loopers.getOrPut(thread.id) { Looper(thread) }
        }
    }

    private val tasksQueue = PriorityBlockingQueue<HandlerMessage>()

    internal fun atTime(timeInMillisecondsFromEpoch: Long, handler: Handler, message: Message)
    {
        this.tasksQueue.offer(HandlerMessage(timeInMillisecondsFromEpoch, handler, message))
    }

    internal fun delay(delayMilliseconds: Long, handler: Handler, message: Message)
    {
        this.atTime(System.currentTimeMillis() + max(0L, delayMilliseconds), handler, message)
    }

    internal fun now(handler: Handler, message: Message)
    {
        this.atTime(System.currentTimeMillis(), handler, message)
    }

    internal fun highPriority(handler: Handler, message: Message)
    {
        this.tasksQueue.offer(HandlerMessage(0L, handler, message))
    }

    internal fun removeMessages(handler: Handler, what: Int)
    {
        this.tasksQueue.removeIf { handlerMessage -> handlerMessage.handler == handler && handlerMessage.message.what == what }
    }

    internal fun removeMessages(handler: Handler, what: Int, obj: Any)
    {
        this.tasksQueue.removeIf { handlerMessage -> handlerMessage.handler == handler && handlerMessage.message.what == what && handlerMessage.message.obj == obj }
    }

    internal fun removeAllCallbacks(handler: Handler)
    {
        this.tasksQueue.removeIf { handlerMessage -> handlerMessage.handler == handler && handlerMessage.message.callback != null }
    }

    internal fun removeCallbacks(handler: Handler, token: Any)
    {
        this.tasksQueue.removeIf { handlerMessage -> handlerMessage.handler == handler && handlerMessage.message.obj == token && handlerMessage.message.callback != null }
    }

    internal fun hasMessages(handler: Handler, what: Int): Boolean =
        this.tasksQueue.any { handlerMessage -> handlerMessage.handler == handler && handlerMessage.message.what == what }

    internal fun hasMessagesOrCallbacks(handler: Handler): Boolean =
        this.tasksQueue.any { handlerMessage -> handlerMessage.handler == handler }

    internal fun hasMessages(handler: Handler, what: Int, obj: Any): Boolean =
        this.tasksQueue.any { handlerMessage -> handlerMessage.handler == handler && handlerMessage.message.what == what && handlerMessage.message.obj == obj }

    internal fun hasCallbacks(handler: Handler, callback: Runnable): Boolean =
        this.tasksQueue.any { handlerMessage -> handlerMessage.handler == handler && handlerMessage.message.callback == callback }

    private fun doLoop(forced: Boolean)
    {
        if (this.tasksQueue.isEmpty())
        {
            return
        }

        val handlerMessage = this.tasksQueue.peek()

        if (handlerMessage.time <= System.currentTimeMillis() || forced)
        {
            this.tasksQueue.remove(handlerMessage)
            handlerMessage.handler.dispatchMessage(handlerMessage.message)
        }
    }
}
