/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.multitools.engine

import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import fr.jhelp.multitools.R

class ImageElementHolder(view: View,
                         private val parent: ImageElementAdapter) :
    RecyclerView.ViewHolder(view), View.OnClickListener
{
    private val imageView = this.itemView.findViewById<ImageView>(R.id.image)
    private var current = 0

    fun element(@DrawableRes element: Int)
    {
        this.current = element
        this.imageView.setImageResource(element)
        this.imageView.setOnClickListener(this)
    }

    override fun onClick(v: View)
    {
        this.parent.elementClicked(this.current)
    }
}