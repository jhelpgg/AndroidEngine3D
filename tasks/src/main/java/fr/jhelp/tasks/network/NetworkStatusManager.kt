/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.tasks.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.annotation.RequiresPermission
import fr.jhelp.tasks.ThreadType
import fr.jhelp.tasks.network.NetworkStatusManager.destroy
import fr.jhelp.tasks.network.NetworkStatusManager.initialize
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Network manager, for make work [ThreadType.NETWORK]
 *
 * It will requires the permissions:
 * * android.Manifest.permission.INTERNET
 * * android.Manifest.permission.ACCESS_NETWORK_STATE
 *
 * To start the management, call [initialize], to stop it call [destroy]
 */
object NetworkStatusManager
{
    private val initialized = AtomicBoolean(false)

    /**
     * Initialize the manager.
     */
    @RequiresPermission(allOf =
                        [android.Manifest.permission.INTERNET,
                            android.Manifest.permission.ACCESS_NETWORK_STATE])
    fun initialize(context: Context)
    {
        if (!this.initialized.getAndSet(true))
        {
            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            context.getSystemService(ConnectivityManager::class.java)
                ?.registerNetworkCallback(request, NetworkStatusCallback)
        }
    }

    /**
     * Stop network management
     */
    fun destroy(context: Context)
    {
        if (this.initialized.getAndSet(false))
        {
            context.getSystemService(ConnectivityManager::class.java)
                ?.unregisterNetworkCallback(NetworkStatusCallback)
            NetworkStatusQueue.stop()
        }
    }
}