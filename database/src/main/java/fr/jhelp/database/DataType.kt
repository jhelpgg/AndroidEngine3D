/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database

import androidx.annotation.Keep

@Keep
enum class DataType(internal val columnValue: String)
{
    INTEGER(COLUMN_VALUE_INTEGER),
    NUMBER(COLUMN_VALUE_NUMBER),
    TEXT(COLUMN_VALUE_TEXT),
    OBJECT(COLUMN_VALUE_OBJECT_ID)
}