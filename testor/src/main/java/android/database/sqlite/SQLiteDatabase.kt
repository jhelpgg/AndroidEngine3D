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

    fun delete(table: String, whereClause: String?, whereArgs: Array<String>?): Int =
        this.database.delete(table, whereClause, whereArgs)

    fun rawQuery(sql: String, selectionArgs: Array<String>?): Cursor =
        this.database.rawQuery(sql, selectionArgs)

    fun update(table: String, values: ContentValues,
               whereClause: String?, whereArgs: Array<String>?): Int =
        this.database.update(table, values, whereClause, whereArgs)

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

    fun beginTransaction()
    {
        this.database.beginTransaction()
    }

    fun beginTransactionNonExclusive()
    {
        this.database.beginTransaction()
    }

    fun beginTransactionWithListener(transactionListener: SQLiteTransactionListener)
    {
        this.database.beginTransaction(transactionListener)
    }

    fun beginTransactionWithListenerNonExclusive(transactionListener: SQLiteTransactionListener)
    {
        this.database.beginTransaction(transactionListener)
    }

    fun setTransactionSuccessful()
    {
        this.database.validateTransaction()
    }

    fun endTransaction()
    {
        this.database.endTransaction()
    }

    fun close()
    {
        this.database.close()
    }
}
