/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database.condition

abstract class DataCondition(internal val field:String)
{
    internal abstract fun condition(): String
    internal abstract fun fill(arguments: MutableList<String>)

    internal fun create(arguments: MutableList<String>): String
    {
        this.fill(arguments)
        return this.condition()
    }
}
