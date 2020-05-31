/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.tasks.strand

import fr.jhelp.tasks.promise.Promise
import java.lang.reflect.Method

internal class StrandHandlerElement(val method: Method,
                                    val args: Array<out Any>?,
                                    val promise: Promise<Any>)
