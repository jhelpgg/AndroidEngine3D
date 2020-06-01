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
import fr.jhelp.utilities.compare
import fr.jhelp.utilities.log
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

class ContrastImageButtonsFragment : Fragment(), View.OnClickListener
{
    private lateinit var contrastTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.contrast_buttons_fragment, container, false)
        this.contrastTextView = view.findViewById(R.id.textViewContrast)
        view.findViewById<Button>(R.id.buttonRight).setOnClickListener(this)
        view.findViewById<Button>(R.id.buttonLeft).setOnClickListener(this)
        return view
    }

    override fun onClick(view: View)
    {
        var contrast = contrastValue.value()

        when (view.id)
        {
            R.id.buttonRight ->
            {
                contrast =
                    if (contrast.compare(1.0) >= 0)
                    {
                        min(10.0, round(contrast + 1.0))
                    }
                    else
                    {
                        round((contrast + 0.1) * 10.0) / 10.0
                    }
            }
            R.id.buttonLeft  ->
            {
                contrast =
                    if (contrast.compare(1.0) > 0)
                    {
                        round(contrast - 1.0)
                    }
                    else
                    {
                        max(0.0, round((contrast - 0.1) * 10.0) / 10.0)
                    }
            }
        }

        this.contrastTextView.text = "$contrast"
        contrastValue.changeValue(contrast)
    }
}

val contrastValue = Observable(1.0)