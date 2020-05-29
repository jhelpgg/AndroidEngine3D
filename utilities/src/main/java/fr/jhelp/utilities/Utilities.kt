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

/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.utilities

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.util.Log
import androidx.annotation.StringRes
import fr.jhelp.utilities.extensions.compatible
import fr.jhelp.utilities.extensions.description
import java.util.Locale

/**
 * Print log message with automatic tag management
 */
inline fun log(message: () -> String)
{
    val current = Throwable().stackTrace[0]
    val className = current.className
    val index = className.lastIndexOf('.')
    val tag =
        if (index > 0) className.substring(index + 1)
        else className
    Log.d(tag, "${current.description} : ${message()}")
}

/**
 * Print log error message with automatic tag management
 */
inline fun logError(exception: Exception, message: () -> String)
{
    val current = Throwable().stackTrace[0]
    val className = current.className
    val index = className.lastIndexOf('.')
    val tag =
        if (index > 0) className.substring(index + 1)
        else className
    Log.e(tag, "${current.description} : ${message()}", exception)
}


/**
 * Create context associate to given locale instead of the current device one
 */
fun localizedContext(locale: Locale, context: Context): Context
{
    if (Locale.getDefault().compatible(locale))
    {
        return context
    }

    val configuration = Configuration(context.resources.configuration)
    configuration.setLocale(locale)
    return context.createConfigurationContext(configuration)
}

/**
 * Obtain resources access with given locale instead of the current device one
 */
fun localizedResources(locale: Locale, context: Context): Resources =
    localizedContext(locale, context).resources


/**
 * Obtain resources string with given locale instead of the current device one
 */
fun localizedString(@StringRes resource: Int, locale: Locale, context: Context): String =
    localizedResources(locale, context).getString(resource)

/**
 * Always true function
 */
val ALWAYS_TRUE = { _: Any -> true }

val Int.seconds get() = this * 1000L
val Int.minutes get() = this.seconds * 60L
val Int.hours get() = this.minutes * 60L
val Int.days get() = this.hours * 24L
