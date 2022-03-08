/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.tasks

import java.util.concurrent.Semaphore

class Mutex
{
    private val mutex = Semaphore(1, true)

    operator fun <R : Any> invoke(task: () -> R): R
    {
        var result: R? = null
        var error: Throwable? = null
        this.mutex.acquire()

        try
        {
            result = task()
        }
        catch (throwable: Throwable)
        {
            error = throwable
        }
        finally
        {
            this.mutex.release()
        }

        if (error != null)
        {
            throw error
        }

        return result!!
    }
}
