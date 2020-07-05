/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.multitools.tutorial

import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import fr.jhelp.multitools.R

class RobotControlsHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener
{
    private val textView = this.itemView.findViewById<TextView>(R.id.text)
    private lateinit var action: () -> Unit

    fun set(@StringRes textId: Int, action: () -> Unit)
    {
        this.textView.setText(textId)
        this.action = action
        this.textView.setOnClickListener(this)
    }

    override fun onClick(v: View)
    {
        this.action()
    }
}