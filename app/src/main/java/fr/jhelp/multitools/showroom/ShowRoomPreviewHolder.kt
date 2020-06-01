/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.multitools.showroom

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import fr.jhelp.multitools.R

class ShowRoomPreviewHolder(baseView:View) : ShowRoomViewHolder<ShowRoomPreview>(baseView), View.OnClickListener
{
    private val previewImage = this.baseView.findViewById<ImageView>(R.id.previewImage)
    private val previewText = this.baseView.findViewById<TextView>(R.id.previewText)
    private lateinit var element: ShowRoomPreview

    override fun update(element: ShowRoomPreview)
    {
        this.element=element
        this.previewImage.setImageResource(element.image)
        this.previewText.setText(element.description)
        this.previewImage.setOnClickListener(this)
        this.previewText.setOnClickListener(this)
    }

    override fun onClick(view: View)
    {
        val context = view.context
        val intent = Intent(context, this.element.activityClass)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}