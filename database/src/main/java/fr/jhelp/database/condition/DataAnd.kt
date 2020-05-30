/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database.condition

class DataAnd(private val first: DataCondition, private val second: DataCondition) : DataCondition(first.field)
{
    private val condition = "(${this.first.condition()}) AND (${this.second.condition()})"

    init
    {
        if(this.first.field != this.second.field)
        {
            throw IllegalArgumentException("first and second must refer the same field. Here first=>${this.first.field} and second=>${this.second.field}")
        }
    }

    override fun condition() = this.condition

    override fun fill(arguments: MutableList<String>)
    {
        this.first.fill(arguments)
        this.second.fill(arguments)
    }
}