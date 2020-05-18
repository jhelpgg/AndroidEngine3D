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
