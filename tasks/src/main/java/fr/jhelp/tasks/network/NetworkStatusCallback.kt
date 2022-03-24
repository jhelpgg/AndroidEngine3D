/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.tasks.network

import android.net.ConnectivityManager
import android.net.Network
import fr.jhelp.tasks.observable.Observable
import fr.jhelp.tasks.observable.ObservableValue

/**
 * React to Network status changed
 */
internal object NetworkStatusCallback : ConnectivityManager.NetworkCallback()
{
    private val availableObservableValue = ObservableValue<Boolean>(false)
    val availableObservable: Observable<Boolean> = this.availableObservableValue.observable

    override fun onAvailable(network: Network)
    {
        this.availableObservableValue.value = true
    }

    override fun onLost(network: Network)
    {
        this.availableObservableValue.value = false
    }
}