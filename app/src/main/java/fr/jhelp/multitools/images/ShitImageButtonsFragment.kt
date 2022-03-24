/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.multitools.images

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import fr.jhelp.images.Point
import fr.jhelp.multitools.R
import fr.jhelp.tasks.observable.Observable
import fr.jhelp.tasks.observable.ObservableValue

private const val STEP = 10
private const val REPEAT_TIME = 128L

class ShitImageButtonsFragment : Fragment(), View.OnTouchListener
{
    private lateinit var coordinatesTextView: TextView
    private var lastActionTime = SystemClock.elapsedRealtime()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.shift_buttons_fragment, container, false)
        this.coordinatesTextView = view.findViewById(R.id.textViewCoordinates)
        view.findViewById<Button>(R.id.buttonUp).setOnTouchListener(this)
        view.findViewById<Button>(R.id.buttonRight).setOnTouchListener(this)
        view.findViewById<Button>(R.id.buttonLeft).setOnTouchListener(this)
        view.findViewById<Button>(R.id.buttonDown).setOnTouchListener(this)
        return view
    }

    override fun onTouch(view: View, event: MotionEvent): Boolean
    {
        if (SystemClock.elapsedRealtime() - this.lastActionTime < REPEAT_TIME)
        {
            return true
        }

        this.lastActionTime = SystemClock.elapsedRealtime()
        var (x, y) = shiftImageCoordinates.value

        when (view.id)
        {
            R.id.buttonUp    -> y -= STEP
            R.id.buttonRight -> x += STEP
            R.id.buttonLeft  -> x -= STEP
            R.id.buttonDown  -> y += STEP
        }

        this.coordinatesTextView.text = "($x, $y)"
        shiftImageCoordinatesObservableValue.value = Point(x, y)
        return true
    }
}

internal val shiftImageCoordinatesObservableValue = ObservableValue<Point>(Point(0, 0))
val shiftImageCoordinates: Observable<Point> = shiftImageCoordinatesObservableValue.observable