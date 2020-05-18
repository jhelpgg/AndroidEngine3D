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

import android.util.Base64

/**
 * Shortcut for transform ByteArray to its base 64 version
 */
fun ByteArray.toBase64(): String =
     Base64.encodeToString(this, Base64.DEFAULT)

/**
 * Shortcut to retrieve ByteArray serialized to base 64
 */
fun String.fromBase64(): ByteArray =
     Base64.decode(this, Base64.DEFAULT)

/**
 * Shortcut to read ByteArray as UTF-8 String
 */
fun ByteArray.fromUtf8(): String =
     String(this, Charsets.UTF_8)

/**
 * Shortcut to convert String to UTF-8 ByteArray
 */
fun String.toUtf8(): ByteArray =
     this.toByteArray(Charsets.UTF_8)
