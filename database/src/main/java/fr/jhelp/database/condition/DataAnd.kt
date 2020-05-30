/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database.condition

class DataAnd(private val first: DataCondition, private val second: DataCondition) : DataCondition()
{
    private var firstCondition = true

    override fun start()
    {
        this.firstCondition = true
        this.first.start()
        this.second.start()
    }

    override fun nextQuery(parameters: MutableList<String>,
                           executeQuery: (String) -> Long): ConditionResult
    {
        if(this.firstCondition)
        {
            val result = this.first.nextQuery(parameters, executeQuery)

            if(result == ConditionResult.UNKNOWN || result == ConditionResult.INVALID)
            {
                return result
            }

            this.firstCondition = false
            return  ConditionResult.UNKNOWN
        }

        return this.second.nextQuery(parameters, executeQuery)
    }
}