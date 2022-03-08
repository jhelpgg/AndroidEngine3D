/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.ui.views

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnAttach
import androidx.core.view.doOnDetach
import fr.jhelp.provided.provided
import fr.jhelp.tasks.Mutex
import fr.jhelp.tasks.atomics.atomic
import fr.jhelp.tasks.observable.Observable
import fr.jhelp.tasks.parallelUI
import fr.jhelp.ui.extensions.dp
import fr.jhelp.ui.tools.DensityPixel

abstract class ViewUI<V : View>
{
    private var status by atomic(ViewStatus.NOT_CREATED)
    private val context: Context by provided<Context>()
    private lateinit var view: V
    private val mutex = Mutex()
    private val marginLayoutParameters: ViewGroup.MarginLayoutParams =
        ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                     ViewGroup.LayoutParams.MATCH_PARENT)
    val marginStart: Observable<DensityPixel> =
        Observable<DensityPixel>(this.marginLayoutParameters.marginStart.dp)
    val marginEnd: Observable<DensityPixel> =
        Observable<DensityPixel>(this.marginLayoutParameters.marginEnd.dp)
    val marginTop: Observable<DensityPixel> =
        Observable<DensityPixel>(this.marginLayoutParameters.topMargin.dp)
    val marginBottom: Observable<DensityPixel> =
        Observable<DensityPixel>(this.marginLayoutParameters.bottomMargin.dp)

    init
    {
        this.marginStart.observe { densityPixel ->
            this.marginLayoutParameters.marginStart = densityPixel.pixels
            this.updateLayout()
        }

        this.marginEnd.observe { densityPixel ->
            this.marginLayoutParameters.marginEnd = densityPixel.pixels
            this.updateLayout()
        }

        this.marginTop.observe { densityPixel ->
            this.marginLayoutParameters.topMargin = densityPixel.pixels
            this.updateLayout()
        }

        this.marginBottom.observe { densityPixel ->
            this.marginLayoutParameters.bottomMargin = densityPixel.pixels
            this.updateLayout()
        }
    }

    fun view() : V {
        this.mutex {
            if (this.status == ViewStatus.NOT_CREATED)
            {
                this.status = ViewStatus.DETACHED
                this.view = this.createView(this.context)
                this.view.layoutParams = this.marginLayoutParameters
                this.view.doOnAttach { this.status = ViewStatus.ATTACHED }
                this.view.doOnDetach { this.status = ViewStatus.DETACHED }
                
            }
        }

        return this.view
    }

    protected abstract fun createView(context: Context): V

    private fun updateLayout()
    {
        if (this.status == ViewStatus.ATTACHED)
        {
            parallelUI { this.view.layoutParams = this.marginLayoutParameters }
        }
    }
}
