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
fun Locale.compatible(locale: Locale): Boolean =
     if (this.language != locale.language)
     {
        false
     }
     else
     {
        this.country.isEmpty() || locale.country.isEmpty() || this.country == locale.country
     }
