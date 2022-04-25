/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.tasks.permissions

import fr.jhelp.tasks.ThreadType
import fr.jhelp.tasks.promise.Promise

internal class PermissionPromise(val promise:Promise<Unit>, val permissions:Array<out String>)
