/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.multitools.engine

import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import fr.jhelp.engine.resources.ResourcesAccess
import fr.jhelp.engine.scene.WHITE
import fr.jhelp.engine.scene.geom.Plane
import fr.jhelp.engine.view.View3D
import fr.jhelp.multitools.R
import fr.jhelp.tasks.parallel

class PlaneActivity : Activity3D()
{
    private val plane = Plane()

    override fun listAdapter(): RecyclerView.Adapter<*> =
        ImageElementAdapter(this::imageClicked,
                            R.drawable.body_costume, R.drawable.default_screen,
                            R.drawable.floor, R.drawable.emerald_bk)

    private fun imageClicked(@DrawableRes image: Int)
    {
        { imageID: Int ->
            this.plane.material.texture = ResourcesAccess.obtainTexture(imageID)
        }.parallel(image)
    }

    override fun fill3D(view3D: View3D)
    {
        val scene = view3D.scene3D
        scene.root.position.z = -2f
        this.plane.material.texture = ResourcesAccess.obtainTexture(R.drawable.floor)
        this.plane.material.diffuse = WHITE
        this.plane.position.scale(1.5f)
        scene.root.add(this.plane)
    }
}