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

package fr.jhelp.utilities.extensions

fun String?.int(defaultValue:Int) =
    this?.let { string ->
        try
        {
            string.toInt()
        }
        catch(_:Exception)
        {
            defaultValue
        }
    } ?: defaultValue

fun String?.long(defaultValue:Long) =
    this?.let { string ->
        try
        {
            string.toLong()
        }
        catch(_:Exception)
        {
            defaultValue
        }
    } ?: defaultValue

fun String?.float(defaultValue:Float) =
    this?.let { string ->
        try
        {
            string.toFloat()
        }
        catch(_:Exception)
        {
            defaultValue
        }
    } ?: defaultValue


fun String?.double(defaultValue:Double) =
    this?.let { string ->
        try
        {
            string.toDouble()
        }
        catch(_:Exception)
        {
            defaultValue
        }
    } ?: defaultValue