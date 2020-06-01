/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.multitools.showroom

import android.view.View
import android.widget.TextView
import fr.jhelp.multitools.R

class ShowRoomTitleHolder(baseView: View) : ShowRoomViewHolder<ShowRoomTitle>(baseView)
{
    private val titleTextView = this.baseView.findViewById<TextView>(R.id.titleTextView)

    override fun update(element: ShowRoomTitle)
    {
        this.titleTextView.setText(element.title)
    }
}