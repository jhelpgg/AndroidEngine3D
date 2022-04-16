/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package android.os

open class Handler constructor(private var looper: Looper, private var callback: Callback?,
                               private var async: Boolean)
{
    companion object
    {
        private val mainHandler = Handler(Looper.getMainLooper())

        @JvmStatic
        fun createAsync(looper: Looper): Handler = Handler(looper, true)

        @JvmStatic
        fun createAsync(looper: Looper, callback: Callback): Handler =
            Handler(looper, callback, true)

        @JvmStatic
        fun getMain(): Handler = Handler.mainHandler

        @JvmStatic
        fun mainIfNull(handler: Handler?): Handler = handler ?: Handler.mainHandler

        private fun getPostMessage(r: Runnable): Message
        {
            val m = Message.obtain()
            m.callback = r
            return m
        }

        private fun getPostMessage(r: Runnable, token: Any?): Message
        {
            val m = Message.obtain()
            m.obj = token
            m.callback = r
            return m
        }

    }

    interface Callback
    {
        fun handleMessage(message: Message): Boolean
    }

    constructor(looper: Looper, async: Boolean) : this(looper, null, async)

    constructor(looper: Looper) : this(looper, null, false)

    constructor(callback: Callback?, async: Boolean) : this(Looper.myLooper(), callback, async)

    constructor(callback: Callback?) : this(Looper.myLooper(), callback, false)

    constructor(async: Boolean) : this(Looper.myLooper(), null, async)

    constructor() : this(Looper.myLooper(), null, false)

    constructor(looper: Looper, callback: Callback?) : this(looper, callback, false)

    open fun handleMessage(message: Message)
    {
    }

    open fun dispatchMessage(message: Message)
    {
        when
        {
            message.callback != null -> message.callback?.run()
            this.callback != null    -> this.callback?.handleMessage(message)
            else                     -> this.handleMessage(message)
        }
    }

    fun obtainMessage(): Message = Message.obtain(this)
    fun obtainMessage(what: Int): Message = Message.obtain(this, what)
    fun obtainMessage(what: Int, obj: Any?): Message = Message.obtain(this, what, obj)
    fun obtainMessage(what: Int, arg1: Int, arg2: Int): Message =
        Message.obtain(this, what, arg1, arg2)

    fun obtainMessage(what: Int, arg1: Int, arg2: Int, obj: Any?): Message =
        Message.obtain(this, what, arg1, arg2, obj)

    fun post(r: Runnable): Boolean =
        this.sendMessageDelayed(Handler.getPostMessage(r), 0)

    fun postAtTime(r: Runnable, uptimeMillis: Long): Boolean =
        sendMessageAtTime(getPostMessage(r), uptimeMillis)

    fun postAtTime(
        r: Runnable, token: Any?, uptimeMillis: Long): Boolean =
        sendMessageAtTime(getPostMessage(r, token), uptimeMillis)

    fun postDelayed(r: Runnable, delayMillis: Long): Boolean =
        sendMessageDelayed(getPostMessage(r), delayMillis)

    fun postDelayed(r: Runnable, what: Int, delayMillis: Long): Boolean
    {
        val message = getPostMessage(r)
        message.what = what
        return sendMessageDelayed(message, delayMillis)
    }

    fun postDelayed(
        r: Runnable, token: Any?, delayMillis: Long): Boolean
    {
        return sendMessageDelayed(getPostMessage(r, token), delayMillis)
    }

    fun postAtFrontOfQueue(r: Runnable): Boolean
    {
        return sendMessageAtFrontOfQueue(getPostMessage(r))
    }

    fun sendMessage(msg: Message): Boolean
    {
        return sendMessageDelayed(msg, 0)
    }

    fun sendEmptyMessage(what: Int): Boolean
    {
        return sendEmptyMessageDelayed(what, 0)
    }

    fun sendEmptyMessageDelayed(what: Int, delayMillis: Long): Boolean
    {
        val msg = Message.obtain()
        msg.what = what
        return sendMessageDelayed(msg, delayMillis)
    }

    fun sendEmptyMessageAtTime(what: Int, uptimeMillis: Long): Boolean
    {
        val msg = Message.obtain()
        msg.what = what
        return sendMessageAtTime(msg, uptimeMillis)
    }

    fun sendMessageDelayed(message: Message, delayMillis: Long): Boolean
    {
        this.looper.delay(delayMillis, this, message)
        return true
    }

    open fun sendMessageAtTime(message: Message, uptimeMillis: Long): Boolean
    {
        this.looper.atTime(uptimeMillis, this, message)
        return true
    }

    fun sendMessageAtFrontOfQueue(message: Message): Boolean
    {
        this.looper.highPriority(this, message)
        return true
    }

    fun executeOrSendMessage(msg: Message): Boolean
    {
        if (this.looper == Looper.myLooper())
        {
            dispatchMessage(msg)
            return true
        }

        return sendMessage(msg)
    }

    fun removeMessages(what: Int)
    {
        this.looper.removeMessages(this, what)
    }

    fun removeMessages(what: Int, obj: Any?)
    {
        if (obj == null)
        {
            this.looper.removeMessages(this, what)
        }
        else
        {
            this.looper.removeMessages(this, what, obj)
        }
    }

    fun removeEqualMessages(what: Int, obj: Any?)
    {
        if (obj == null)
        {
            this.looper.removeMessages(this, what)
        }
        else
        {
            this.looper.removeMessages(this, what, obj)
        }
    }

    fun removeCallbacksAndMessages(token: Any?)
    {
        if (token == null)
        {
            this.looper.removeAllCallbacks(this)
        }
        else
        {
            this.looper.removeCallbacks(this, token)
        }
    }


    fun removeCallbacksAndEqualMessages(token: Any?)
    {
        if (token == null)
        {
            this.looper.removeAllCallbacks(this)
        }
        else
        {
            this.looper.removeCallbacks(this, token)
        }
    }

    fun hasMessages(what: Int): Boolean = this.looper.hasMessages(this, what)

    fun hasMessagesOrCallbacks(): Boolean = this.looper.hasMessagesOrCallbacks(this)

    fun hasMessages(what: Int, obj: Any?): Boolean =
        if (obj == null)
        {
            this.looper.hasMessages(this, what)
        }
        else
        {
            this.looper.hasMessages(this, what, obj)
        }

    fun hasEqualMessages(what: Int, obj: Any?): Boolean =
        if (obj == null)
        {
            this.looper.hasMessages(this, what)
        }
        else
        {
            this.looper.hasMessages(this, what, obj)
        }

    fun hasCallbacks(r: Runnable): Boolean =
        this.looper.hasCallbacks(this, r)

    fun getLooper(): Looper = this.looper
}