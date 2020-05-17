package fr.jhelp.images.crawler

import fr.jhelp.images.Point
import kotlin.math.abs
import kotlin.math.sign

class SegmentCrawler(startX: Int, startY: Int,
                     private val endX: Int, private val endY: Int)
{
    constructor(point1: Point, point2: Point) : this(point1.x, point2.y, point2.x, point2.y)

    var hasNext = true
        private set
    private var x = startX
    private var y = startY
    private var error = 0
    private val dx = abs(this.endX - startX)
    private val dy = abs(this.endY - startY)
    private val sx = (this.endX - startX).sign
    private val sy = (this.endY - startY).sign
    private val next =
        if (this.dx >= this.dy) this::nextDx
        else this::nextDy

    fun next(step: Int): Point
    {
        if (step <= 0 || !this.hasNext)
        {
            return Point(this.x, this.y)
        }

        var xx = this.x
        var yy = this.y

        for (count in 0 until step)
        {
            if (this.x == this.endX && this.y == this.endY)
            {
                this.hasNext = false
                return Point(this.endX, this.endY)
            }

            xx = this.x
            yy = this.y
            this.next()
        }

        return Point(xx, yy)
    }

    private fun nextDx()
    {
        this.x += this.sx
        this.error += this.dy

        if (this.error >= this.dx)
        {
            this.error -= this.dx
            this.y += this.sy
        }
    }

    private fun nextDy()
    {
        this.y += this.sy
        this.error += this.dx

        if (this.error >= this.dy)
        {
            this.error -= this.dy
            this.x += this.sx
        }
    }
}