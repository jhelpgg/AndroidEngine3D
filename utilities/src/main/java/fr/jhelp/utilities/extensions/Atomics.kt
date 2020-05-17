package fr.jhelp.utilities.extensions

import java.util.concurrent.atomic.AtomicReference

/**
 * Do action if the stored value is not `null
 */
fun <T> AtomicReference<T>.ifNotNull(action: (T) -> Unit)
{
   this.get()?.let { action(it) }
}

/**
 * Do an action or an other one depends if content value is `null`or not
 */
fun <P, R> AtomicReference<P>.ifNotNullElse(action: (P) -> R, alternative: () -> R) =
     this.get()?.let { action(it) }
     ?: alternative()
