/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.multitools.showroom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.jhelp.multitools.R
import fr.jhelp.multitools.engine.AnimationActivity
import fr.jhelp.multitools.engine.BoxActivity
import fr.jhelp.multitools.engine.FieldActivity
import fr.jhelp.multitools.engine.PlaneActivity
import fr.jhelp.multitools.engine.RevolutionActivity
import fr.jhelp.multitools.engine.SphereActivity
import fr.jhelp.multitools.images.AddImageActivity
import fr.jhelp.multitools.images.BumpedImageActivity
import fr.jhelp.multitools.images.ContrastImageActivity
import fr.jhelp.multitools.images.DarkerImageActivity
import fr.jhelp.multitools.images.GreyImageActivity
import fr.jhelp.multitools.images.InvertImageActivity
import fr.jhelp.multitools.images.MaskedImageActivity
import fr.jhelp.multitools.images.MultiplyImageActivity
import fr.jhelp.multitools.images.ShiftImageActivity
import fr.jhelp.multitools.images.TintImageActivity
import fr.jhelp.multitools.tutorial.DicedActivity
import fr.jhelp.multitools.tutorial.DiffuseMaterialActivity
import fr.jhelp.multitools.tutorial.HelloWorldActivity
import fr.jhelp.multitools.tutorial.RobotActivity
import fr.jhelp.multitools.tutorial.TextureAndDiffuseMaterialActivity
import fr.jhelp.multitools.tutorial.TextureMaterialActivity

object ShowRoomAdapter : RecyclerView.Adapter<ShowRoomViewHolder<*>>()
{
    private val content: Array<ShowRoomElement> =
        arrayOf(ShowRoomTitle(R.string.imageManipulationTitle),
                ShowRoomPreview(R.drawable.preview_grey, R.string.greyImageTitle,
                                GreyImageActivity::class.java),
                ShowRoomPreview(R.drawable.preview_tint, R.string.tintImageTitle,
                                TintImageActivity::class.java),
                ShowRoomPreview(R.drawable.preview_invert, R.string.invertImageTitle,
                                InvertImageActivity::class.java),
                ShowRoomPreview(R.drawable.preview_add, R.string.addImageTitle,
                                AddImageActivity::class.java),
                ShowRoomPreview(R.drawable.preview_multiply, R.string.multiplyImageTitle,
                                MultiplyImageActivity::class.java),
                ShowRoomPreview(R.drawable.preview_dark, R.string.darkerImageTitle,
                                DarkerImageActivity::class.java),
                ShowRoomPreview(R.drawable.preview_contrast, R.string.contrastImageTitle,
                                ContrastImageActivity::class.java),
                ShowRoomPreview(R.drawable.preview_bump, R.string.bumpImageTitle,
                                BumpedImageActivity::class.java),
                ShowRoomPreview(R.drawable.preview_mask, R.string.maskImageTitle,
                                MaskedImageActivity::class.java),
                ShowRoomPreview(R.drawable.preview_shift, R.string.shiftImageTitle,
                                ShiftImageActivity::class.java),

            // --- 3D ---

                ShowRoomTitle(R.string.engineSamplesTitle),
                ShowRoomPreview(R.drawable.preview_plane, R.string.planeTitle,
                                PlaneActivity::class.java),
                ShowRoomPreview(R.drawable.preview_box, R.string.boxTitle,
                                BoxActivity::class.java),
                ShowRoomPreview(R.drawable.preview_sphere, R.string.sphereTitle,
                                SphereActivity::class.java),
                ShowRoomPreview(R.drawable.preview_revolution, R.string.revolutionTitle,
                                RevolutionActivity::class.java),
                ShowRoomPreview(R.drawable.preview_field, R.string.fieldTitle,
                                FieldActivity::class.java),
                ShowRoomPreview(R.drawable.preview_animation, R.string.animationTitle,
                                AnimationActivity::class.java),

            // Tutorials

                ShowRoomTitle(R.string.tutorialsTitle),
                ShowRoomPreview(R.drawable.preview_hello_world,
                                R.string.helloWorldTitle,
                                HelloWorldActivity::class.java),
                ShowRoomPreview(R.drawable.preview_diffuse,
                                R.string.materialDiffuseTitle,
                                DiffuseMaterialActivity::class.java),
                ShowRoomPreview(R.drawable.preview_texture,
                                R.string.materialTextureTitle,
                                TextureMaterialActivity::class.java),
                ShowRoomPreview(R.drawable.preview_texture_and_diffuse,
                                R.string.materialTextureAndDiffuseTitle,
                                TextureAndDiffuseMaterialActivity::class.java),
                ShowRoomPreview(R.drawable.preview_robot,
                                R.string.robotTitle,
                                RobotActivity::class.java),
                ShowRoomPreview(R.drawable.preview_dice,
                                R.string.diceTitle,
                                DicedActivity::class.java)
               )

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ShowRoomViewHolder<*> =
        when (viewType)
        {
            SHOW_ROOM_TITLE_TYPE   ->
            {
                val layoutInflater = parent.context.getSystemService(LayoutInflater::class.java)!!
                val view = layoutInflater.inflate(R.layout.show_room_title, parent, false)
                ShowRoomTitleHolder(view)
            }
            SHOW_ROOM_PREVIEW_TYPE ->
            {
                val layoutInflater = parent.context.getSystemService(LayoutInflater::class.java)!!
                val view = layoutInflater.inflate(R.layout.show_room_preview, parent, false)
                ShowRoomPreviewHolder(view)
            }
            else                   -> throw IllegalArgumentException("Unknown type $viewType")
        }


    override fun getItemCount() = this.content.size

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: ShowRoomViewHolder<*>, position: Int)
    {
        (holder as ShowRoomViewHolder<ShowRoomElement>).update(this.content[position])
    }

    override fun getItemViewType(position: Int) =
        this.content[position].type
}