/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.utilities.text

/**
 * Simple interval of characters between two characters
 *
 * To have instance use [createBasicCharactersInterval]
 */
class BasicCharactersInterval internal constructor(val minimum: Char, val maximum: Char)
{
   /**
    * String representation
    */
   override fun toString() = format()

   /**
    * Transform character to string
    * @param character Character to transform
    * @param hexadecimalForm Indicates if use the hexadecimal form
    */
   private fun form(character: Char, hexadecimalForm: Boolean) =
        if (hexadecimalForm)
        {
           val stringBuilder = StringBuilder(4)
           stringBuilder.append(java.lang.Integer.toHexString(character.toInt()))

           while (stringBuilder.length < 4)
           {
              stringBuilder.insert(0, '0')
           }

           stringBuilder.insert(0, "\\u")
           stringBuilder.toString()
        }
        else character.toString()

   /**
    * Compute a string representation on following given format.
    *
    * @param openInterval String used for open interval, when the minimum and maximum are different
    * @param intervalSeparator String used for separate the minimum and the maximum, when they are different
    * @param closeInterval String used for close interval, when the minimum and maximum are different
    * @param openAlone String used for open when interval contains only one character
    * @param closeAlone String used for close when interval contains only one character
    * @param hexadecimalForm Indicates if characters have the hexadecimal form
    */
   fun format(openInterval: String = "[", intervalSeparator: String = ", ",
              closeInterval: String = "]",
              openAlone: String = "{", closeAlone: String = "}",
              hexadecimalForm: Boolean = false) =
        when
        {
           this.minimum > this.maximum  -> "$openInterval$closeInterval"
           this.minimum == this.maximum -> "$openAlone${this.form(this.minimum,
                                                                  hexadecimalForm)}$closeAlone"
           else                         ->
              "$openInterval${this.form(this.minimum,
                                        hexadecimalForm)}$intervalSeparator${this.form(
                   this.maximum,
                   hexadecimalForm)}$closeInterval"
        }

   /**
    * Indicates if an other object is equals to this interval
    */
   override fun equals(other: Any?): Boolean
   {
      if (this === other)
      {
         return true
      }

      if (null == other || other !is BasicCharactersInterval)
      {
         return false
      }

      if (this.empty)
      {
         return other.empty
      }

      if (other.empty)
      {
         return false
      }

      return this.minimum == other.minimum && this.maximum == other.maximum
   }

   /**
    * Hash code
    */
   override fun hashCode(): Int
   {
      if (this.empty)
      {
         return Int.MAX_VALUE
      }

      return this.minimum.hashCode() * 31 + this.maximum.hashCode()
   }

   /**
    * Indicates if interval is empty
    */
   val empty: Boolean get() = this.minimum > this.maximum

   /**
    * Indicates if given character inside the interval
    *
    * Note: It is possible to write:
    *
    * ````Kotlin
    * 'a' in basicCharactersInterval
    * ````
    */
   operator fun contains(character: Char) = character >= this.minimum && character <= this.maximum

   /**
    * Indicates if this interval intersects to given one
    */
   fun intersects(basicCharactersInterval: BasicCharactersInterval) =
        basicCharactersInterval.maximum >= this.minimum && basicCharactersInterval.minimum <= this.maximum

   /**
    * Indicates if this interval intersects for union.
    *
    * Here it will consider `[C, H]` and `[I, M]` intersects because **I** just after **H** so:
    *
    *     [C, H] U [I, M] = [C, M]
    */
   internal fun intersectsUnion(basicCharactersInterval: BasicCharactersInterval) =
        basicCharactersInterval.maximum >= this.minimum - 1 && basicCharactersInterval.minimum <= this.maximum + 1

   /**
    * Compute intersect between this interval and given one. Then return the result.
    *
    * Note: It is possible to write:
    *
    * ````Kotlin
    * val intersectionBasicCharactersInterval = basicCharactersInterval1 * basicCharactersInterval2
    * ````
    */
   operator fun times(basicCharactersInterval: BasicCharactersInterval) =
        when
        {
           this.empty || basicCharactersInterval.empty -> EMPTY_CHARACTERS_INTERVAL
           else                                        ->
              interval(maxOf(basicCharactersInterval.minimum, this.minimum),
                       minOf(basicCharactersInterval.maximum, this.maximum))
        }

   /**
    * Create union between this interval and given one. And return the result
    *
    *  Note: It is possible to write:
    *
    *  ````Kotlin
    *  var unionCharactersInterval = basicCharactersInterval1 + basicCharactersInterval2
    *  ````
    */
   operator fun plus(basicCharactersInterval: BasicCharactersInterval): CharactersInterval
   {
      val result = CharactersInterval()
      result += this
      result += basicCharactersInterval
      return result
   }

   /**
    * Create exclusion of all elements from given one. And return the result
    *
    *  Note: It is possible to write:
    *
    *  ````Kotlin
    *  var excludeCharactersInterval = basicCharactersInterval1 - basicCharactersInterval2
    *  ````
    */
   operator fun minus(basicCharactersInterval: BasicCharactersInterval): CharactersInterval
   {
      val result = CharactersInterval()
      result += this
      result -= basicCharactersInterval
      return result
   }

   /**
    * Create symmetric difference between this interval and given one. And return the result
    *
    *  Note: It is possible to write:
    *
    *  ````Kotlin
    *  var symmetricDifferenceCharactersInterval = basicCharactersInterval1 % basicCharactersInterval2
    *  ````
    */
   operator fun rem(basicCharactersInterval: BasicCharactersInterval): CharactersInterval
   {
      val result = CharactersInterval()
      result += this
      result %= basicCharactersInterval
      return result
   }

   /**
    * Embed the interval to an union of intervals
    */
   fun toCharactersInterval(): CharactersInterval
   {
      val result = CharactersInterval()
      result += this
      return result
   }
}