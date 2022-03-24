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
import fr.jhelp.engine.scene.geom.Field3D
import fr.jhelp.engine.view.View3D
import fr.jhelp.multitools.R
import fr.jhelp.tasks.parallel
import fr.jhelp.utilities.formal.MathFunction
import fr.jhelp.utilities.formal.X
import fr.jhelp.utilities.formal.Y
import fr.jhelp.utilities.formal.cos
import fr.jhelp.utilities.formal.minus
import fr.jhelp.utilities.formal.plus
import fr.jhelp.utilities.formal.sin
import fr.jhelp.utilities.formal.times

class FieldActivity : Activity3D()
{
    private val mainNode = Node3D()
    private val fields = HashMap<MathFunction<*>, Field3D>()
    private val startFunction = X * X - Y * Y
    private val material = Material()

    override fun listAdapter(): RecyclerView.Adapter<*> =
        TextElementAdapter(this::functionSelected,
                           this.startFunction,
                           X * X + Y * Y,
                           cos(3 * X) - sin(3 * Y))

    override fun fill3D(view3D: View3D)
    {
        this.material.diffuse = WHITE
        this.material.texture = ResourcesAccess.obtainTexture(R.drawable.emerald_bk)
        val scene = view3D.scene3D
        scene.root.position.z = -3.1f
        scene.root.add(this.mainNode)
        this.mainNode.add(this.field3D(this.startFunction))
    }

    private fun functionSelected(mathFunction: MathFunction<*>)
    {
        {
            this.mainNode.removeAllChildren()
            this.mainNode.add(this.field3D(mathFunction))
        }.parallel()
    }

    private fun field3D(mathFunction: MathFunction<*>): Field3D =
        this.fields.getOrPut(mathFunction)
        {
            val field = Field3D(mathFunction,
                                -1f, 1f, 10,
                                -1f, 1f, 10)
            field.material = this.material
            return field
        }
}