package fr.jhelp.images.path

import fr.jhelp.images.Point2D

class Segment(val startX: Float, val startY: Float, var startValue: Float,
              val endX: Float, val endY: Float, var endValue: Float)
{
    constructor(start:Point2D, startValue: Float, end:Point2D, endValue: Float) :
            this(start.x, start.y, startValue, end.x, end.y, endValue)
}