/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.engine.view

import fr.jhelp.engine.scene.Point3D
import fr.jhelp.images.Point2D
import fr.jhelp.tasks.observable.Observable
import fr.jhelp.tasks.observable.ObservableValue

internal val viewBoundsObservableValue =
    ObservableValue<ViewBounds>(ViewBounds(Point3D(0f, 0f, 0f), Point3D(0f, 0f, 0f),
                                           Point2D(0f, 0f), Point2D(0f, 0f)))
val viewBounds: Observable<ViewBounds> = viewBoundsObservableValue.observable
