/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.tasks

import fr.jhelp.utilities.bounds
import java.util.concurrent.Semaphore

class ReaderWriter(numberMaxReader: Int = 16)
{
    private val numberMaxReader = numberMaxReader.bounds(1, 64)
    private val semaphore = Semaphore(this.numberMaxReader, true)

    fun <R : Any> read(reader: () -> R): R
    {
        var result: R? = null
        var error: Throwable? = null
        this.semaphore.acquire()

        try
        {
            result = reader()
        }
        catch (throwable: Throwable)
        {
            error = throwable
        }
        finally
        {
            this.semaphore.release()
        }

        if (error != null)
        {
            throw error
        }

        return result!!
    }

    fun <R : Any> write(writer: () -> R): R
    {
        var result: R? = null
        var error: Throwable? = null
        this.semaphore.acquire(this.numberMaxReader)

        try
        {
            result = writer()
        }
        catch (throwable: Throwable)
        {
            error = throwable
        }
        finally
        {
            this.semaphore.release(this.numberMaxReader)
        }

        if (error != null)
        {
            throw error
        }

        return result!!
    }
}
