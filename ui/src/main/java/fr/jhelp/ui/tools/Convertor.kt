/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.ui.tools

import android.content.Context
import fr.jhelp.provided.provided

internal object Convertor
{
    private val context: Context by provided<Context>()

    fun dpToPixels(dp: Int): Int =
        (this.context.resources.displayMetrics.density * dp + 0.5f).toInt()

    fun spToPixels(sp: Int): Int =
        (this.context.resources.displayMetrics.scaledDensity * sp + 0.5f).toInt()
}
