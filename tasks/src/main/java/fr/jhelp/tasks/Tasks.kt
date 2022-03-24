/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.tasks

import fr.jhelp.tasks.promise.FutureResult

fun <R : Any> (() -> R).parallel(): FutureResult<R> =
    ThreadType.SHORT.parallel(this)

fun <R : Any> (() -> R).parallel(threadType: ThreadType): FutureResult<R> =
    threadType.parallel(this)

fun <P, R : Any> ((P) -> R).parallel(parameter: P): FutureResult<R> =
    ThreadType.SHORT.parallel(parameter, this)

fun <P, R : Any> ((P) -> R).parallel(threadType: ThreadType, parameter: P): FutureResult<R> =
    threadType.parallel(parameter, this)

fun <R : Any> (() -> R).delay(milliseconds: Long): FutureResult<R> =
    ThreadType.SHORT.delay(milliseconds, this)

fun <R : Any> (() -> R).delay(threadType: ThreadType, milliseconds: Long): FutureResult<R> =
    threadType.delay(milliseconds, this)

fun <P, R : Any> ((P) -> R).delay(parameter: P, milliseconds: Long): FutureResult<R> =
    ThreadType.SHORT.delay(milliseconds, parameter, this)

fun <P, R : Any> ((P) -> R).delay(threadType: ThreadType,
                                  parameter: P,
                                  milliseconds: Long): FutureResult<R> =
    threadType.delay(milliseconds, parameter, this)


