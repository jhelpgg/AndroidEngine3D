/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.multitools.images

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import fr.jhelp.multitools.R
import fr.jhelp.tasks.observable.Observable
import kotlin.math.max
import kotlin.math.min

private const val SETEP = 10

class DarkerImageButtonsFragment : Fragment(), View.OnClickListener
{
    private lateinit var darkerTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.darker_buttons_fragment, container, false)
        this.darkerTextView = view.findViewById(R.id.textViewDark)
        view.findViewById<Button>(R.id.buttonRight).setOnClickListener(this)
        view.findViewById<Button>(R.id.buttonLeft).setOnClickListener(this)
        return view
    }

    override fun onClick(view: View)
    {
        var dark = darkValue.value()

        when (view.id)
        {
            R.id.buttonRight -> dark = min(255, dark + SETEP)
            R.id.buttonLeft  -> dark = max(0, dark - SETEP)
        }

        this.darkerTextView.text = "$dark"
        darkValue.changeValue(dark)
    }
}

val darkValue = Observable(0)