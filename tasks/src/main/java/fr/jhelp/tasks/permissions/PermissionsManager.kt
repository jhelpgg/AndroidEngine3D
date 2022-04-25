/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.tasks.permissions

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import fr.jhelp.tasks.promise.FutureResult
import fr.jhelp.tasks.promise.Promise

class PermissionsManager(activity: ComponentActivity)
{
    private val permissionsStatus = HashMap<String, PermissionStatus>()
    private val applicationContext = activity.applicationContext
    private val waitingTasks = ArrayList<PermissionPromise>()

    private val permissionsRequester =
        activity.activityResultRegistry.register("PERMISSIONS",
                                                 ActivityResultContracts.RequestMultiplePermissions(),
                                                 this::permissionsResult)

    fun onPermissions(vararg permissions: String): FutureResult<Unit>
    {
        val promise = Promise<Unit>()
        val permissionTask = PermissionPromise(promise, permissions)

        if (this.launchTask(permissionTask))
        {
            return promise.future
        }

        synchronized(this.waitingTasks)
        {
            this.waitingTasks.add(permissionTask)
        }

        return promise.future
    }

    private fun permissionsResult(permissionsResult: Map<String, Boolean>)
    {
        synchronized(this.permissionsStatus)
        {
            for ((permission, granted) in permissionsResult)
            {
                this.permissionsStatus[permission] =
                    if (granted) PermissionStatus.GRANTED else PermissionStatus.DENIED
            }
        }

        synchronized(this.waitingTasks)
        {
            val iterator = this.waitingTasks.iterator()

            while (iterator.hasNext())
            {
                if (this.launchTask(iterator.next()))
                {
                    iterator.remove()
                }
            }
        }
    }

    /**
     * @return Indicates if managed now (`true`) or later (`false`)
     */
    private fun launchTask(permissionPromise: PermissionPromise): Boolean
    {
        var allGranted = true
        val toRequest = ArrayList<String>()

        synchronized(this.permissionsStatus)
        {

            for (permission in permissionPromise.permissions)
            {
                val status = this.permissionsStatus.getOrPut(permission) {
                    if (this.applicationContext
                            .checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED)
                    {
                        PermissionStatus.GRANTED
                    }
                    else
                    {
                        PermissionStatus.TO_REQUEST
                    }
                }

                when (status)
                {
                    PermissionStatus.TO_REQUEST ->
                    {
                        toRequest.add(permission)
                        allGranted = false
                    }
                    PermissionStatus.GRANTED    -> Unit
                    PermissionStatus.REQUESTING -> allGranted = false
                    PermissionStatus.DENIED     ->
                    {
                        permissionPromise.promise.error(PermissionDeniedException(permission))
                        return true
                    }
                }
            }
        }

        if (allGranted)
        {
            permissionPromise.promise.result(Unit)
            return true
        }

        if (toRequest.isNotEmpty())
        {
            synchronized(this.permissionsStatus)
            {
                for (permission in toRequest)
                {
                    this.permissionsStatus[permission] = PermissionStatus.REQUESTING
                }
            }

            this.permissionsRequester.launch(toRequest.toTypedArray())
        }

        return false
    }
}
