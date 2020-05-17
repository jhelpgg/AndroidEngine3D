package fr.jhelp.multitools

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fr.jhelp.engine.material
import fr.jhelp.engine.resources.ResourcesAccess
import fr.jhelp.engine.scene.Color3D
import fr.jhelp.engine.scene.GREEN
import fr.jhelp.engine.scene.RED
import fr.jhelp.engine.scene.geom.Box
import fr.jhelp.engine.scene.geom.Field3D
import fr.jhelp.engine.scene.geom.Sphere
import fr.jhelp.engine.view.View3D
import fr.jhelp.tasks.parallel
import fr.jhelp.utilities.formal.X
import fr.jhelp.utilities.formal.Y
import fr.jhelp.utilities.formal.minus
import fr.jhelp.utilities.formal.times

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        parallel(21, this.findViewById<View3D>(R.id.view3D), GREEN, this::createScene)
    }

    private fun createScene(id: Int, view3D: View3D, color3D: Color3D)
    {
        val scene = view3D.scene3D
        scene.root.position.z = -3f
        val field = Field3D(X * X - Y * Y,
                            -1f, 1f, 10,
                            -1f, 1f, 10)
        val box = Box()
        box.material.diffuse = GREEN
        box.position.setScale(0.25f)
        field.add(box)
        val sphere = Sphere()
        sphere.material {
            alpha = 0.666f
            diffuse = RED
        }
        sphere.position.z = 1f
        box.add(sphere)
        scene.root.add(field)
        field.material.texture = ResourcesAccess.obtainTexture(R.drawable.body_costume)
    }
}
