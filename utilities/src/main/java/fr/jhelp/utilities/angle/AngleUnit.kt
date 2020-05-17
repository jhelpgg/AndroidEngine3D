package fr.jhelp.utilities.angle

import fr.jhelp.utilities.TWO_PI
import fr.jhelp.utilities.modulo

const val DEGREE_NAME = "deg"
const val RADIAN_NAME = "rad"
const val GRADE_NAME = "grad"

fun angleUnitByName(name: String) = AngleUnit.values().first { it.angleName == name }

enum class AngleUnit(val angleName: String, private val modularizeValue: Double)
{
    DEGREE(DEGREE_NAME, 360.0),
    RADIAN(RADIAN_NAME, TWO_PI),
    GRADE(GRADE_NAME, 400.0)
    ;

    fun modularize(value: Double) = modulo(value, this.modularizeValue)

    fun modularize(value: Float) = modulo(value, this.modularizeValue.toFloat())
}
