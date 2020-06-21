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
import fr.jhelp.engine.resources.ResourcesAccess
import fr.jhelp.engine.scene.LIGHT_RED
import fr.jhelp.engine.scene.geom.Box
import fr.jhelp.engine.view.View3D
import fr.jhelp.multitools.R
import fr.jhelp.tasks.parallel

/**
 * Hello world tutorial
 */
class TextureAndDiffuseMaterialActivity : Activity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main)
        // Get the 3D view and draw it in independent thread to free the UI thread
        parallel(this.findViewById(R.id.view3D), this::drawScene)
    }

    /**
     * Draw scene on 3D view
     */
    private fun drawScene(view3D: View3D)
    {
        // Get scene in 3D view
        val scene3D = view3D.scene3D
        // Position the root node in front of the camera to able see the scene
        scene3D.root.position.z = -2f
        // Create a box
        val box = Box()
        // Add box to the scene
        scene3D.root.add(box)

        // Change material diffuse and texture
        box.material.diffuse = LIGHT_RED
        box.material.texture = ResourcesAccess.obtainTexture(R.drawable.default_screen)
    }
}