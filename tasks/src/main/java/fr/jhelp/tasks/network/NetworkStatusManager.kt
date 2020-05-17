package fr.jhelp.tasks.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.annotation.RequiresPermission
import java.util.concurrent.atomic.AtomicBoolean

object NetworkStatusManager
{
    private val initialized = AtomicBoolean(false)

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

    fun destroy(context: Context)
    {
        if(this.initialized.getAndSet(false))
        {
            context.getSystemService(ConnectivityManager::class.java)
                ?.unregisterNetworkCallback(NetworkStatusCallback)
            NetworkStatusQueue.stop()
        }
    }
}