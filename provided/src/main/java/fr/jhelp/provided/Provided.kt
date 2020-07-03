/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.provided

import kotlin.reflect.KProperty

class Provided<T>(private val identifier: String)
{
    operator fun getValue(thisRef: Any, property: KProperty<*>): T =
        ProviderManager.provided(this.identifier) as T
}