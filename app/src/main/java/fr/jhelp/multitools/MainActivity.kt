/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.multitools

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fr.jhelp.engine.position
import fr.jhelp.engine.scene.Clone3D
import fr.jhelp.engine.scene.Color3D
import fr.jhelp.engine.scene.GREEN
import fr.jhelp.engine.scene.LIGHT_GREEN
import fr.jhelp.engine.scene.Material
import fr.jhelp.engine.scene.Node3D
import fr.jhelp.engine.scene.RED
import fr.jhelp.engine.scene.geom.Plane
import fr.jhelp.engine.scene.geom.Revolution
import fr.jhelp.engine.view.View3D
import fr.jhelp.graphics.progress.circle.ProgressCircleView
import fr.jhelp.images.path.Path
import fr.jhelp.models.objects.Horse
import fr.jhelp.sound.SoundManager
import fr.jhelp.tasks.delay
import fr.jhelp.tasks.parallel
import fr.jhelp.utilities.PI_FLOAT
import fr.jhelp.utilities.random
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

private const val thetaStep = PI_FLOAT / 10f
private const val gamStep = 2f * PI_FLOAT / 10f

class MainActivity : AppCompatActivity()
{
    private lateinit var testPercent: ProgressCircleView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)//activity_test)
        this::showHorse.parallel(this.findViewById(R.id.view3D))
        //this::createScene.parallel(this.findViewById(R.id.view3D))
    }

    private fun showHorse(view3D: View3D)
    {
        val scene = view3D.scene3D
        scene.root.position.z = -2f
        val horse = Horse()
        val material = Material()
        material.diffuse = GREEN
        horse.node.applyMaterialHierarchically(material)
        scene.root.add(horse.node)

        ({ scene.play(horse.runAnimation()) }).delay(4096)
    }

    private fun createScene(view3D: View3D)
    {
        val scene = view3D.scene3D
        scene.root.position.z = -3f
        val node = Node3D()
        scene.root.add(node)
        val reference = Plane().position { scale(0.1f) }
        node.add(reference)
        val moveNodes = ArrayList<Node3D>()
        moveNodes.add(reference)

        val path = Path()
        path.moveTo(0f, 1f)
        path.lineTo(0.25f, 0.8f)
        path.lineTo(0.25f, -0.8f)
        path.lineTo(0f, -1f)
        val cristal = Revolution(path, rotationPrecision = 5)
        cristal.showWire()
        cristal.material.diffuse = LIGHT_GREEN
        cristal.material.alpha = 0.7f
        scene.root.add(cristal)

        for (index in 0 until 99)
        {
            val clone = Clone3D(reference).position { scale(0.1f) }
            node.add(clone)
            moveNodes.add(clone)
        }

        val material = Material()
        material.diffuse = RED
        node.applyMaterialHierarchically(material)

        scene.play { frame ->
            val ray = min(2f, frame / 250f)
            var theta = thetaStep / 2f
            var gama = 0f
            material.diffuse = Color3D(ray / 2f, 1f - (ray / 2f), 1f - (ray / 2f))

            for ((index, moved) in moveNodes.withIndex())
            {
                moved.position.x = ray * sin(theta) * cos(gama)
                moved.position.y = ray * sin(theta) * sin(gama)
                moved.position.z = ray * cos(theta)
                moved.position.angleX += random(1f, 15f)
                moved.position.angleY += random(1f, 15f)
                moved.position.angleZ += random(1f, 15f)
                gama += gamStep

                if (index % 10 == 9)
                {
                    gama = 0f
                    theta += thetaStep
                }
            }

            true
        }
    }

    override fun onPause()
    {
        SoundManager.pause()
        super.onPause()
    }

    override fun onResume()
    {
        super.onResume()
        SoundManager.resume()
    }

    override fun onDestroy()
    {
        SoundManager.stopSounds()
        super.onDestroy()
    }
}
