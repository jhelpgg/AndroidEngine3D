/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.images.path

import fr.jhelp.utilities.angle.AngleFloat
import fr.jhelp.utilities.distance
import fr.jhelp.utilities.nul
import fr.jhelp.utilities.same
import kotlin.math.max

class Path
{
    private val path = ArrayList<PathElement>()
    private var startX = 0f
    private var startY = 0f
    private var x = 0f
    private var y = 0f

    fun copy(): Path
    {
        val copy = Path()
        copy.path.addAll(this.path)
        copy.x = this.x
        copy.y = this.y
        copy.startX = this.startX
        copy.startY = this.startY
        return copy
    }

    fun moveTo(startX: Float, startY: Float) : Path
    {
        this.startX = startX
        this.startY = startY
        this.x = startX
        this.y = startY
        return this
    }

    fun close() : Path
    {
        if (!this.startX.same(this.x) || !this.startY.same(this.y))
        {
            this.path.add(LineElement(this.x, this.y, this.startX, this.startY))
            this.x = this.startX
            this.y = this.startY
        }

        return this
    }

    fun lineTo(endX: Float, endY: Float) : Path
    {
        this.path.add(LineElement(this.x, this.y, endX, endY))
        this.x = endX
        this.y = endY
        return this
    }

    fun quadraticTo(controlX: Float, controlY: Float,
                    endX: Float, endY: Float) : Path
    {
        this.path.add(QuadraticElement(this.x, this.y,
                                       controlX, controlY,
                                       endX, endY))
        this.x = endX
        this.y = endY
        return this
    }

    fun cubicTo(control1X: Float, control1Y: Float,
                control2X: Float, control2Y: Float,
                endX: Float, endY: Float) : Path
    {
        this.path.add(CubicElement(this.x, this.y,
                                   control1X, control1Y,
                                   control2X, control2Y,
                                   endX, endY))
        this.x = endX
        this.y = endY
        return this
    }

    fun ellipticArcTo(radiusX: Float, radiusY: Float, rotationAxisX: AngleFloat,
                      largeArc: Boolean, sweep: Boolean,
                      endX: Float, endY: Float) : Path
    {
        this.path.add(EllipticArcElement(this.x, this.y,
                                         radiusX, radiusY, rotationAxisX,
                                         largeArc, sweep,
                                         endX, endY))
        this.x = endX
        this.y = endY
        return this
    }

    fun add(path: Path) : Path
    {
        this.path.addAll(path.path)
        this.x = path.x
        this.y = path.y
        this.startX = path.startX
        this.startY = path.startY
        return this
    }

    fun path(precision: Int, start: Float, end: Float): List<Segment>
    {
        val precision = max(2, precision)
        val lines = ArrayList<Segment>()

        for (element in this.path)
        {
            element.appendSegments(lines, precision)
        }

        val distances = ArrayList<Float>()
        var size = 0f
        var distance: Float

        for (line in lines)
        {
            distance = distance(line.startX, line.startY, line.endX, line.endY)
            distances.add(distance)
            size += distance
        }

        if (size.nul)
        {
            return lines
        }

        var value = start
        val diff = end - start

        for ((index, line) in lines.withIndex())
        {
            line.startValue = value
            value += (distances[index] * diff) / size
            line.endValue = value
        }

        return lines
    }
}