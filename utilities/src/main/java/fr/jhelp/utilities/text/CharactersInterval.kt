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

package fr.jhelp.utilities.text

/**
 * Interval composed of union of [BasicCharactersInterval]
 */
class CharactersInterval
{
   /**Union of intervals*/
   private val intervals = ArrayList<BasicCharactersInterval>()

   /**
    * String representation
    */
   override fun toString() = format()

   /**
    * Indicates if interval is empty
    */
   val empty: Boolean get() = this.intervals.isEmpty()

   override fun equals(other: Any?): Boolean
   {
      if (this === other)
      {
         return true
      }

      if (null == other)
      {
         return false
      }

      val compareWith =
           when (other)
           {
              is BasicCharactersInterval -> other.toCharactersInterval()
              is CharactersInterval      -> other
              else                       -> return false
           }

      return this.intervals.equals(compareWith.intervals)
   }

   /**
    * Hash code
    */
   override fun hashCode() = this.intervals.hashCode()

   /**
    * Interval copy
    */
   fun copy(): CharactersInterval
   {
      val copy = CharactersInterval()
      copy.intervals.addAll(this.intervals)
      return copy
   }

   /**
    * Compute a string representation on following given format.
    *
    * @param openInterval String used for open interval, when the minimum and maximum are different
    * @param intervalSeparator String used for separate the minimum and the maximum, when they are different
    * @param closeInterval String used for close interval, when the minimum and maximum are different
    * @param openAlone String used for open when interval contains only one character
    * @param closeAlone String used for close when interval contains only one character
    * @param unionSymbol String used for the union between intervals
    * @param hexadecimalForm Indicates if characters have the hexadecimal form
    */
   fun format(openInterval: String = "[", intervalSeparator: String = ", ", closeInterval: String = "]",
              openAlone: String = "{", closeAlone: String = "}",
              unionSymbol: String = " U ", hexadecimalForm: Boolean = false): String
   {
      if (this.intervals.isEmpty())
      {
         return "$openInterval$closeInterval"
      }

      val stringBuilder = StringBuilder()
      stringBuilder.append(this.intervals[0].format(openInterval, intervalSeparator, closeInterval,
                                                    openAlone, closeAlone, hexadecimalForm))

      (1 until this.intervals.size).forEach {
         stringBuilder.append(unionSymbol)
         stringBuilder.append(this.intervals[it].format(openInterval, intervalSeparator, closeInterval,
                                                        openAlone, closeAlone, hexadecimalForm))
      }

      return stringBuilder.toString()
   }

   /**
    * Do an action for each embed intervals
    * @param action Action to do:
    * * Parameter: Current interval
    */
   fun forEach(action: (BasicCharactersInterval) -> Unit) = this.intervals.forEach(action)

   /**
    * Add an interval to this interval.
    *
    * It adapts the union.
    * @param minimum Interval minimum
    * @param maximum Interval maximum
    */
   fun add(minimum: Char, maximum: Char = minimum)
   {
      this += interval(minimum, maximum)
   }

   /**
    * Remove an interval to this interval.
    *
    * It adapts the union.
    * @param minimum Interval minimum
    * @param maximum Interval maximum
    */
   fun remove(minimum: Char, maximum: Char = minimum)
   {
      this -= interval(minimum, maximum)
   }

   /**
    * Add character to this interval
    */
   operator fun plusAssign(character: Char)
   {
      this += interval(character)
   }

   /**
    * Add interval to this interval
    */
   operator fun plusAssign(basicCharactersInterval: BasicCharactersInterval)
   {
      if (basicCharactersInterval.empty)
      {
         return
      }

      if (this.intervals.isEmpty())
      {
         this.intervals.add(basicCharactersInterval)
         return
      }

      var intervalToAdd = basicCharactersInterval
      var size = this.intervals.size

      (size - 1 downTo 0).forEach {
         val interval = this.intervals[it]

         if (interval.intersectsUnion(intervalToAdd))
         {
            this.intervals.removeAt(it)
            size--
            intervalToAdd = interval(minOf(intervalToAdd.minimum, interval.minimum),
                                     maxOf(intervalToAdd.maximum, interval.maximum))
         }
      }

      var index = 0

      while (index < size && intervalToAdd.maximum >= this.intervals[index].minimum)
      {
         index++
      }

      if (index < size)
      {
         this.intervals.add(index, intervalToAdd)
      }
      else
      {
         this.intervals.add(intervalToAdd)
      }
   }

   /**
    * Add interval to this interval
    */
   operator fun plusAssign(charactersInterval: CharactersInterval) = charactersInterval.forEach { this += it }

   /**
    * Create interval result of union of this interval and character
    */
   operator fun plus(character: Char): CharactersInterval
   {
      val result = this.copy()
      result += character
      return result
   }

   /**
    * Create interval result of union of this interval and given interval
    */
   operator fun plus(basicCharactersInterval: BasicCharactersInterval): CharactersInterval
   {
      val result = this.copy()
      result += basicCharactersInterval
      return result
   }

   /**
    * Create interval result of union of this interval and given interval
    */
   operator fun plus(charactersInterval: CharactersInterval): CharactersInterval
   {
      val result = this.copy()
      result += charactersInterval
      return result
   }

   /**
    * Remove character form this interval
    */
   operator fun minusAssign(character: Char)
   {
      this -= interval(character)
   }

   /**
    * Remove interval form this interval
    */
   operator fun minusAssign(basicCharactersInterval: BasicCharactersInterval)
   {
      if (basicCharactersInterval.empty || this.empty)
      {
         return
      }

      val size = this.intervals.size

      (size - 1 downTo 0).forEach {
         val interval = this.intervals[it]

         if (interval.intersects(basicCharactersInterval))
         {
            this.intervals.removeAt(it)
            this += interval(interval.minimum, basicCharactersInterval.minimum - 1)
            this += interval(basicCharactersInterval.maximum + 1, interval.maximum)
         }
      }
   }

   /**
    * Remove interval form this interval
    */
   operator fun minusAssign(charactersInterval: CharactersInterval) = charactersInterval.forEach { this -= it }

   /**
    * Create interval result of remove character form this interval
    */
   operator fun minus(character: Char): CharactersInterval
   {
      val result = this.copy()
      result -= character
      return result
   }

   /**
    * Create interval result of remove interval form this interval
    */
   operator fun minus(basicCharactersInterval: BasicCharactersInterval): CharactersInterval
   {
      val result = this.copy()
      result -= basicCharactersInterval
      return result
   }

   /**
    * Create interval result of remove interval form this interval
    */
   operator fun minus(charactersInterval: CharactersInterval): CharactersInterval
   {
      val result = this.copy()
      result -= charactersInterval
      return result
   }

   /**
    * Indicates if a character inside the interval
    */
   operator fun contains(character: Char) = this.intervals.any { character in it }

   /**
    * Indicates if given interval intersects this interval
    */
   fun intersects(basicCharactersInterval: BasicCharactersInterval) =
        this.intervals.any { basicCharactersInterval.intersects(it) }

   /**
    * Indicates if given interval intersects this interval
    */
   fun intersects(charactersInterval: CharactersInterval) = this.intervals.any { charactersInterval.intersects(it) }

   /**
    * Create interval result of intersection between this interval and given one
    */
   operator fun times(basicCharactersInterval: BasicCharactersInterval): CharactersInterval
   {
      val result = CharactersInterval()
      this.forEach { result += basicCharactersInterval * it }
      return result
   }

   /**
    * Create interval result of intersection between this interval and given one
    */
   operator fun times(charactersInterval: CharactersInterval): CharactersInterval
   {
      val result = CharactersInterval()
      this.forEach { result += charactersInterval * it }
      return result
   }

   /**
    * The interval become the interction with this interval and given one
    */
   operator fun timesAssign(basicCharactersInterval: BasicCharactersInterval)
   {
      val result = this * basicCharactersInterval
      this.intervals.clear()
      this.intervals.addAll(result.intervals)
   }

   /**
    * The interval become the interction with this interval and given one
    */
   operator fun timesAssign(charactersInterval: CharactersInterval)
   {
      val result = this * charactersInterval
      this.intervals.clear()
      this.intervals.addAll(result.intervals)
   }

   /**
    * Create interval result of symmetric difference between this interval and given one
    */
   operator fun rem(basicCharactersInterval: BasicCharactersInterval) =
        (this + basicCharactersInterval) - (this * basicCharactersInterval)

   /**
    * Create interval result of symmetric difference between this interval and given one
    */
   operator fun rem(charactersInterval: CharactersInterval) = (this + charactersInterval) - (this * charactersInterval)

   /**
    * This interval become the symmetric difference between this interval and given one
    */
   operator fun remAssign(basicCharactersInterval: BasicCharactersInterval)
   {
      val intersection = this * basicCharactersInterval
      this += basicCharactersInterval
      this -= intersection
   }

   /**
    * This interval become the symmetric difference between this interval and given one
    */
   operator fun remAssign(charactersInterval: CharactersInterval)
   {
      val intersection = this * charactersInterval
      this += charactersInterval
      this -= intersection
   }
}