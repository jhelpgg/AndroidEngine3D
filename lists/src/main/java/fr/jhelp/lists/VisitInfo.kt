/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.lists

internal class VisitInfo(val min: Int, val max: Int, var firstTime: Boolean = true,
                         var info: String = "")
{
    val middle = (this.min + this.max) / 2
    val explorable get() = this.firstTime && (this.min + 1 < this.max)
}