/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.multitools.tutorial

import android.app.Activity
import android.os.Bundle
import fr.jhelp.engine.scene
import fr.jhelp.engine.scene.BLUE_GREY
import fr.jhelp.engine.scene.Scene3D
import fr.jhelp.engine.scene.geom.Box
import fr.jhelp.engine.view.View3D
import fr.jhelp.multitools.R

/**
 * Hello world tutorial
 */
class DiffuseMaterialActivity : Activity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main)
        // Get the 3D view and draw draw 3D scene on it
        this.findViewById<View3D>(R.id.view3D).scene(this::drawScene)
    }

    /**
     * Draw scene on 3D view
     * @param scene3D Scene where add 3D objects
     */
    private fun drawScene(scene3D: Scene3D)
    {
        // Position the root node in front of the camera to able see the scene
        scene3D.root.position.z = -2f
        // Create a box
        val box = Box()
        // Add box to the scene
        scene3D.root.add(box)

        // Change material diffuse
        box.material.diffuse = BLUE_GREY
    }
}