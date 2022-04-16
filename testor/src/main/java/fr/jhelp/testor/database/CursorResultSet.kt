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

class CursorResultSet internal constructor(private val statement: Statement,
                                           private val resultSet: ResultSet) : Cursor
{
    override fun getCount(): Int =
        this.resultSet.fetchSize

    override fun getPosition(): Int =
        this.resultSet.row

    override fun move(offset: Int): Boolean =
        this.resultSet.relative(offset)

    override fun moveToPosition(position: Int): Boolean =
        this.resultSet.absolute(position)

    override fun moveToFirst(): Boolean =
        this.resultSet.first()

    override fun moveToLast(): Boolean =
        this.resultSet.last()

    override fun moveToNext(): Boolean =
        this.resultSet.next()

    override fun moveToPrevious(): Boolean =
        this.resultSet.previous()

    override fun isFirst(): Boolean =
        this.resultSet.isFirst

    override fun isLast(): Boolean =
        this.resultSet.isLast

    override fun isBeforeFirst(): Boolean =
        this.resultSet.isBeforeFirst

    override fun isAfterLast(): Boolean =
        this.resultSet.isAfterLast

    override fun getColumnIndex(columnName: String): Int =
        try
        {
            this.resultSet.findColumn(columnName) - 1
        }
        catch (_: Exception)
        {
            -1
        }

    override fun getColumnIndexOrThrow(columnName: String): Int =
        this.resultSet.findColumn(columnName) - 1

    override fun getColumnName(columnIndex: Int): String =
        this.resultSet.metaData.getColumnName(columnIndex + 1)

    override fun getColumnNames(): Array<String> =
        this.resultSet.metaData.let { metadata ->
            Array<String>(metadata.columnCount) { index -> metadata.getColumnName(index) }
        }

    override fun getColumnCount(): Int =
        this.resultSet.metaData.columnCount

    override fun getBlob(columnIndex: Int): ByteArray =
        this.resultSet.getBytes(columnIndex + 1)

    override fun getString(columnIndex: Int): String =
        this.resultSet.getString(columnIndex + 1)

    override fun copyStringToBuffer(columnIndex: Int, buffer: CharArrayBuffer)
    {
        TODO("Not yet implemented")
    }

    override fun getShort(columnIndex: Int): Short =
        this.resultSet.getShort(columnIndex + 1)

    override fun getInt(columnIndex: Int): Int =
        this.resultSet.getInt(columnIndex + 1)

    override fun getLong(columnIndex: Int): Long =
        this.resultSet.getLong(columnIndex + 1)

    override fun getFloat(columnIndex: Int): Float =
        this.resultSet.getFloat(columnIndex + 1)

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

    override fun close()
    {
        this.resultSet.close()
        this.statement.close()
    }

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
