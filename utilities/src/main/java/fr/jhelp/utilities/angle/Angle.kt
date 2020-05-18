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

package fr.jhelp.utilities.angle

import fr.jhelp.utilities.compare
import fr.jhelp.utilities.degreeToGrade
import fr.jhelp.utilities.gradeToDegree
import fr.jhelp.utilities.gradeToRadian
import fr.jhelp.utilities.radianToGrade
import fr.jhelp.utilities.same
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Represents an angle
 *
 * @param value Angle value
 * @param unit Angle unit, see [AngleUnit]
 */
class Angle(value: Double, val unit: AngleUnit) : Comparable<Angle>
{
    /**Angle value in its unit*/
    val value = this.unit.modularize(value)

    operator fun plus(angle: Angle) = Angle(this.value + angle.convert(this.unit).value, this.unit)

    operator fun minus(angle: Angle) = Angle(this.value - angle.convert(this.unit).value, this.unit)

    operator fun times(factor: Number) = Angle(this.value * factor.toDouble(), this.unit)

    operator fun div(factor: Number) = Angle(this.value / factor.toDouble(), this.unit)

    /**
     * Convert this angle to an other unit
     *
     * If the unit is the same as this angle unit, this angle is returns, else a new angle is created
     */
    fun convert(angleUnit: AngleUnit) =
        when (this.unit)
        {
            AngleUnit.DEGREE ->
                when (angleUnit)
                {
                    AngleUnit.DEGREE -> this
                    AngleUnit.RADIAN -> Angle(Math.toRadians(this.value), AngleUnit.RADIAN)
                    AngleUnit.GRADE  -> Angle(degreeToGrade(this.value), AngleUnit.GRADE)
                }
            AngleUnit.RADIAN ->
                when (angleUnit)
                {
                    AngleUnit.DEGREE -> Angle(Math.toDegrees(this.value), AngleUnit.DEGREE)
                    AngleUnit.RADIAN -> this
                    AngleUnit.GRADE  -> Angle(radianToGrade(this.value), AngleUnit.GRADE)
                }
            AngleUnit.GRADE  ->
                when (angleUnit)
                {
                    AngleUnit.DEGREE -> Angle(gradeToDegree(this.value), AngleUnit.DEGREE)
                    AngleUnit.RADIAN -> Angle(gradeToRadian(this.value), AngleUnit.RADIAN)
                    AngleUnit.GRADE  -> this
                }
        }

    /**
     * Angle cosinus values
     */
    fun cos() = cos(this.convert(AngleUnit.RADIAN).value)

    /**
     * Angle sinus values
     */
    fun sin() = sin(this.convert(AngleUnit.RADIAN).value)

    override fun hashCode() = this.convert(AngleUnit.RADIAN).value.hashCode()

    override fun equals(other: Any?): Boolean
    {
        if (this === other)
        {
            return true
        }

        if (other == null || other !is Angle)
        {
            return false
        }

        return this.convert(AngleUnit.RADIAN).value.same(other.convert(AngleUnit.RADIAN).value)
    }

    override fun toString() = "${this.value}${this.unit.angleName}"

    override operator fun compareTo(other: Angle) =
        this.convert(AngleUnit.RADIAN).value.compare(other.convert(AngleUnit.RADIAN).value)
}

val AngleZero = Angle(0.0, AngleUnit.RADIAN)
val AngleQuarter = Angle(PI / 2.0, AngleUnit.RADIAN)
val AngleMiddle = Angle(PI, AngleUnit.RADIAN)

operator fun Number.times(angle: Angle) = angle * this
