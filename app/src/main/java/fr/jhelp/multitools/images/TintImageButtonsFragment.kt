/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.multitools.images

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import fr.jhelp.images.COLOR_BLUE_0500
import fr.jhelp.images.COLOR_GREEN_0500
import fr.jhelp.images.COLOR_RED_0500
import fr.jhelp.images.COLOR_TEAL_0500
import fr.jhelp.multitools.R
import fr.jhelp.tasks.observable.Observable

class TintImageButtonsFragment : Fragment(), View.OnClickListener
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.tint_buttons_fragment, container, false)

        var button = view.findViewById<Button>(R.id.color1Button)
        button.setBackgroundColor(COLOR_BLUE_0500)
        button.setOnClickListener(this)

        button = view.findViewById(R.id.color2Button)
        button.setBackgroundColor(COLOR_RED_0500)
        button.setOnClickListener(this)

        button = view.findViewById(R.id.color3Button)
        button.setBackgroundColor(COLOR_GREEN_0500)
        button.setOnClickListener(this)

        button = view.findViewById(R.id.color4Button)
        button.setBackgroundColor(COLOR_TEAL_0500)
        button.setOnClickListener(this)

        return view
    }

    override fun onClick(v: View)
    {
        tintColorObservable.changeValue((v.background as ColorDrawable).color)
    }
}

val tintColorObservable = Observable(COLOR_BLUE_0500)