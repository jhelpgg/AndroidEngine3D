/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.provided

inline fun <reified T : Any> provideSingle(noinline producer: () -> T) =
    provideSingle("", producer)


inline fun <reified T : Any> provideSingle(qualifier: String, noinline producer: () -> T)
{
    ProviderManager.provide(producer, T::class, qualifier, true)
}

inline fun <reified T : Any> provideMultiple(noinline producer: () -> T) =
    provideMultiple("", producer)


inline fun <reified T : Any> provideMultiple(qualifier: String, noinline producer: () -> T)
{
    ProviderManager.provide(producer, T::class, qualifier, false)
}

inline fun <reified T : Any> forget(qualifier: String = "")
{
    ProviderManager.forget(T::class, qualifier)
}

inline fun <reified T> provided(qualifier: String = "") =
    Provided<T>("${T::class.qualifiedName}:$qualifier")

