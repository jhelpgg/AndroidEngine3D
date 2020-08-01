/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package android.util

class Log
{
    companion object
    {
        @JvmStatic
        var tag = ""
            private set

        @JvmStatic
        var message = ""
            private set

        @JvmStatic
        var throwable: Throwable? = null
            private set

        @JvmStatic
        fun d(tag: String, message: String): Int =
            Log.d(tag, message, null)

        @JvmStatic
        fun d(tag: String, message: String, throwable: Throwable?): Int
        {
            this.tag = tag
            this.message = message
            this.throwable = throwable
            return 0
        }

        @JvmStatic
        fun e(tag: String, message: String): Int =
            Log.e(tag, message, null)

        @JvmStatic
        fun e(tag: String, message: String, throwable: Throwable?): Int
        {
            this.tag = tag
            this.message = message
            this.throwable = throwable
            return 0
        }
    }
}