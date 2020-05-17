package fr.jhelp.multitools

import android.app.Application
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