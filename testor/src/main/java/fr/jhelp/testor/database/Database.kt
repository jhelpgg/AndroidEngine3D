/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.testor.database

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteTransactionListener
import fr.jhelp.testor.io.createFile
import fr.jhelp.testor.io.databaseDirectory
import fr.jhelp.testor.io.deleteFull
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

/**
 * Real database in Hsql
 *
 * @param name Database name
 * @param deleteOnClose Indicates if have to remove the database when we close it
 */
class Database private constructor(val name: String, val deleteOnClose: Boolean)
{
    companion object
    {
        private val memoryDatabaseId = AtomicInteger(0)

        init
        {
            Class.forName("org.hsqldb.jdbcDriver")
                .getConstructor()
                .newInstance()
        }

        /**
         * Create memory database
         *
         * Memory database will be deleted when close
         */
        fun create(): Database =
            Database("memory_${Database.memoryDatabaseId.getAndIncrement()}", true)

        /**
         * Create real database
         * @param name Database name
         */
        fun create(name: String): Database =
            Database(name, false)
    }

    private val status = AtomicReference<DatabaseStatus>(DatabaseStatus.NORMAL)
    private val directory = databaseDirectory(this.name)
    private val databaseConnection: Connection =
        DriverManager.getConnection(
            "jdbc:hsqldb:file:${File(this.directory, this.name).createFile().absolutePath}",
            "SA", "")
    private val startTrace: Throwable = Throwable()
    private var statusTrace: Throwable = this.startTrace
    private var transactionListener: SQLiteTransactionListener? = null

    /**
     * Make a query that no need result and not modify the database
     * @param query Query to do
     */
    fun simpleQuery(query: String)
    {
        println(query)
        println(";")
        println()

        this.checkNotClosed()

        if (query.isEmpty())
        {
            throw IllegalArgumentException("Query must not be empty !")
        }

        val statement = this.databaseConnection.createStatement()
        statement.executeQuery(convertSqliteToHsql(query))
        statement.close()
        this.databaseConnection.commit()
    }

    /**
     * Make a query that modify the database: [createTable], [delete], [update], [insert]
     * @param query Query to do
     */
    fun updateQuery(query: String): Int
    {
        println(query)
        println(";")
        println()
        this.checkNotClosed()

        if (query.isNotEmpty())
        {
            try
            {
                val statement = this.databaseConnection.createStatement()
                val count = statement.executeUpdate(convertSqliteToHsql(query))
                statement.close()

                if (this.status.get() == DatabaseStatus.NORMAL)
                {
                    this.databaseConnection.commit()
                }

                return count
            }
            catch (exception: Exception)
            {
                exception.printStackTrace()
                return -1
            }
        }

        return 0
    }

    /**
     * Do query that returns row result list
     */
    fun query(query: String): Cursor
    {
        println(query)
        println(";")
        println()
        this.checkNotClosed()
        val statement = this.databaseConnection.createStatement()
        return CursorResultSet(statement, statement.executeQuery(convertSqliteToHsql(query)))
    }

    /**
     * Delete rows an table
     *
     * @param table Table where remove rows
     * @param whereClause Condition for row deletion
     * @param whereArgs Arguments of `whereClause`
     * @return Number of deleted rows
     */
    fun delete(table: String, whereClause: String?, whereArgs: Array<String>?): Int
    {
        val query = StringBuilder()
        query.append("DELETE FROM ")
        query.append(table)

        if (whereClause != null)
        {
            query.append(" WHERE ")
            query.append(this.replaceArguments(whereClause, whereArgs))
        }

        return this.updateQuery(query.toString())
    }

    /**
     * Update a table rows
     *
     * @param table Table where change rows
     * @param values Values to change
     * @param whereClause Condition on row for change
     * @param whereArgs Arguments of `whereClause`
     * @return Number of updated rows
     */
    fun update(table: String, values: ContentValues,
               whereClause: String?, whereArgs: Array<String>?): Int
    {
        val query = StringBuilder()
        query.append("UPDATE ")
        query.append(table)

        query.append(" SET ")
        var afterFirst = false

        for ((column, value) in values.serialized)
        {
            if (afterFirst)
            {
                query.append(", ")
            }

            query.append(column)
            query.append("=")
            query.append(value)

            afterFirst = true
        }

        whereClause?.let { where ->
            query.append(" WHERE ")
            query.append(this.replaceArguments(where, whereArgs))
        }

        return this.updateQuery(query.toString())
    }

    /**
     * Do query that returns row result list
     */
    fun rawQuery(sql: String, selectionArgs: Array<String>?): Cursor =
        this.query(this.replaceArguments(sql, selectionArgs))

    /**
     * Start a transaction.
     *
     * @param transactionListener Spy of transaction states
     * @throws IllegalStateException If transaction already opened
     */
    fun beginTransaction(transactionListener: SQLiteTransactionListener? = null)
    {
        this.checkNotClosed()
        this.transactionListener = transactionListener

        if (!this.status.compareAndSet(DatabaseStatus.NORMAL, DatabaseStatus.IN_TRANSACTION))
        {
            throw IllegalStateException("There already an opened transaction", this.statusTrace)
        }

        this.statusTrace = Throwable()
        this.transactionListener?.onBegin()
    }

    /**
     * Validate current transaction. It will be commit
     *
     * @throws IllegalStateException If no transaction open or already validated
     */
    fun validateTransaction()
    {
        this.checkNotClosed()

        if (!this.status.compareAndSet(DatabaseStatus.IN_TRANSACTION,
                                       DatabaseStatus.TRANSACTION_WILL_BE_COMMIT))
        {
            throw IllegalStateException("No transaction open or already validated")
        }
    }

    /**
     * Terminate current transaction
     *
     * If commit transaction if [validateTransaction] was called before, et modification during transaction are rollback
     *
     * @throws IllegalStateException If transaction not opened
     */
    fun endTransaction()
    {
        this.checkNotClosed()

        when (this.status.get())
        {
            DatabaseStatus.NORMAL                     ->
                throw IllegalStateException("No transaction open")
            DatabaseStatus.IN_TRANSACTION             ->
            {
                this.transactionListener?.onRollback()
                this.databaseConnection.rollback()
            }
            DatabaseStatus.TRANSACTION_WILL_BE_COMMIT ->
            {
                this.transactionListener?.onCommit()
                this.databaseConnection.commit()
            }
            else                                      ->
                throw RuntimeException("Can't be in this situation : ${this.status.get()}")
        }

        this.transactionListener = null
        this.status.set(DatabaseStatus.NORMAL)
        this.statusTrace = this.startTrace
    }

    /**
     * commit last changes and close properly the database
     *
     * @throws IllegalStateException If transaction is opened
     */
    fun close()
    {
        this.checkNotClosed()
        val normalState = this.status.get() == DatabaseStatus.NORMAL

        if (this.status.compareAndSet(DatabaseStatus.NORMAL, DatabaseStatus.CLOSED))
        {
            this.databaseConnection.commit()

            val statement = this.databaseConnection.createStatement()
            statement.executeQuery("SHUTDOWN")
            statement.close()

            this.databaseConnection.close()
        }

        if (this.deleteOnClose)
        {
            this.directory.deleteFull()
        }

        if (!normalState)
        {
            throw IllegalStateException("A transaction is opened but not closed", this.statusTrace)
        }

        this.statusTrace = Throwable()
    }

    private fun checkNotClosed()
    {
        if (this.status.get() == DatabaseStatus.CLOSED)
        {
            throw IllegalStateException("Database was closed", this.statusTrace)
        }
    }

    private fun replaceArguments(sentenceToResolve: String, arguments: Array<String>?): String
    {
        val resolved = StringBuilder()

        if (arguments == null || arguments.isEmpty())
        {
            resolved.append(sentenceToResolve)
        }
        else
        {
            val size = arguments.size
            var indexStart = 0
            var indexSelection = 0
            var indexReplacement = sentenceToResolve.indexOf('?')

            while (indexSelection < size && indexReplacement > 0)
            {
                resolved.append(sentenceToResolve.substring(indexStart, indexReplacement))
                val value = arguments[indexSelection]
                val needQuote = value.toDoubleOrNull() == null

                if (needQuote)
                {
                    resolved.append("'")
                }

                resolved.append(value)

                if (needQuote)
                {
                    resolved.append("'")
                }

                indexStart = indexReplacement + 1
                indexReplacement = sentenceToResolve.indexOf('?', indexStart)
                indexSelection++
            }

            resolved.append(sentenceToResolve.substring(indexStart))
        }

        return resolved.toString()
    }
}