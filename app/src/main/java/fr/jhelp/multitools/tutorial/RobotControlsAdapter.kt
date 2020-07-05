/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.multitools.tutorial

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.jhelp.multitools.R

class RobotControlsAdapter(vararg elements: Pair<Int, () -> Unit>) :
    RecyclerView.Adapter<RobotControlsHolder>()
{
    private val elements = Array(elements.size) { index -> elements[index] }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RobotControlsHolder
    {
        val layoutInflater = parent.context.getSystemService(LayoutInflater::class.java)!!
        val layout = layoutInflater.inflate(R.layout.text_only, parent, false)
        return RobotControlsHolder(layout)
    }

    override fun getItemCount(): Int =
        this.elements.size

    override fun onBindViewHolder(holder: RobotControlsHolder, position: Int)
    {
        val (textId, action) = this.elements[position]
        holder.set(textId, action)
    }
}