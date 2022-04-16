/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database.query

import fr.jhelp.database.DatabaseObject
import fr.jhelp.database.OrCreation
import fr.jhelp.database.query.condition.OrCondition

@OrCreation
class OrCreator<DO : DatabaseObject> internal constructor (private val objectClass: Class<DO>)
{
    private val queries = ArrayList<DatabaseQuery<DO>>()

    @OrCreation
    fun or(fillQuery: DatabaseQuery<DO>.() -> Unit)
    {
        val query = DatabaseQuery<DO>(this.objectClass)
        query.fillQuery()
        this.queries.add(query)
    }

    internal fun buildCondition(): OrCondition =
        when
        {
            this.queries.size < 2  -> throw IllegalStateException(
                "Must have at least two queries for create a OR")
            this.queries.size == 2 -> OrCondition(this.queries[0].andConditions(),
                                                  this.queries[1].andConditions())
            else                   ->
            {
                val others = this.queries
                    .subList(2, this.queries.size)
                    .map { query -> query.andConditions() }
                    .toTypedArray()
                OrCondition(this.queries[0].andConditions(), this.queries[1].andConditions(),
                            *others)
            }
        }
}
