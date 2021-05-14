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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.jhelp.engine.resources.Eyes
import fr.jhelp.engine.resources.Mouths
import fr.jhelp.engine.scene
import fr.jhelp.engine.scene.Scene3D
import fr.jhelp.engine.scene.geom.robot.Robot
import fr.jhelp.engine.scene.geom.robot.animation.headNoAnimation
import fr.jhelp.engine.scene.geom.robot.animation.headYesAnimation
import fr.jhelp.engine.scene.geom.robot.animation.run
import fr.jhelp.engine.scene.geom.robot.animation.stand
import fr.jhelp.engine.scene.geom.robot.animation.walk
import fr.jhelp.engine.view.View3D
import fr.jhelp.multitools.R
import fr.jhelp.utilities.random

class RobotActivity : Activity()
{
    private val robot by lazy { Robot() }
    private val stand by lazy { this.robot.stand(16) }
    private val walk by lazy { this.robot.walk(16, 16) }
    private val run by lazy { this.robot.run(8, 16) }
    private lateinit var scene: Scene3D

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_robot)
        val controls = this.findViewById<RecyclerView>(R.id.controls)
        controls.layoutManager = LinearLayoutManager(this)
        controls.adapter =
            RobotControlsAdapter(Pair(R.string.robotRun, this::doRun),
                                 Pair(R.string.robotWalk, this::doWalk),
                                 Pair(R.string.robotStop, this::doStop),
                                 Pair(R.string.robotYes,
                                      { this.scene.play(this.robot.headYesAnimation) }),
                                 Pair(R.string.robotNo,
                                      { this.scene.play(this.robot.headNoAnimation) }),
                                 Pair(R.string.robotEyes,
                                      {
                                          val eyes = random<Eyes>()
                                          this.robot.headTexture.leftEye = eyes
                                          this.robot.headTexture.rightEye = eyes
                                          this.robot.headTexture.refresh()
                                      }),
                                 Pair(R.string.robotFace,
                                      {
                                          this.robot.headTexture.mouth = random<Mouths>()
                                          this.robot.headTexture.refresh()
                                      }))
        this.findViewById<View3D>(R.id.view3D).scene(this::drawScene)
    }

    private fun doWalk()
    {
        this.scene.stop(this.run)
        this.scene.play(this.walk)
    }

    private fun doRun()
    {
        this.scene.stop(this.walk)
        this.scene.play(this.run)
    }

    private fun doStop()
    {
        this.scene.stop(this.run)
        this.scene.stop(this.walk)
        this.scene.play(this.stand)
    }

    private fun drawScene(scene: Scene3D)
    {
        this.scene = scene
        scene.root.add(this.robot.mainNode)
        scene.root.position.z = -6f
    }
}