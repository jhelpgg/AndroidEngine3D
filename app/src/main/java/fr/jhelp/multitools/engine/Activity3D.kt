/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.multitools.engine

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.jhelp.engine.view.View3D
import fr.jhelp.multitools.R

abstract class Activity3D : Activity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_3d_samples)
        val list = this.findViewById<RecyclerView>(R.id.actionsList)
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = this.listAdapter()
        this.fill3D(this.findViewById(R.id.view3D))
    }

    abstract fun listAdapter() :  RecyclerView.Adapter<*>

    abstract fun fill3D(view3D:View3D)
}