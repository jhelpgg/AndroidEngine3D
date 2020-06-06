/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.multitools.engine

import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.jhelp.multitools.R

class TextElementHolder<T : Any>(view: View,
                                          private val parent: TextElementAdapter<T>) :
    RecyclerView.ViewHolder(view), View.OnClickListener
{
    private val textView = this.itemView.findViewById<TextView>(R.id.text)
    private lateinit var current: T

    fun element(element: T)
    {
        this.current = element
        this.textView.text = element.toString()
        this.textView.setOnClickListener(this)
    }

    override fun onClick(v: View)
    {
        this.parent.elementClicked(this.current)
    }
}