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

fun View3D.scene(scene: Scene3D.() -> Unit) = scene(this.scene3D)

val Scene3D.position get() = this.root.position

fun <N:Node3D> N.position(position: Position3D.() -> Unit) :N
{
    position(this.position)
    return this
}

fun Object3D.material(material: Material.() -> Unit) :Object3D
{
    material(this.material)
    return this
}

fun Clone3D.material(material: Material.() -> Unit) : Clone3D
{
    material(this.material)
    return this
}

fun Texture?.draw(draw: (Bitmap,Canvas, Paint) -> Unit): Texture?
{
    val bitmap = this?.bitmap()
    val canvas = this?.canvas()
    val paint = this?.paint()

    if (bitmap!=null && canvas != null && paint != null)
    {
        draw(bitmap, canvas, paint)
    }

    return this
}