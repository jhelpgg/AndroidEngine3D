/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package android.database.sqlite

import android.content.ContentValues
import android.database.Cursor
import fr.jhelp.testor.database.Database

/**
 * Emulates SQLite database
 */
class SQLiteDatabase private constructor(private val database: Database)
{
    companion object
    {
        @JvmStatic
        fun create(cursorFactor: SQLiteDatabase.CursorFactory?): SQLiteDatabase =
            SQLiteDatabase(Database.create())
    }

    interface CursorFactory
    {
        /**
         * See [SQLiteCursor.SQLiteCursor].
         */
        fun newCursor(db: SQLiteDatabase,
                      masterQuery: SQLiteCursorDriver?,
                      editTable: String?,
                      query: SQLiteQuery?): Cursor?
    }

    /**
     * Execute a database query with no return
     */
    fun execSQL(sql: String)
    {
        if (sql.trim().startsWith("PRAGMA ", ignoreCase = true))
        {
            println(
                "We ignore PRAGMA instructions for now so the following command is ignored : $sql")
            return
        }

        this.database.simpleQuery(sql)
    }

    /**
     * Insert elements in table
     * @param table Table where insert
     * @param nullColumnHack Ignored for now
     * @param values Values to insert
     */
    fun insert(table: String, nullColumnHack: String?, values: ContentValues): Long
    {
        val query = StringBuilder()
        query.append("INSERT INTO ")
        query.append(table)

        query.append(" (")
        val queryValues = StringBuilder()
        queryValues.append(") VALUES (")
        var afterFirst = false

        for ((column, value) in values.serialized)
        {
            if (afterFirst)
            {
                query.append(", ")
                queryValues.append(", ")
            }

            query.append(column)
            queryValues.append(value)

            afterFirst = true
        }

        query.append(queryValues)
        query.append(")")

        return this.database.updateQuery(query.toString()).toLong()
    }

    /**
     * Delete rows in table
     *
     * @param table Table where delete
     * @param whereClause Condition on row that match to be delete
     * @param whereArgs `whereClause` parameters
     */
    fun delete(table: String, whereClause: String?, whereArgs: Array<String>?): Int =
        this.database.delete(table, whereClause, whereArgs)

    /**
     * Do request that returns a list of rows, like select
     *
     * @param sql Query
     * @param selectionArgs Query parameters
     * @return Cursor to read the result
     */
    fun rawQuery(sql: String, selectionArgs: Array<String>?): Cursor =
        this.database.rawQuery(sql, selectionArgs)

    /**
     * Update rows in table
     *
     * @param table Table to modify
     * @param values Values to change
     * @param whereClause Conditions on rows to change
     * @param whereArgs Arguments of `whereClause`
     */
    fun update(table: String, values: ContentValues,
               whereClause: String?, whereArgs: Array<String>?): Int =
        this.database.update(table, values, whereClause, whereArgs)

    /**
     * Do a query get some rows
     *
     * @param table Table to lookup
     * @param columns Columns selected
     * @param selection Condition on row to by selected
     * @param selectionArgs Arguments of `selection`
     * @param groupBy Group condition
     * @param having Having condition
     * @param orderBy Order by condition
     * @param limit Maximum number of rows to select
     */
    @JvmOverloads
    fun query(table: String, columns: Array<String>,
              selection: String?, selectionArgs: Array<String>?,
              groupBy: String?,
              having: String?,
              orderBy: String?,
              limit: String? = null): Cursor
    {
        val query = StringBuilder()

        query.append("SELECT ")
        query.append(columns[0])

        for (index in 1 until columns.size)
        {
            query.append(", ")
            query.append(columns[index])
        }

        query.append(" FROM ")
        query.append(table)

        selection?.let { condition ->
            query.append(" WHERE ")
            query.append(condition)
        }

        groupBy?.let { group ->
            query.append(" GROUP BY ")
            query.append(group)
        }

        having?.let { have ->
            query.append(" HAVING ")
            query.append(have)
        }

        orderBy?.let { order ->
            query.append(" ORDER BY ")
            query.append(order)
        }

        limit?.let { limitDefined ->
            query.append(" LIMIT ")
            query.append(limitDefined)
        }

        return this.database.rawQuery(query.toString(), selectionArgs)
    }

    /**
     * Begin transaction (Only one transaction in same time)
     */
    fun beginTransaction()
    {
        this.database.beginTransaction()
    }

    /**
     * Begin transaction (Only one transaction in same time)
     */
    fun beginTransactionNonExclusive()
    {
        this.database.beginTransaction()
    }

    /**
     * Begin transaction (Only one transaction in same time)
     *
     * @param transactionListener Listener to spy transaction updates
     */
    fun beginTransactionWithListener(transactionListener: SQLiteTransactionListener)
    {
        this.database.beginTransaction(transactionListener)
    }

    /**
     * Begin transaction (Only one transaction in same time)
     *
     * @param transactionListener Listener to spy transaction updates
     */
    fun beginTransactionWithListenerNonExclusive(transactionListener: SQLiteTransactionListener)
    {
        this.database.beginTransaction(transactionListener)
    }

    /**
     * Make current transaction as success, so it will be commit
     */
    fun setTransactionSuccessful()
    {
        this.database.validateTransaction()
    }

    /**
     * Finish current transaction
     *
     * If [setTransactionSuccessful] was called before, transaction chain will be commit, else it will be rollback
     */
    fun endTransaction()
    {
        this.database.endTransaction()
    }

    /**
     * Close properly the database
     */
    fun close()
    {
        this.database.close()
    }
}
