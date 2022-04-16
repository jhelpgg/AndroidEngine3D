/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database.query.condition

internal class AndCondition(private val databaseCondition1: DatabaseCondition,
                            private val databaseCondition2: DatabaseCondition,
                            private vararg val databaseConditions: DatabaseCondition)
    : DatabaseCondition
{
    override fun where(stringBuilder: StringBuilder)
    {
        stringBuilder.append("(")
        this.databaseCondition1.where(stringBuilder)
        stringBuilder.append(") AND (")
        this.databaseCondition2.where(stringBuilder)
        stringBuilder.append(")")

        for (databaseCondition in this.databaseConditions)
        {
            stringBuilder.append(" AND (")
            databaseCondition.where(stringBuilder)
            stringBuilder.append(")")
        }
    }
}
