/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.multitools.engine

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.jhelp.multitools.R

class TextElementAdapter<T : Any>(private val elementClickListener: (T) -> Unit,
                                  vararg elements: T) : RecyclerView.Adapter<TextElementHolder<T>>()
{
    private val elements = ArrayList<T>()

    init
    {
        for (element in elements)
        {
            this.elements.add(element)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextElementHolder<T>
    {
        val layoutInflater = parent.context.getSystemService(LayoutInflater::class.java)!!
        val layout = layoutInflater.inflate(R.layout.text_only, parent, false)
        return TextElementHolder(layout, this)
    }

    override fun getItemCount(): Int =
        this.elements.size

    override fun onBindViewHolder(holder: TextElementHolder<T>, position: Int)
    {
        holder.element(this.elements[position])
    }

    internal fun elementClicked(element: T)
    {
        this.elementClickListener(element)
    }
}