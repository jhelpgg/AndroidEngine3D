package fr.jhelp.tasks.network

import android.net.ConnectivityManager
import android.net.Network
import fr.jhelp.tasks.observable.Observable

internal object NetworkStatusCallback :ConnectivityManager.NetworkCallback()
{
    val availableObservable = Observable(false)

    override fun onAvailable(network: Network)
    {
        this.availableObservable.changeValue(true)
    }

    override fun onLost(network: Network)
    {
        this.availableObservable.changeValue(false)
    }
}