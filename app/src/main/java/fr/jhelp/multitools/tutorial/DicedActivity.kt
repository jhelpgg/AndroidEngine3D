/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.multitools.tutorial

import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import fr.jhelp.engine.draw
import fr.jhelp.engine.scene
import fr.jhelp.engine.scene.GREEN
import fr.jhelp.engine.scene.Node3D
import fr.jhelp.engine.scene.Scene3D
import fr.jhelp.engine.scene.WHITE
import fr.jhelp.engine.scene.geom.Plane
import fr.jhelp.engine.scene.geom.dice.Dice
import fr.jhelp.engine.scene.texture
import fr.jhelp.engine.view.View3D
import fr.jhelp.images.COLOR_ALPHA_LOWER
import fr.jhelp.images.COLOR_AMBER_0200
import fr.jhelp.images.COLOR_BLACK
import fr.jhelp.images.clear
import fr.jhelp.images.useAlpha
import fr.jhelp.multitools.R
import fr.jhelp.utilities.log
import kotlinx.android.synthetic.main.activity_main.view3D

/**
 * Hello world tutorial
 */
class DicedActivity : Activity()
{
    private val dice by lazy { Dice() }
    private lateinit var scene3D: Scene3D
    private val valueTexture by lazy { texture(64, 64) }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main)
        // Get the 3D view and draw it in independent thread to free the UI thread
        val view3D = this.findViewById<View3D>(R.id.view3D)
        view3D.scene(this::drawScene)
        view3D.setOnClickListener { this.scene3D.play(this.dice.roll()) }
    }

    private fun updateValue(value: Int)
    {
        this.valueTexture.draw { bitmap, canvas, paint ->
            bitmap.clear(0)
            paint.color = COLOR_BLACK
            paint.typeface = Typeface.MONOSPACE
            paint.textSize = 32f
            canvas.drawText(value.toString(), 8f, 32f, paint)
        }
    }

    /**
     * Draw scene on 3D view
     */
    private fun drawScene(scene3D: Scene3D)
    {
        this.scene3D = scene3D
        this.dice.position.z = -3f
        this.dice.position.scale(1.25f)
        this.view3D.manipulateNode = Node3D()
        this.dice.color(GREEN)
        this.scene3D.root.add(this.dice)
        val plane = Plane()
        plane.position.x =  -1.75f
        plane.position.y = 1f

            plane.position.z = -2f

        plane.material.diffuse = WHITE
        plane.material.texture = this.valueTexture
        this.scene3D.root.add(plane)
        this.dice.observe { diceInfo -> this.updateValue(diceInfo.diceValue) }
    }
}