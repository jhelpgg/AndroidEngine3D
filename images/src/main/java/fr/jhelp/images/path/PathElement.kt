package fr.jhelp.images.path

import fr.jhelp.images.Point2D
import fr.jhelp.images.crawler.EllipticArcCrawler
import fr.jhelp.utilities.angle.AngleFloat
import fr.jhelp.utilities.cubic
import fr.jhelp.utilities.quadratic

internal sealed class PathElement
{
    abstract fun appendSegments(segments: MutableList<Segment>, precision: Int)
}

internal class LineElement(private val startX: Float, private val startY: Float,
                           private val endX: Float, private val endY: Float) : PathElement()
{
    override fun appendSegments(segments: MutableList<Segment>, precision: Int)
    {
        segments.add(Segment(this.startX, this.startY, 0f,
                             this.endX, this.endY, 1f))
    }
}

internal class QuadraticElement(private val startX: Float, private val startY: Float,
                                private val controlX: Float, private val controlY: Float,
                                private val endX: Float, private val endY: Float) : PathElement()
{
    override fun appendSegments(segments: MutableList<Segment>, precision: Int)
    {
        val xs = quadratic(this.startX, this.controlX, this.endX, precision)
        val ys = quadratic(this.startY, this.controlY, this.endY, precision)

        for (index in 1 until precision)
        {
            segments.add(
                Segment(xs[index - 1], ys[index - 1], 0f,
                        xs[index], ys[index], 1f))
        }
    }
}

internal class CubicElement(private val startX: Float, private val startY: Float,
                            private val control1X: Float, private val control1Y: Float,
                            private val control2X: Float, private val control2Y: Float,
                            private val endX: Float, private val endY: Float) : PathElement()
{
    override fun appendSegments(segments: MutableList<Segment>, precision: Int)
    {
        val xs = cubic(this.startX, this.control1X, this.control2X, this.endX, precision)
        val ys = cubic(this.startY, this.control1Y, this.control2Y, this.endY, precision)

        for (index in 1 until precision)
        {
            segments.add(
                Segment(xs[index - 1], ys[index - 1], 0f,
                        xs[index], ys[index], 1f))
        }
    }
}

internal class EllipticArcElement(private val startX: Float, private val startY: Float,
                                  private val radiusX: Float, private val radiusY: Float,
                                  private val rotationAxisX: AngleFloat,
                                  private val largeArc: Boolean, private val sweep: Boolean,
                                  private val endX: Float, private val endY: Float) : PathElement()
{
    override fun appendSegments(segments: MutableList<Segment>, precision: Int)
    {
        val ellipticArcToCrawler =
            EllipticArcCrawler(this.startX, this.startY,
                               this.radiusX,
                               this.radiusY,
                               this.rotationAxisX,
                               this.largeArc, this.sweep,
                               this.endX, this.endY,
                               precision)
        var first = ellipticArcToCrawler[0]
        var second: Point2D

        for (index in 1 until ellipticArcToCrawler.numberStep)
        {
            second = ellipticArcToCrawler[index]
            segments.add(Segment(first, 0f, second, 1f))
            first = second
        }
    }
}

