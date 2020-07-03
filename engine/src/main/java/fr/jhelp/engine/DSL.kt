/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.engine

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import fr.jhelp.engine.scene.Clone3D
import fr.jhelp.engine.scene.Material
import fr.jhelp.engine.scene.Node3D
import fr.jhelp.engine.scene.Object3D
import fr.jhelp.engine.scene.Position3D
import fr.jhelp.engine.scene.Scene3D
import fr.jhelp.engine.scene.Texture
import fr.jhelp.engine.view.View3D
import fr.jhelp.tasks.parallel

/**
 * Manipulate [View3D] scene
 */
fun View3D.scene(scene: Scene3D.() -> Unit) =
    parallel(scene) { sceneModifier -> sceneModifier(this.scene3D) }

/**
 * Scene roo position short cut
 */
val Scene3D.position get() = this.root.position

/**
 * Manipulate [Node3D] position
 */
fun <N : Node3D> N.position(position: Position3D.() -> Unit): N
{
    position(this.position)
    return this
}

/**
 * Manipulate [Object3D] material
 */
fun Object3D.material(material: Material.() -> Unit): Object3D
{
    material(this.material)
    return this
}

/**
 * Manipulate [Clone3D] material
 */
fun Clone3D.material(material: Material.() -> Unit): Clone3D
{
    material(this.material)
    return this
}

/**
 * Modify a texture, if not sealed.
 *
 * Automatically refreshed
 *
 * @param draw Described what drawing if texture not null and not sealed
 */
fun Texture?.draw(draw: (Bitmap, Canvas, Paint) -> Unit): Texture?
{
    val bitmap = this?.bitmap()
    val canvas = this?.canvas()
    val paint = this?.paint()

    if (bitmap != null && canvas != null && paint != null)
    {
        draw(bitmap, canvas, paint)
        this?.refresh()
    }

    return this
}