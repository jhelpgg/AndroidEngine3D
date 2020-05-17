package fr.jhelp.tasks.strand

import fr.jhelp.tasks.promise.Promise
import java.lang.reflect.Method

internal class StrandHandlerElement(val method: Method,
                                    val args: Array<out Any>?,
                                    val promise: Promise<Any>)
