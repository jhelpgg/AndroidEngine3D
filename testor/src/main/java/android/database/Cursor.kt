/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact. 
 */

package android.database

import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import java.util.Arrays

interface Cursor
{
    companion object
    {
        const val FIELD_TYPE_NULL = 0
        const val FIELD_TYPE_INTEGER = 1
        const val FIELD_TYPE_FLOAT = 2
        const val FIELD_TYPE_STRING = 3
        const val FIELD_TYPE_BLOB = 4
    }

    fun getCount(): Int
    fun getPosition(): Int
    fun move(offset: Int): Boolean
    fun moveToPosition(position: Int): Boolean
    fun moveToFirst(): Boolean
    fun moveToLast(): Boolean
    fun moveToNext(): Boolean
    fun moveToPrevious(): Boolean
    fun isFirst(): Boolean
    fun isLast(): Boolean
    fun isBeforeFirst(): Boolean
    fun isAfterLast(): Boolean
    fun getColumnIndex(columnName: String): Int

    @Throws(IllegalArgumentException::class)
    fun getColumnIndexOrThrow(columnName: String): Int
    fun getColumnName(columnIndex: Int): String
    fun getColumnNames(): Array<String>
    fun getColumnCount(): Int
    fun getBlob(columnIndex: Int): ByteArray
    fun getString(columnIndex: Int): String
    fun copyStringToBuffer(columnIndex: Int,
                           buffer: CharArrayBuffer)

    fun getShort(columnIndex: Int): Short
    fun getInt(columnIndex: Int): Int
    fun getLong(columnIndex: Int): Long
    fun getFloat(columnIndex: Int): Float
    fun getDouble(columnIndex: Int): Double
    fun getType(columnIndex: Int): Int
    fun isNull(columnIndex: Int): Boolean

    @Deprecated("Since {@link #requery()} is deprecated, so too is this.")
    fun deactivate()

    @Deprecated("""Don't use this. Just request a new cursor, so you can do this
      asynchronously and update your list view once the new cursor comes back.""")
    fun requery(): Boolean
    fun close()
    fun isClosed(): Boolean
    fun registerContentObserver(observer: ContentObserver)
    fun unregisterContentObserver(observer: ContentObserver)
    fun registerDataSetObserver(observer: DataSetObserver)
    fun unregisterDataSetObserver(observer: DataSetObserver)
    fun setNotificationUri(cr: ContentResolver, uri: Uri)
    fun setNotificationUris(cr: ContentResolver, uris: List<Uri>)
    {
        setNotificationUri(cr, uris[0])
    }

    fun getNotificationUri(): Uri?
    fun getNotificationUris(): List<Uri>?
    {
        val notifyUri: Uri? = getNotificationUri()
        return if (notifyUri == null) null else listOf(notifyUri)
    }

    fun getWantsAllOnMoveCalls(): Boolean
    fun setExtras(extras: Bundle?)
    fun getExtras(): Bundle?
    fun respond(extras: Bundle?): Bundle?
}