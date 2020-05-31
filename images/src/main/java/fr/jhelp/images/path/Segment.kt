/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.images.path

import fr.jhelp.images.Point2D

/**
 * Represents a segments with associated value at start and at end
 */
class Segment(val startX: Float, val startY: Float, var startValue: Float,
              val endX: Float, val endY: Float, var endValue: Float)
{
    constructor(start: Point2D, startValue: Float, end: Point2D, endValue: Float) :
            this(start.x, start.y, startValue, end.x, end.y, endValue)
}