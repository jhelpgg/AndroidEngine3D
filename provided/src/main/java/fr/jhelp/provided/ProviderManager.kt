/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.provided

import kotlin.reflect.KClass

object ProviderManager
{
    private val providedProducers = HashMap<String, Producer<*>>()

    @Synchronized
    fun <T : Any> provide(producer: () -> T, kclass: KClass<T>, qualifier: String, single:Boolean)
    {
        this.providedProducers["${kclass.qualifiedName}:$qualifier"] = Producer(single, producer)
    }

    @Synchronized
    fun <T : Any> forget(kclass: KClass<T>, qualifier: String)
    {
        this.providedProducers.remove("${kclass.qualifiedName}:$qualifier")
    }

    @Synchronized
    internal fun provided(qualifier: String) =
        try
        {
            this.providedProducers.getValue(qualifier).value()
        }
        catch (exception: Exception)
        {
            throw IllegalArgumentException(
                "No definition for $qualifier, call 'share' before use the instance", exception)
        }

}