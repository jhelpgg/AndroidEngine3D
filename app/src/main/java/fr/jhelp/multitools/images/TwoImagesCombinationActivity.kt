/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.multitools.images

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import fr.jhelp.multitools.R
import fr.jhelp.tasks.MainThread
import fr.jhelp.tasks.launch
import fr.jhelp.utilities.logError

abstract class TwoImagesCombinationActivity(@StringRes private val title: Int) : Activity(),
    View.OnClickListener
{
    private lateinit var chooseImageTable: TableLayout
    private lateinit var resultImage: ImageView
    private lateinit var firstImage: ImageView
    private lateinit var secondImage: ImageView
    private lateinit var choiceImage1: ImageView
    private lateinit var choiceImage2: ImageView
    private lateinit var choiceImage3: ImageView
    private lateinit var choiceImage4: ImageView
    private var firstImageChange = true
    private var currentFirst = 0
    private var currentSecond = 0

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_two_images_combination)
        this.findViewById<TextView>(R.id.title).setText(this.title)

        this.chooseImageTable = this.findViewById(R.id.chooseImageTable)
        this.resultImage = this.findViewById(R.id.resultImage)
        this.firstImage = this.findViewById(R.id.firstImage)
        this.secondImage = this.findViewById(R.id.secondImage)
        this.choiceImage1 = this.findViewById(R.id.image_1_1)
        this.choiceImage2 = this.findViewById(R.id.image_2_1)
        this.choiceImage3 = this.findViewById(R.id.image_1_2)
        this.choiceImage4 = this.findViewById(R.id.image_2_2)

        this.firstImage.setOnClickListener(this)
        this.secondImage.setOnClickListener(this)
        this.choiceImage1.setOnClickListener(this)
        this.choiceImage2.setOnClickListener(this)
        this.choiceImage3.setOnClickListener(this)
        this.choiceImage4.setOnClickListener(this)

        this.firstImage.setImageBitmap(this.firstImageChoice(this.currentFirst))
        this.secondImage.setImageBitmap(this.secondImageChoice(this.currentSecond))
        this.updateResult()
    }

    abstract fun firstImageChoice(index: Int): Bitmap

    abstract fun secondImageChoice(index: Int): Bitmap

    abstract fun doOperation(first: Bitmap, second: Bitmap): Bitmap

    fun obtainBitmapFromResources(@DrawableRes resourceId: Int): Bitmap
    {
        val options = BitmapFactory.Options()
        options.inScaled = false
        val bitmap = BitmapFactory.decodeResource(this.resources, resourceId, options)
        return Bitmap.createScaledBitmap(bitmap, 512, 512, false)
    }

    fun createBitmap(drawer: (Bitmap, Canvas, Paint) -> Unit): Bitmap
    {
        val bitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        drawer(bitmap, canvas, paint)
        return bitmap
    }

    override fun onClick(view: View)
    {
        when (view.id)
        {
            R.id.firstImage  ->
            {
                this.firstImageChange = true
                this.showDialog(this::firstImageChoice)
            }
            R.id.secondImage ->
            {
                this.firstImageChange = false
                this.showDialog(this::secondImageChoice)
            }
            R.id.image_1_1   ->
            {
                if (this.firstImageChange)
                {
                    this.currentFirst = 0
                    this.firstImage.setImageBitmap(this.firstImageChoice(0))
                }
                else
                {
                    this.currentSecond = 0
                    this.secondImage.setImageBitmap(this.secondImageChoice(0))
                }

                this.hideDialog()
            }
            R.id.image_2_1   ->
            {
                if (this.firstImageChange)
                {
                    this.currentFirst = 1
                    this.firstImage.setImageBitmap(this.firstImageChoice(1))
                }
                else
                {
                    this.currentSecond = 1
                    this.secondImage.setImageBitmap(this.secondImageChoice(1))
                }

                this.hideDialog()
            }
            R.id.image_1_2   ->
            {
                if (this.firstImageChange)
                {
                    this.currentFirst = 2
                    this.firstImage.setImageBitmap(this.firstImageChoice(2))
                }
                else
                {
                    this.currentSecond = 2
                    this.secondImage.setImageBitmap(this.secondImageChoice(2))
                }

                this.hideDialog()
            }
            R.id.image_2_2   ->
            {
                if (this.firstImageChange)
                {
                    this.currentFirst = 3
                    this.firstImage.setImageBitmap(this.firstImageChoice(3))
                }
                else
                {
                    this.currentSecond = 3
                    this.secondImage.setImageBitmap(this.secondImageChoice(3))
                }

                this.hideDialog()
            }
        }
    }

    private fun showDialog(chooseList: (Int) -> Bitmap)
    {
        this.firstImage.setOnClickListener(null)
        this.secondImage.setOnClickListener(null)

        this.choiceImage1.setImageBitmap(chooseList(0))
        this.choiceImage2.setImageBitmap(chooseList(1))
        this.choiceImage3.setImageBitmap(chooseList(2))
        this.choiceImage4.setImageBitmap(chooseList(3))

        this.chooseImageTable.visibility = View.VISIBLE
    }

    private fun hideDialog()
    {
        this.chooseImageTable.visibility = View.GONE
        this.firstImage.setOnClickListener(this)
        this.secondImage.setOnClickListener(this)
        this.updateResult()
    }

    private fun updateResult()
    {
        launch {
            this.doOperation(this.firstImageChoice(this.currentFirst),
                             this.secondImageChoice(this.currentSecond))
        }
            .and(MainThread) { result -> this.resultImage.setImageBitmap(result) }
            .onError { logError(it) { "Oups" } }
    }
}