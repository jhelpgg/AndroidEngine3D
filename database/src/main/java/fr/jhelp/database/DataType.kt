/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database

internal enum class DataType
{
    BOOLEAN,
    INT,
    LONG,
    FLOAT,
    DOUBLE,
    STRING,
    ENUM,
    OBJECT
}

internal fun dataTypeFrom(clas: Class<*>): DataType =
    when
    {
        clas == Boolean::class.java                       -> DataType.BOOLEAN
        clas == Int::class.java                           -> DataType.INT
        clas == Long::class.java                          -> DataType.LONG
        clas == Float::class.java                         -> DataType.FLOAT
        clas == Double::class.java                        -> DataType.DOUBLE
        clas == String::class.java                        -> DataType.STRING
        clas.isEnum                                       -> DataType.ENUM
        DatabaseObject::class.java.isAssignableFrom(clas) -> DataType.OBJECT
        else                                              ->
            throw IllegalArgumentException("Not managed type : ${clas.name}")
    }
