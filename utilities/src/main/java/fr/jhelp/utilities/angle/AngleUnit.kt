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

import fr.jhelp.utilities.TWO_PI
import fr.jhelp.utilities.modulo

const val DEGREE_NAME = "deg"
const val RADIAN_NAME = "rad"
const val GRADE_NAME = "grad"

fun angleUnitByName(name: String) = AngleUnit.values().first { it.angleName == name }

/**
 * Managed angle unit
 */
enum class AngleUnit(val angleName: String, private val modularizeValue: Double)
{
    DEGREE(DEGREE_NAME, 360.0),
    RADIAN(RADIAN_NAME, TWO_PI),
    GRADE(GRADE_NAME, 400.0)
    ;

    /**
     * Modularize the value for this unit
     */
    fun modularize(value: Double) = modulo(value, this.modularizeValue)

    /**
     * Modularize the value for this unit
     */
    fun modularize(value: Float) = modulo(value, this.modularizeValue.toFloat())
}
