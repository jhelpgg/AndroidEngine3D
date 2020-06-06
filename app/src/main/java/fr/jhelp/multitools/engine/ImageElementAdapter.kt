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
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import fr.jhelp.lists.ArrayInt
import fr.jhelp.multitools.R

class ImageElementAdapter(private val elementClickListener: (Int) -> Unit,
                          @DrawableRes vararg elements: Int) :
    RecyclerView.Adapter<ImageElementHolder>()
{
    private val elements = ArrayInt()

    init
    {
        for (element in elements)
        {
            this.elements.add(element)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageElementHolder
    {
        val layoutInflater = parent.context.getSystemService(LayoutInflater::class.java)!!
        val layout = layoutInflater.inflate(R.layout.image_only, parent, false)
        return ImageElementHolder(layout, this)
    }

    override fun getItemCount(): Int =
        this.elements.size

    override fun onBindViewHolder(holder: ImageElementHolder, position: Int)
    {
        holder.element(this.elements[position])
    }

    internal fun elementClicked(@DrawableRes element: Int)
    {
        this.elementClickListener(element)
    }
}