/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.graphics.chart

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import fr.jhelp.graphics.view.GraphicView

class BarChartView(context: Context, attributes: AttributeSet?, defaultStyleAttributes: Int,
                   defaultStyleResources: Int) :
    GraphicView(context, attributes, defaultStyleAttributes, defaultStyleResources)
{
    constructor(context: Context, attributes: AttributeSet?, defaultStyleAttributes: Int) :
            this(context, attributes, defaultStyleAttributes, 0)

    constructor(context: Context, attributes: AttributeSet?) :
            this(context, attributes, 0, 0)

    constructor(context: Context) :
            this(context, null, 0, 0)

    override fun animationStarted()
    {
        TODO("Not yet implemented")
    }

    override fun animationStopped()
    {
        TODO("Not yet implemented")
    }

    override fun updateValues(): Boolean
    {
        TODO("Not yet implemented")
    }

    override fun drawing(canvas: Canvas)
    {
        TODO("Not yet implemented")
    }
}