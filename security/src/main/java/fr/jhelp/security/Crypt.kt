package fr.jhelp.security

import fr.jhelp.io.treatInputOutputStream
import fr.jhelp.utilities.logError
import java.io.InputStream
import java.io.OutputStream

fun <I : InputStream, O : OutputStream> encrypt(clear: () -> I, encrypted: () -> O,
                                                error: (Exception) -> Unit = {
                                                    logError(it) { "Issue while encrypting!" }
                                                }) =
    treatInputOutputStream(clear, encrypted,
                           { input, output -> Security.encrypt(input, output) },
                           error)

fun <I : InputStream, O : OutputStream> decrypt(encrypted: () -> I, clear: () -> O,
                                                error: (Exception) -> Unit = {
                                                    logError(it) { "Issue while decrypting!" }
                                                }) =
    treatInputOutputStream(encrypted, clear,
                           { input, output -> Security.decrypt(input, output) },
                           error)