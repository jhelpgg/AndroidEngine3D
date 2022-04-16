/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database.query.condition

internal fun andConditions(vararg conditions: DatabaseCondition): DatabaseCondition =
    when (conditions.size)
    {
        0    -> throw IllegalArgumentException("Need at least one parameter")
        1    -> conditions[0]
        2    -> AndCondition(conditions[0], conditions[1])
        else ->
        {
            val subArray = conditions.copyOfRange(2, conditions.size)
            AndCondition(conditions[0], conditions[1], *subArray)
        }
    }
