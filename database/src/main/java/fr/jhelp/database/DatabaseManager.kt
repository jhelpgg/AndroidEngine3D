/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database

import android.database.sqlite.SQLiteDatabase
import fr.jhelp.security.decrypt
import fr.jhelp.security.encrypt
import fr.jhelp.tasks.parallelIO
import fr.jhelp.tasks.promise.FutureResult
import fr.jhelp.tasks.promise.Promise
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.concurrent.atomic.AtomicReference

internal class DatabaseManager(private val encrypted: File, private val clear: File)
{
    private val status = AtomicReference(DatabaseManagerStatus.INITIALIZING)
    private val promiseDatabase = Promise<SQLiteDatabase>()
    val futureDatabase = this.promiseDatabase.future
    private var dirty = false
    private var promiseShutdown = Promise<Unit>()
    val shutdownResult = this.promiseShutdown.future

    init
    {
        parallelIO(this::initialize)
    }

    private fun initialize()
    {
        if (this.clear.exists())
        {
            this.clear.delete()
        }

        if (this.encrypted.exists())
        {
            decrypt({ FileInputStream(this.encrypted) }, { FileOutputStream(this.clear) })
        }

        this.status.set(DatabaseManagerStatus.READY)
        this.promiseDatabase.result(SQLiteDatabase.openOrCreateDatabase(this.clear, null))
    }

    fun shutdown(): FutureResult<Unit>
    {
        this.update()
        this.status.set(DatabaseManagerStatus.SHUTDOWN)
        return this.shutdownResult
    }

    fun update()
    {
        if (this.status.compareAndSet(DatabaseManagerStatus.READY,
                                      DatabaseManagerStatus.ENCRYPTING))
        {
            parallelIO(this::updating)
        }
        else if (this.status.get() != DatabaseManagerStatus.SHUTDOWN)
        {
            this.dirty = true
        }
    }

    private fun updating()
    {
        do
        {
            this.dirty = false
            encrypt({ FileInputStream(this.clear) }, { FileOutputStream(this.encrypted) })
        }
        while (this.dirty)

        if (this.status.get() != DatabaseManagerStatus.SHUTDOWN)
        {
            this.status.set(DatabaseManagerStatus.READY)
        }
        else
        {
            this.clear.delete()
            this.promiseShutdown.result(Unit)
        }
    }
}