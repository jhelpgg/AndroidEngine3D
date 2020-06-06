/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.multitools.engine

import androidx.recyclerview.widget.RecyclerView
import fr.jhelp.engine.resources.ResourcesAccess
import fr.jhelp.engine.scene.Material
import fr.jhelp.engine.scene.Node3D
import fr.jhelp.engine.scene.WHITE
import fr.jhelp.engine.scene.geom.Revolution
import fr.jhelp.engine.view.View3D
import fr.jhelp.images.path.Path
import fr.jhelp.multitools.R

class RevolutionActivity : Activity3D()
{
    private val mainNode = Node3D()
    private val revolution3 = this.revolution(3)
    private val revolution5 = this.revolution(5)
    private val revolution9 = this.revolution(9)
    private val revolution12 = this.revolution(12)

    override fun listAdapter(): RecyclerView.Adapter<*> =
        TextElementAdapter(this::changeRevolution,
                           RevolutionInformation(3, this.revolution3),
                           RevolutionInformation(5, this.revolution5),
                           RevolutionInformation(9, this.revolution9),
                           RevolutionInformation(12, this.revolution12))

    override fun fill3D(view3D: View3D)
    {
        val scene = view3D.scene3D
        scene.root.position.z = -2.2f
        scene.root.add(this.mainNode)
        this.mainNode.add(this.revolution9)
        val material = Material()
        material.diffuse = WHITE
        material.texture = ResourcesAccess.obtainTexture(R.drawable.emerald_bk)
        this.revolution3.material = material
        this.revolution5.material = material
        this.revolution9.material = material
        this.revolution12.material = material
    }

    private fun changeRevolution(revolutionInformation: RevolutionInformation)
    {
        this.mainNode.removeAllChildren()
        this.mainNode.add(revolutionInformation.revolution)
    }

    private fun revolution(precision: Int): Revolution
    {
        val path = Path()
        path.moveTo(0.25f, 1f)
        path.lineTo(0.25f, 0f)
        path.quadraticTo(0.5f, 0f, 0.5f, -0.25f)
        path.lineTo(0.5f, -0.5f)
        path.lineTo(0f, -0.5f)
        val revolution = Revolution(path, pathPrecision = 10, rotationPrecision = precision)
        revolution.doubleFace = true
        return revolution
    }
}