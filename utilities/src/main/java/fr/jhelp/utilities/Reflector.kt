/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.utilities

import java.lang.reflect.Array

/**
 * Default Byte value
 */
const val DEFAULT_BYTE = 0.toByte()
/**
 * Default Character value
 */
const val DEFAULT_CHARACTER = '\u0000'
/**
 * Default Double value
 */
const val DEFAULT_DOUBLE = 0.0
/**
 * Default Float value
 */
const val DEFAULT_FLOAT = 0.0f
/**
 * Default Integer value
 */
const val DEFAULT_INTEGER = 0
/**
 * Default Long value
 */
const val DEFAULT_LONG = 0L
/**
 * Default Short value
 */
const val DEFAULT_SHORT = 0.toShort()

/**
 * Indicates if given class can be considered as void
 *
 * @return `true` if given class can be considered as void
 */
val Class<*>?.isVoid get() =
     this == null || this == Unit.javaClass || Void.TYPE == this || Void::class.java == this

/**
 * Compute a default value for a given class
 *
 * @return Default value
 */
val Class<*>?.defaultValue : Any? get()
{
   if (this == null || this.isVoid)
   {
      return null
   }

   if (this.isPrimitive)
   {
      if (Boolean::class.javaPrimitiveType == this)
      {
         return false
      }

      if (Char::class.javaPrimitiveType == this)
      {
         return '\u0000'
      }

      if (Byte::class.javaPrimitiveType == this)
      {
         return 0.toByte()
      }

      if (Short::class.javaPrimitiveType == this)
      {
         return 0.toShort()
      }

      if (Int::class.javaPrimitiveType == this)
      {
         return 0
      }

      if (Long::class.javaPrimitiveType == this)
      {
         return 0L
      }

      if (Float::class.javaPrimitiveType == this)
      {
         return 0.0f
      }

      if (Double::class.javaPrimitiveType == this)
      {
         return 0.0
      }
   }

   if (Boolean::class.java == this)
   {
      return java.lang.Boolean.FALSE
   }

   if (Char::class.java == this)
   {
      return DEFAULT_CHARACTER
   }

   if (Byte::class.java == this)
   {
      return DEFAULT_BYTE
   }

   if (Short::class.java == this)
   {
      return DEFAULT_SHORT
   }

   if (Int::class.java == this)
   {
      return DEFAULT_INTEGER
   }

   if (Long::class.java == this)
   {
      return DEFAULT_LONG
   }

   if (Float::class.java == this)
   {
      return DEFAULT_FLOAT
   }

   if (Double::class.java == this)
   {
      return DEFAULT_DOUBLE
   }

   if (this.isEnum)
   {
      try
      {
         val values = this.getMethod("values").invoke(null)

         if (values != null && Array.getLength(values) > 0)
         {
            return Array.get(values, 0)
         }
      }
      catch (ignored: Exception)
      {
      }

   }

   if (CharSequence::class.java.isAssignableFrom(this))
   {
      return ""
   }

   if (this.isArray)
   {
      var componentType = this.componentType!!
      var count = 1

      while (componentType.isArray)
      {
         componentType = componentType.componentType!!
         count++
      }

      return Array.newInstance(componentType, *IntArray(count))
   }

   return null
}


