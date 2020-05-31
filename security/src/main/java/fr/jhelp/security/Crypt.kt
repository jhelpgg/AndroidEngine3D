/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.security

import fr.jhelp.io.treatInputOutputStream
import fr.jhelp.utilities.logError
import java.io.InputStream
import java.io.OutputStream

/**
 * Helper for encrypt a stream.
 *
 * It manages exception and stream close properly
 *
 * @param clear Create stream to encrypt
 * @param encrypted Create stream where write encrypted version
 * @param error Called if error happen. By default just log the error
 */
fun <I : InputStream, O : OutputStream> encrypt(clear: () -> I, encrypted: () -> O,
                                                error: (Exception) -> Unit = {
                                                    logError(it) { "Issue while encrypting!" }
                                                }) =
    treatInputOutputStream(clear, encrypted,
                           { input, output -> Security.encrypt(input, output) },
                           error)

/**
 * Helper for decrypt a stream.
 *
 * It manages exception and stream close properly
 *
 * @param encrypted Create stream de decrypt
 * @param clear Create stream where write decrypted version
 * @param error Called if error happen. By default just log the error
 */
fun <I : InputStream, O : OutputStream> decrypt(encrypted: () -> I, clear: () -> O,
                                                error: (Exception) -> Unit = {
                                                    logError(it) { "Issue while decrypting!" }
                                                }) =
    treatInputOutputStream(encrypted, clear,
                           { input, output -> Security.decrypt(input, output) },
                           error)