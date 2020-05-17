package fr.jhelp.engine.resources

import androidx.annotation.DrawableRes
import fr.jhelp.engine.R

enum class Mouths(@DrawableRes private val drawableId: Int)
{
    ANNOY_1(R.drawable.annoy1),
    ANNOY_2(R.drawable.annoy2),
    ANNOY_3(R.drawable.annoy3),
    ANNOY_BIG_1(R.drawable.annoy_big1),
    ANNOY_BIG_2(R.drawable.annoy_big2),
    ANNOY_BIG_3(R.drawable.annoy_big3),
    ANNOY_SEMI_1(R.drawable.annoy_semi1),
    ANNOY_SEMI_2(R.drawable.annoy_semi2),
    ANNOY_SEMI_3(R.drawable.annoy_semi3),
    SAD_1(R.drawable.sad1),
    SAD_2(R.drawable.sad2),
    SAD_3(R.drawable.sad3),
    SERIOUS_1(R.drawable.serious1),
    SERIOUS_2(R.drawable.serious2),
    SERIOUS_3(R.drawable.serious3),
    SMILE_1(R.drawable.smile1),
    SMILE_2(R.drawable.smile2),
    SMILE_3(R.drawable.smile3),
    SMILE_BIG_1(R.drawable.smile_big1),
    SMILE_BIG_2(R.drawable.smile_big2),
    SMILE_BIG_3(R.drawable.smile_big3),
    SMILE_OTHER_1(R.drawable.smile_other1),
    SMILE_OTHER_2(R.drawable.smile_other2),
    SMILE_OTHER_3(R.drawable.smile_other3),
    WHISPER_1(R.drawable.whisper1),
    WHISPER_2(R.drawable.whisper2),
    WHISPER_3(R.drawable.whisper3)
    ;

    val texture by lazy { ResourcesAccess.obtainTexture(this.drawableId) }

    fun bitmap() = ResourcesAccess.obtainBitmap(this.drawableId)
}