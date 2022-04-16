/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package android.util

import java.io.PrintStream
import java.util.Calendar
import java.util.GregorianCalendar

class Log private constructor()
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
        @JvmOverloads
        fun v(tag: String, message: String, throwable: Throwable? = null): Int =
            this.print(System.out, "VERBOSE", tag, message, throwable)

        @JvmStatic
        @JvmOverloads
        fun d(tag: String, message: String, throwable: Throwable? = null): Int =
            this.print(System.out, "DEBUG", tag, message, throwable)

        @JvmStatic
        @JvmOverloads
        fun i(tag: String, message: String, throwable: Throwable? = null): Int =
            this.print(System.out, "INFORMATION", tag, message, throwable)

        @JvmStatic
        @JvmOverloads
        fun w(tag: String, message: String, throwable: Throwable? = null): Int =
            this.print(System.out, "WARNING", tag, message, throwable)

        @JvmStatic
        @JvmOverloads
        fun e(tag: String, message: String, throwable: Throwable? = null): Int =
            this.print(System.err, "ERROR", tag, message, throwable)

        @JvmStatic
        @JvmOverloads
        fun wtf(tag: String, message: String, throwable: Throwable? = null): Int =
            this.print(System.err, "FAILURE", tag, message, throwable)

        private fun print(printStream: PrintStream, type: String, tag: String, message: String,
                          throwable: Throwable?): Int
        {
            Log.tag = tag
            Log.message = message
            Log.throwable = throwable

            val calendar = GregorianCalendar()
            printStream.print(calendar[Calendar.YEAR])
            printStream.print("/")
            var value = calendar[Calendar.MONTH] + 1

            if (value < 10)
            {
                printStream.print("0")
            }

            printStream.print(value)
            printStream.print("/")
            value = calendar[Calendar.DAY_OF_MONTH]

            if (value < 10)
            {
                printStream.print("0")
            }

            printStream.print(value)
            printStream.print(" at ")

            value = calendar[Calendar.HOUR_OF_DAY]

            if (value < 10)
            {
                printStream.print("0")
            }

            printStream.print(value)
            printStream.print("H ")

            value = calendar[Calendar.MINUTE]

            if (value < 10)
            {
                printStream.print("0")
            }

            printStream.print(value)
            printStream.print("M ")

            value = calendar[Calendar.SECOND]

            if (value < 10)
            {
                printStream.print("0")
            }

            printStream.print(value)
            printStream.print("S ")

            value = calendar[Calendar.MILLISECOND]

            if (value < 100)
            {
                printStream.print("0")
            }

            if (value < 10)
            {
                printStream.print("0")
            }

            printStream.print(value)

            printStream.print(":")
            printStream.print(type)
            printStream.print(":")
            printStream.print(tag)
            printStream.print(":")
            printStream.println(message)

            var cause = throwable

            while (cause != null)
            {
                printStream.println(cause)

                for (trace in cause.stackTrace)
                {
                    printStream.print("     ")
                    printStream.print(trace.className)
                    printStream.print(".")
                    printStream.print(trace.methodName)
                    printStream.print(" at ")
                    printStream.println(trace.lineNumber)
                }

                cause = cause.cause

                if (cause != null)
                {
                    printStream.println("Caused by : ")
                }
            }

            return 0
        }
    }
}