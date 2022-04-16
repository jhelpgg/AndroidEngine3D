/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.testor.database

import android.content.ContentResolver
import android.database.CharArrayBuffer
import android.database.ContentObserver
import android.database.Cursor
import android.database.DataSetObserver
import android.net.Uri
import android.os.Bundle
import java.sql.ResultSet
import java.sql.Statement

/**
 * Cursor based on ResultSet
 */
class CursorResultSet internal constructor(private val statement: Statement,
                                           private val resultSet: ResultSet) : Cursor
{
    /**
     * Number rows in result.
     * This methods maybe slow
     */
    override fun getCount(): Int =
        this.resultSet.fetchSize

    /**
     * Current row read position
     */
    override fun getPosition(): Int =
        this.resultSet.row

    /**
     * Move a number of ofsset
     */
    override fun move(offset: Int): Boolean =
        this.resultSet.relative(offset)

    /**
     * Put a result position
     */
    override fun moveToPosition(position: Int): Boolean =
        this.resultSet.absolute(position)

    /**
     * Put a first result
     */
    override fun moveToFirst(): Boolean =
        this.resultSet.first()

    /**
     * Put t last result
     */
    override fun moveToLast(): Boolean =
        this.resultSet.last()

    /**
     * Next result
     */
    override fun moveToNext(): Boolean =
        this.resultSet.next()

    /**
     * Previous result
     */
    override fun moveToPrevious(): Boolean =
        this.resultSet.previous()

    /**
     * Indicates if result is the first one
     */
    override fun isFirst(): Boolean =
        this.resultSet.isFirst

    /**
     * Indicates if result is the last one
     */
    override fun isLast(): Boolean =
        this.resultSet.isLast

    /**
     * Indicates if we are just before first result
     */
    override fun isBeforeFirst(): Boolean =
        this.resultSet.isBeforeFirst

    /**
     * Indicates if we are just afer last result
     */
    override fun isAfterLast(): Boolean =
        this.resultSet.isAfterLast

    /**
     * Column index by its name
     *
     * -1 if not found
     */
    override fun getColumnIndex(columnName: String): Int =
        try
        {
            this.resultSet.findColumn(columnName) - 1
        }
        catch (_: Exception)
        {
            -1
        }

    /**
     * Column index by its name
     *
     * Throw exception if not found
     */
    override fun getColumnIndexOrThrow(columnName: String): Int =
        this.resultSet.findColumn(columnName) - 1

    /**
     * Column at index name
     */
    override fun getColumnName(columnIndex: Int): String =
        this.resultSet.metaData.getColumnName(columnIndex + 1)

    /**
     * All collected columns
     */
    override fun getColumnNames(): Array<String> =
        this.resultSet.metaData.let { metadata ->
            Array<String>(metadata.columnCount) { index -> metadata.getColumnName(index) }
        }

    /**
     * Number of result column
     */
    override fun getColumnCount(): Int =
        this.resultSet.metaData.columnCount

    /**
     * Get a blob value
     */
    override fun getBlob(columnIndex: Int): ByteArray =
        this.resultSet.getBytes(columnIndex + 1)

    /**
     * Get a String value
     */
    override fun getString(columnIndex: Int): String =
        this.resultSet.getString(columnIndex + 1)

    override fun copyStringToBuffer(columnIndex: Int, buffer: CharArrayBuffer)
    {
        TODO("Not yet implemented")
    }

    /**
     * Get a Short value
     */
    override fun getShort(columnIndex: Int): Short =
        this.resultSet.getShort(columnIndex + 1)

    /**
     * Get a Int value
     */
    override fun getInt(columnIndex: Int): Int =
        this.resultSet.getInt(columnIndex + 1)

    /**
     * Get a Long value
     */
    override fun getLong(columnIndex: Int): Long =
        this.resultSet.getLong(columnIndex + 1)

    /**
     * Get a Float value
     */
    override fun getFloat(columnIndex: Int): Float =
        this.resultSet.getFloat(columnIndex + 1)

    /**
     * Get a Double value
     */
    override fun getDouble(columnIndex: Int): Double =
        this.resultSet.getDouble(columnIndex + 1)

    override fun getType(columnIndex: Int): Int
    {
        TODO("Not yet implemented")
    }

    override fun isNull(columnIndex: Int): Boolean
    {
        TODO("Not yet implemented")
    }

    override fun deactivate() = Unit

    override fun requery(): Boolean = false

    /**
     * Close properly the cursor
     */
    override fun close()
    {
        this.resultSet.close()
        this.statement.close()
    }

    /**
     * Indicates idf cursor is closed
     */
    override fun isClosed(): Boolean =
        this.resultSet.isClosed

    override fun registerContentObserver(observer: ContentObserver)
    {
        TODO("Not yet implemented")
    }

    override fun unregisterContentObserver(observer: ContentObserver)
    {
        TODO("Not yet implemented")
    }

    override fun registerDataSetObserver(observer: DataSetObserver)
    {
        TODO("Not yet implemented")
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver)
    {
        TODO("Not yet implemented")
    }

    override fun setNotificationUri(cr: ContentResolver, uri: Uri)
    {
        TODO("Not yet implemented")
    }

    override fun getNotificationUri(): Uri
    {
        TODO("Not yet implemented")
    }

    override fun getWantsAllOnMoveCalls(): Boolean
    {
        TODO("Not yet implemented")
    }

    override fun setExtras(extras: Bundle?)
    {
        TODO("Not yet implemented")
    }

    override fun getExtras(): Bundle?
    {
        TODO("Not yet implemented")
    }

    override fun respond(extras: Bundle?): Bundle?
    {
        TODO("Not yet implemented")
    }
}
