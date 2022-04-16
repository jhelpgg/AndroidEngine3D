/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.utilities.extensions

import java.util.Locale

/**
 * Serialize a [Locale]
 *
 * Can be parse again with [String.parseLocale]
 */
fun Locale.serialize(): String
{
   val stringBuilder = StringBuilder().append(this.language)
   var part = this.country

   if (part.isNotEmpty())
   {
      stringBuilder.append('_').append(part)
      part = this.variant

      if (part.isNotEmpty())
      {
         stringBuilder.append('_').append(part)
      }
   }

   return stringBuilder.toString()
}

/**
 * Parse [Locale] serialized with [Locale.serialize]
 */
fun String.parseLocale(): Locale
{
   var end = this.indexOf('_')

   if (end < 0)
   {
      return Locale(this)
   }

   val language = this.substring(0, end)
   val start = end + 1
   end = this.indexOf('_', start)

   if (end < 0)
   {
      return Locale(language, this.substring(start))
   }

   return Locale(language, this.substring(start, end), this.substring(end + 1))
}

/**
 * Indicates if two locales can be consider as compatible
 */
fun Locale.compatible(locale: Locale): Boolean =
     if (this.language != locale.language)
     {
        false
     }
     else
     {
        this.country.isEmpty() || locale.country.isEmpty() || this.country == locale.country
     }
