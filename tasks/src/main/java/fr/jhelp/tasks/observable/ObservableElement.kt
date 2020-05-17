package fr.jhelp.tasks.observable

import fr.jhelp.tasks.ThreadType


internal class ObservableElement<V : Any>(val threadType: ThreadType,
                                          val matcher: (V) -> Boolean,
                                          val observer: (V) -> Unit)