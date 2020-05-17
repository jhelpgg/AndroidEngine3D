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

class Angle(value: Double, val unit: AngleUnit) : Comparable<Angle>
{
    val value = this.unit.modularize(value)

    operator fun plus(angle: Angle) = Angle(this.value + angle.convert(this.unit).value, this.unit)

    operator fun minus(angle: Angle) = Angle(this.value - angle.convert(this.unit).value, this.unit)

    operator fun times(factor: Number) = Angle(this.value * factor.toDouble(), this.unit)

    operator fun div(factor: Number) = Angle(this.value / factor.toDouble(), this.unit)

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

    fun cos() = cos(this.convert(AngleUnit.RADIAN).value)
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
