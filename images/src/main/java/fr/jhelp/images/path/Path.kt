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
    private var moved = false

    fun copy(): Path
    {
        val copy = Path()
        copy.path.addAll(this.path)
        copy.x = this.x
        copy.y = this.y
        copy.startX = this.startX
        copy.startY = this.startY
        copy.moved = this.moved
        return copy
    }

    fun moveTo(startX: Float, startY: Float)
    {
        this.startX = startX
        this.startY = startY
        this.x = startX
        this.y = startY
        this.moved = true
    }

    fun close()
    {
        if (this.moved && (!this.startX.same(this.x) || !this.startY.same(this.y)))
        {
            this.line(this.x, this.y, this.startX, this.startY)
            this.x = this.startX
            this.y = this.startY
        }
    }

    fun lineTo(endX: Float, endY: Float)
    {
        this.line(this.x, this.y, endX, endY)
    }

    fun line(startX: Float, startY: Float,
             endX: Float, endY: Float)
    {
        this.path.add(
            LineElement(startX, startY, endX, endY))
        this.x = endX
        this.y = endY
    }

    fun quadraticTo(controlX: Float, controlY: Float,
                    endX: Float, endY: Float)
    {
        this.quadratic(this.x, this.y, controlX, controlY, endX, endY)
    }

    fun quadratic(startX: Float, startY: Float,
                  controlX: Float, controlY: Float,
                  endX: Float, endY: Float)
    {
        this.path.add(
            QuadraticElement(startX, startY, controlX,
                             controlY, endX, endY))
        this.x = endX
        this.y = endY
    }

    fun cubicTo(control1X: Float, control1Y: Float,
                control2X: Float, control2Y: Float,
                endX: Float, endY: Float)
    {
        this.cubic(this.x, this.y, control1X, control1Y, control2X, control2Y, endX, endY)
    }

    fun cubic(startX: Float, startY: Float,
              control1X: Float, control1Y: Float,
              control2X: Float, control2Y: Float,
              endX: Float, endY: Float)
    {
        this.path.add(
            CubicElement(startX, startY, control1X,
                         control1Y, control2X, control2Y,
                         endX, endY))
        this.x = endX
        this.y = endY
    }

    fun ellipticArcTo(radiusX: Float, radiusY: Float, rotationAxisX: AngleFloat,
                      largeArc: Boolean, sweep: Boolean,
                      endX: Float, endY: Float)
    {
        this.ellipticArc(this.x, this.y,
                         radiusX, radiusY, rotationAxisX,
                         largeArc, sweep,
                         endX, endY)
    }

    fun ellipticArc(startX: Float, startY: Float,
                    radiusX: Float, radiusY: Float, rotationAxisX: AngleFloat,
                    largeArc: Boolean, sweep: Boolean,
                    endX: Float, endY: Float)
    {
        this.path.add(
            EllipticArcElement(startX, startY,
                               radiusX, radiusY, rotationAxisX,
                               largeArc, sweep,
                               endX, endY))
        this.x = endX
        this.y = endY
    }

    fun add(path:Path)
    {
        this.path.addAll(path.path)
        this.x = path.x
        this.y = path.y

        if(path.moved)
        {
            this.startX = path.startX
            this.startY = path.startY
        }
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