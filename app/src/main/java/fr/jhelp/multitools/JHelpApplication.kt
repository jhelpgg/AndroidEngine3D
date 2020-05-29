/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.multitools

import android.app.Application
import fr.jhelp.sound.SoundManager
import fr.jhelp.utilities.ContextReference

class JHelpApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ContextReference.initialize(this)
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}