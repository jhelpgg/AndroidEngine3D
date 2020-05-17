package fr.jhelp.utilities.extensions

/**
 * Short serialization of list (No [ or space)
 */
fun <T> List<T>.serialize(): String
{
   val stringBuilder = StringBuilder()
   var afterFirst = false

   for (element in this)
   {
      if (afterFirst)
      {
         stringBuilder.append(",")
      }

      afterFirst = true
      stringBuilder.append(element)
   }

   return stringBuilder.toString()
}

/**
 * Short serialization of array (No [ or space)
 */
fun IntArray.serialize(): String
{
   val stringBuilder = StringBuilder()
   var afterFirst = false

   for (element in this)
   {
      if (afterFirst)
      {
         stringBuilder.append(",")
      }

      afterFirst = true
      stringBuilder.append(element)
   }

   return stringBuilder.toString()
}

/**
 * Parse IntArray serialized with [IntArray.serialize]
 */
fun String.parseIntArray(): IntArray
{
   val split = this.split(',')
   return IntArray(split.size) { index -> split[index].toInt() }
}

/**
 * Short serialization of array (No [ or space)
 */
fun LongArray.serialize(): String
{
   val stringBuilder = StringBuilder()
   var afterFirst = false

   for (element in this)
   {
      if (afterFirst)
      {
         stringBuilder.append(",")
      }

      afterFirst = true
      stringBuilder.append(element)
   }

   return stringBuilder.toString()
}

/**
 * Parse LongArray serialized with [LongArray.serialize]
 */
fun String.parseLongArray(): LongArray
{
   val split = this.split(',')
   return LongArray(split.size) { index -> split[index].toLong() }
}

/**
 * Short serialization of array (No [ or space)
 */
fun DoubleArray.serialize(): String
{
   val stringBuilder = StringBuilder()
   var afterFirst = false

   for (element in this)
   {
      if (afterFirst)
      {
         stringBuilder.append(",")
      }

      afterFirst = true
      stringBuilder.append(element)
   }

   return stringBuilder.toString()
}

/**
 * Parse DoubleArray serialized with [DoubleArray.serialize]
 */
fun String.parseDoubleArray(): DoubleArray
{
   val split = this.split(',')
   return DoubleArray(split.size) { index -> split[index].toDouble() }
}
