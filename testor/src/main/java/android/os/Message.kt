/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package android.os

class Message
{
    companion object
    {
        fun obtain(): Message = Message()

        fun obtain(message: Message): Message
        {
            val messageCopy = Message()
            messageCopy.what = message.what
            messageCopy.arg1 = message.arg1
            messageCopy.arg2 = message.arg2
            messageCopy.obj = message.obj
            messageCopy.target = message.target
            messageCopy.callback = message.callback
            return messageCopy
        }

        fun obtain(handler: Handler?): Message
        {
            val message = Message()
            message.target = handler
            return message
        }

        fun obtain(handler: Handler?, callback: Runnable?): Message
        {
            val message = Message()
            message.target = handler
            message.callback = callback
            return message
        }

        fun obtain(handler: Handler?, what: Int): Message
        {
            val message = Message()
            message.target = handler
            message.what = what
            return message
        }

        fun obtain(handler: Handler?, what: Int, obj: Any?): Message
        {
            val message = Message()
            message.target = handler
            message.what = what
            message.obj = obj
            return message
        }

        fun obtain(handler: Handler?, what: Int, arg1: Int, arg2: Int): Message
        {
            val message = Message()
            message.target = handler
            message.what = what
            message.arg1 = arg1
            message.arg2 = arg2
            return message
        }

        fun obtain(handler: Handler?, what: Int, arg1: Int, arg2: Int, obj: Any?): Message
        {
            val message = Message()
            message.target = handler
            message.what = what
            message.arg1 = arg1
            message.arg2 = arg2
            message.obj = obj
            return message
        }
    }

    var what: Int = 0
    var arg1: Int = 0
    var arg2: Int = 0
    var obj: Any? = null
    var target: Handler? = null
    var callback: Runnable? = null

    override fun equals(other: Any?): Boolean
    {
        if (this === other)
        {
            return true
        }

        if (null == other || other !is Message)
        {
            return false
        }

        return this.what == other.what && this.arg1 == other.arg1 && this.arg2 == other.arg2 && this.obj == other.obj && this.callback == other.callback && this.target == other.target
    }
}