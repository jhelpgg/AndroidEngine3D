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

data class ViewBounds(val topLeftNear: Point3D, val bottomRightFar: Point3D,
                      val topLet: Point2D, val bottomRight: Point2D)