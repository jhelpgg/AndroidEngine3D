/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.graphics.progress.circle

import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.SweepGradient
import android.graphics.Typeface
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import androidx.annotation.ColorInt
import fr.jhelp.animations.interpoolation.SinusInterpolation
import fr.jhelp.animations.values.AnimatedInt
import fr.jhelp.graphics.R
import fr.jhelp.graphics.view.GraphicView
import fr.jhelp.images.ALPHA_MASK
import fr.jhelp.images.blue
import fr.jhelp.images.green
import fr.jhelp.images.red
import fr.jhelp.utilities.bounds
import kotlin.math.min

class ProgressCircleView(context: Context, attributes: AttributeSet?, defaultStyleAttributes: Int,
                         defaultStyleResources: Int) :
    GraphicView(context, attributes, defaultStyleAttributes, defaultStyleResources)
{
    private val percentDraw = AnimatedInt()
    var percent: Int
        private set
    var borderColor: Int
        private set
    var startColor: Int
        private set
    var endColor: Int
        private set
    var textColor: Int
        private set
    var shape: ProgressCircleShape
        private set

    constructor(context: Context, attributes: AttributeSet?, defaultStyleAttributes: Int) :
            this(context, attributes, defaultStyleAttributes, 0)

    constructor(context: Context, attributes: AttributeSet?) :
            this(context, attributes, 0, 0)

    constructor(context: Context) :
            this(context, null, 0, 0)

    private val paintText = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val paintBorder = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintFill = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintShadow = Paint(Paint.ANTI_ALIAS_FLAG)

    private val textBounds = Rect()
    private var xStart = 0f
    private var yStart = 0f
    private var xCenter = 0f
    private var yCenter = 0f
    private var innerWidth = 0f
    private var innerHeight = 0f
    private val pathBorder = Path()
    private val pathFilled = Path()
    private var text = ""

    init
    {
        val typeArray =
            context.theme.obtainStyledAttributes(attributes, R.styleable.ProgressCircleView, 0,
                                                 R.style.DefaultProgressCircleView)
        this.percent = typeArray.getInt(R.styleable.ProgressCircleView_percent, 0).bounds(0, 100)
        this.borderColor = typeArray.getColor(R.styleable.ProgressCircleView_borderColor, 0)
        this.startColor = typeArray.getColor(R.styleable.ProgressCircleView_startColor, 0)
        this.endColor = typeArray.getColor(R.styleable.ProgressCircleView_endColor, 0)
        this.textColor = typeArray.getColor(R.styleable.ProgressCircleView_textColor, 0)
        this.setAnimated(typeArray.getBoolean(R.styleable.ProgressCircleView_animated, true))
        this.shape = typeArray.getInt(R.styleable.ProgressCircleView_shape, 0).progressCircleShape

        this.paintText.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
        this.paintText.style = Paint.Style.FILL
        this.paintText.color = this.textColor

        this.paintBorder.style = Paint.Style.STROKE
        this.paintBorder.strokeWidth = 8f
        this.paintBorder.color = this.borderColor

        this.paintFill.style = Paint.Style.FILL
        this.paintFill.shader = SweepGradient(0f, 0f, this.startColor, this.endColor)

        this.paintShadow.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
        this.paintShadow.style = Paint.Style.FILL
        this.paintShadow.color = 0x99101010.toInt()
        this.paintShadow.maskFilter = BlurMaskFilter(8f, BlurMaskFilter.Blur.NORMAL)
        this.updatePercents()

        typeArray.recycle()
    }

    fun setPercent(percent: Int)
    {
        val value = percent.bounds(0, 100)

        if (this.percent != value)
        {
            this.percent = value
            this.updatePercents()
            this.refreshView()
        }
    }

    private fun updatePercents()
    {
        if (this.animated)
        {
            this.percentDraw.setValueIn(this.percent, 4096, SinusInterpolation)
        }
        else
        {
            this.percentDraw.setValue(this.percent)
        }
    }

    fun setBorderColor(@ColorInt color: Int)
    {
        if (this.borderColor != color)
        {
            this.borderColor = color
            this.paintBorder.color = color
            this.postInvalidate()
        }
    }

    fun setStartColor(@ColorInt color: Int)
    {
        if (this.startColor != color)
        {
            this.startColor = color
            this.updateFillColors()
            this.postInvalidate()
        }
    }

    fun setEndColor(@ColorInt color: Int)
    {
        if (this.endColor != color)
        {
            this.endColor = color
            this.updateFillColors()
            this.postInvalidate()
        }
    }

    fun setTextColor(@ColorInt color: Int)
    {
        if (this.textColor != color)
        {
            this.textColor = color
            this.paintText.color = color
            this.postInvalidate()
        }
    }

    fun setShape(shape: ProgressCircleShape)
    {
        if (this.shape != shape)
        {
            this.shape = shape
            this.updatePaths()
            this.updateFillColors()
            this.postInvalidate()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int)
    {
        this.xStart = this.paddingLeft.toFloat()
        var xEnd = w - this.paddingRight.toFloat()
        this.yStart = this.paddingTop.toFloat()
        var yEnd = h - this.paddingBottom.toFloat()
        this.innerWidth = xEnd - this.xStart
        this.innerHeight = yEnd - this.yStart

        var percent = this.innerWidth / 100f
        this.xStart += percent / 2f
        xEnd -= percent / 2f
        this.innerWidth -= percent

        percent = this.innerHeight / 100f
        this.yStart += percent / 2f
        yEnd -= percent / 2f
        this.innerHeight -= percent

        this.xCenter = (this.xStart + xEnd) / 2f
        this.yCenter = (this.yStart + yEnd) / 2f

        val maxTextWidth = this.innerWidth / 2f
        val maxTextHeight = this.innerHeight / 2f
        var size = min(maxTextWidth, maxTextHeight)

        do
        {
            this.paintText.textSize = size
            this.paintText.getTextBounds("888%", 0, 4, this.textBounds)
            Log.d("###", size.toString())
            size -= 1f
        }
        while (size > 16f && (this.textBounds.width() > maxTextWidth || this.textBounds.height() > maxTextHeight))

        this.paintShadow.textSize = this.paintText.textSize
        this.updateFillColors()
        this.updatePaths()
        this.updateText()
        super.onSizeChanged(w, h, oldw, oldh)
    }

    private fun updateFillColors()
    {
        val percentToDraw = this.percentDraw.value

        when (this.shape)
        {
            ProgressCircleShape.FULL_CIRCLE, ProgressCircleShape.RING ->
            {
                if (percentToDraw <= 50)
                {
                    this.paintFill.shader =
                        SweepGradient(this.xCenter, this.yCenter,
                                      intArrayOf(this.startColor,
                                                 this.endColor),
                                      floatArrayOf(0.5f, 0.5f + percentToDraw / 100f))
                }
                else
                {
                    val percent = 50f / percentToDraw
                    val antiPercent = 1f - percent
                    val red =
                        (this.startColor.red * antiPercent + this.endColor.red * percent).toInt()
                    val green =
                        (this.startColor.green * antiPercent + this.endColor.green * percent).toInt()
                    val blue =
                        (this.startColor.blue * antiPercent + this.endColor.blue * percent).toInt()
                    val middleColor = ALPHA_MASK or (red shl 16) or (green shl 8) or blue
                    this.paintFill.shader =
                        SweepGradient(this.xCenter, this.yCenter,
                                      intArrayOf(middleColor,
                                                 this.endColor,
                                                 this.startColor,
                                                 middleColor),
                                      floatArrayOf(0f, percentToDraw / 100f - 0.5f, 0.5f, 1f))

                }
            }
            ProgressCircleShape.HALF_RING ->
            {
                this.paintFill.shader =
                    SweepGradient(this.xCenter, this.yStart + this.innerHeight,
                                  intArrayOf(this.startColor,
                                             this.endColor),
                                  floatArrayOf(0.5f, 0.5f + (percentToDraw / 100f) * 0.5f))
            }
        }
    }

    private fun updatePaths()
    {
        this.pathBorder.reset()
        this.pathFilled.reset()

        when (this.shape)
        {
            ProgressCircleShape.FULL_CIRCLE -> this.fullCirclePath()
            ProgressCircleShape.RING -> this.ringPath()
            ProgressCircleShape.HALF_RING -> this.halfRingPath()
        }
    }

    private fun updateText()
    {
        this.text = "${this.percentDraw.value}%"
        this.paintText.getTextBounds(this.text, 0, this.text.length, this.textBounds)
    }

    private fun fullCirclePath()
    {
        val percentToDraw = this.percentDraw.value

        this.pathBorder.addOval(this.xStart, this.yStart,
                                this.xStart + this.innerWidth, this.yStart + this.innerHeight,
                                Path.Direction.CW)

        if (percentToDraw == 100)
        {
            this.pathFilled.set(this.pathBorder)
        }
        else if (percentToDraw > 0)
        {
            this.pathFilled.moveTo(this.xCenter, this.yCenter)
            this.pathFilled.arcTo(this.xStart, this.yStart,
                                  this.xStart + this.innerWidth, this.yStart + this.innerHeight,
                                  180f, (360f * percentToDraw) / 100f,
                                  false)
            this.pathFilled.close()
        }
    }

    private fun ringPath()
    {
        val ringThin = min(this.innerWidth / 5f, this.innerHeight / 5f)
        this.pathBorder.addOval(this.xStart,
                                this.yStart,
                                this.xStart + this.innerWidth,
                                this.yStart + this.innerHeight,
                                Path.Direction.CW)
        this.pathBorder.addOval(this.xStart + ringThin,
                                this.yStart + ringThin,
                                this.xStart + this.innerWidth - ringThin,
                                this.yStart + this.innerHeight - ringThin,
                                Path.Direction.CCW)

        val percentToDraw = this.percentDraw.value

        if (percentToDraw == 100)
        {
            this.pathFilled.set(this.pathBorder)
        }
        else if (percentToDraw > 0)
        {
            val sweepAngle = (360f * percentToDraw) / 100f
            this.pathFilled.arcTo(this.xStart,
                                  this.yStart,
                                  this.xStart + this.innerWidth,
                                  this.yStart + this.innerHeight,
                                  180f, sweepAngle,
                                  false)
            this.pathFilled.arcTo(this.xStart + ringThin,
                                  this.yStart + ringThin,
                                  this.xStart + this.innerWidth - ringThin,
                                  this.yStart + this.innerHeight - ringThin,
                                  180f + sweepAngle, -sweepAngle,
                                  false)
            this.pathFilled.close()
        }
    }

    private fun halfRingPath()
    {
        val ringThin = min(this.innerWidth / 5f, this.innerHeight / 5f)

        this.pathBorder.arcTo(this.xStart,
                              this.yStart,
                              this.xStart + this.innerWidth,
                              this.yStart + this.innerHeight * 2f,
                              180f, 180f,
                              false)
        this.pathBorder.arcTo(this.xStart + ringThin,
                              this.yStart + ringThin,
                              this.xStart + this.innerWidth - ringThin,
                              this.yStart + this.innerHeight * 2f - ringThin,
                              0f, -180f,
                              false)
        this.pathBorder.close()

        val percentToDraw = this.percentDraw.value
        if (percentToDraw == 100)
        {
            this.pathFilled.set(this.pathBorder)
        }
        else if (percentToDraw > 0)
        {
            val sweepAngle = (180f * percentToDraw) / 100f
            this.pathFilled.arcTo(this.xStart,
                                  this.yStart,
                                  this.xStart + this.innerWidth,
                                  this.yStart + this.innerHeight * 2,
                                  180f, sweepAngle,
                                  false)
            this.pathFilled.arcTo(this.xStart + ringThin,
                                  this.yStart + ringThin,
                                  this.xStart + this.innerWidth - ringThin,
                                  this.yStart + this.innerHeight * 2 - ringThin,
                                  180f + sweepAngle, -sweepAngle,
                                  false)
            this.pathFilled.close()
        }
    }

    override fun updateValues(): Boolean
    {
        val animating = this.percentDraw.animating
        this.updateText()
        this.updatePaths()
        this.updateFillColors()
        return animating
    }

    override fun animationStarted()
    {
        this.percentDraw.start()
    }

    override fun animationStopped()
    {
        this.percentDraw.setValue(this.percent)
    }

    override fun drawing(canvas: Canvas)
    {
        canvas.translate(8f, 8f)
        canvas.drawPath(this.pathFilled, this.paintShadow)
        canvas.translate(-8f, -8f)
        canvas.drawPath(this.pathFilled, this.paintFill)
        canvas.drawPath(this.pathBorder, this.paintBorder)
        val textX = this.xCenter - this.textBounds.centerX()
        val textY = this.yCenter - this.textBounds.centerY()
        canvas.translate(8f, 8f)
        canvas.drawText(this.text, textX, textY, this.paintShadow)
        canvas.translate(-8f, -8f)
        canvas.drawText(this.text, textX, textY, this.paintText)
    }
}