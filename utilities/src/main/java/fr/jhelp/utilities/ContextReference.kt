/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.utilities

import android.annotation.SuppressLint
import android.app.Application
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentSender
import android.content.IntentSender.SendIntentException
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.UserHandle
import android.view.Display
import androidx.annotation.RequiresApi
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.lang.ref.WeakReference


/**
 * Context can be used from anywhere
 *
 * It have to be initialized form Application as soon as possible, ideal is samething like:
 *
 * ````Kotlin
 * class MainApplication : Application()
 * {
 *    override fun onCreate()
 *    {
 *       super.onCreate()
 *       ContextReference.initialize(this)
 *       // ... May do something else
 *     }
 * ````
 *
 * With in `AndroidManifest.xml` :
 *
 * ````XML
 *     <application
 *        android:name=".MainApplication"
 * ````
 */
object ContextReference : Context()
{
    private var context =
        WeakReference<Context?>(null)
    private var resources =
        WeakReference<Resources?>(null)

    private fun context() =
        context.get()
        ?: throw RuntimeException("Context reference is lost or not initialized")

    private fun resources(): Resources
    {
        var resources = resources.get()

        if (resources == null)
        {
            resources = context().resources

            if (resources == null)
            {
                throw RuntimeException("Can't obtain Resources form stored Context")
            }

            ContextReference.resources = WeakReference(resources)
        }

        return resources
    }

    fun initialize(application: Application)
    {
        context = WeakReference(application.applicationContext)
        resources = WeakReference(application.resources)
    }

    override fun getAssets() =
        context().assets

    override fun getResources() =
        resources()

    override fun getPackageManager() =
        context().packageManager

    override fun getContentResolver() =
        context().contentResolver

    override fun getMainLooper() =
        context().mainLooper

    override fun getApplicationContext() =
        context().applicationContext

    override fun setTheme(resid: Int)
    {
        context.get()?.setTheme(resid)
    }

    override fun getTheme() = context().theme

    override fun getClassLoader() =
        context().classLoader

    override fun getPackageName() =
        context().packageName

    override fun getApplicationInfo() =
        context().applicationInfo

    override fun getPackageResourcePath() =
        context().packageResourcePath

    override fun getPackageCodePath() =
        context().packageCodePath

    override fun getSharedPreferences(name: String, mode: Int) =
        context().getSharedPreferences(name, mode)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun moveSharedPreferencesFrom(sourceContext: Context, name: String) =
        context.get()?.moveSharedPreferencesFrom(sourceContext, name)
        ?: false

    @RequiresApi(Build.VERSION_CODES.N)
    override fun deleteSharedPreferences(name: String) =
        context.get()?.deleteSharedPreferences(name)
        ?: false

    @Throws(FileNotFoundException::class)
    override fun openFileInput(name: String) =
        context.get()?.openFileInput(name)
        ?: throw FileNotFoundException("No context, no file : $name")

    @Throws(FileNotFoundException::class)
    override fun openFileOutput(name: String, mode: Int) =
        context.get()?.openFileOutput(name, mode)
        ?: throw FileNotFoundException("No context, no file : $name")

    override fun deleteFile(name: String) =
        context.get()?.deleteFile(name)
        ?: false

    override fun getFileStreamPath(name: String) =
        context()
            .getFileStreamPath(name)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun getDataDir() =
        context().dataDir

    override fun getFilesDir() =
        context().filesDir

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun getNoBackupFilesDir() =
        context().noBackupFilesDir

    override fun getExternalFilesDir(type: String?) =
        context()
            .getExternalFilesDir(type)

    override fun getExternalFilesDirs(type: String) =
        context.get()?.getExternalFilesDirs(type)
        ?: emptyArray()

    override fun getObbDir() =
        context().obbDir

    override fun getObbDirs() =
        context.get()?.obbDirs
        ?: emptyArray()

    override fun getCacheDir() =
        context().cacheDir

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun getCodeCacheDir() =
        context().codeCacheDir

    override fun getExternalCacheDir() =
        context().externalCacheDir

    override fun getExternalCacheDirs() =
        context.get()?.externalCacheDirs
        ?: emptyArray()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun getExternalMediaDirs() =
        context.get()?.externalMediaDirs
        ?: emptyArray()

    override fun fileList() =
        context.get()?.fileList()
        ?: emptyArray()

    override fun getDir(name: String, mode: Int) =
        context().getDir(name, mode)

    override fun openOrCreateDatabase(name: String, mode: Int, factory: CursorFactory?) =
        context()
            .openOrCreateDatabase(name, mode, factory)

    override fun openOrCreateDatabase(name: String,
                                      mode: Int,
                                      factory: CursorFactory,
                                      errorHandler: DatabaseErrorHandler?) =
        context().openOrCreateDatabase(name, mode, factory, errorHandler)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun moveDatabaseFrom(sourceContext: Context, name: String) =
        context.get()?.moveDatabaseFrom(sourceContext, name)
        ?: false

    override fun deleteDatabase(name: String) =
        context.get()?.deleteDatabase(name)
        ?: false

    override fun getDatabasePath(name: String) =
        context()
            .getDatabasePath(name)

    override fun databaseList() =
        context.get()?.databaseList()
        ?: emptyArray()

    override fun getWallpaper() =
        context().wallpaper

    override fun peekWallpaper() =
        context().peekWallpaper()

    override fun getWallpaperDesiredMinimumWidth() =
        context.get()?.wallpaperDesiredMinimumWidth
        ?: 1

    override fun getWallpaperDesiredMinimumHeight() =
        context.get()?.wallpaperDesiredMinimumHeight
        ?: 1

    @Throws(IOException::class)
    override fun setWallpaper(bitmap: Bitmap)
    {
        context.get()?.setWallpaper(bitmap)
    }

    @Throws(IOException::class)
    override fun setWallpaper(data: InputStream)
    {
        context.get()?.setWallpaper(data)
    }

    @Throws(IOException::class)
    override fun clearWallpaper()
    {
        context.get()?.clearWallpaper()
    }

    override fun startActivity(intent: Intent)
    {
        context.get()?.startActivity(intent)
    }

    override fun startActivity(intent: Intent, options: Bundle?)
    {
        context.get()?.startActivity(intent, options)
    }

    override fun startActivities(intents: Array<Intent>)
    {
        context.get()?.startActivities(intents)
    }

    override fun startActivities(intents: Array<Intent>, options: Bundle)
    {
        context.get()?.startActivities(intents, options)
    }

    @Throws(SendIntentException::class)
    override fun startIntentSender(intent: IntentSender,
                                   fillInIntent: Intent?,
                                   flagsMask: Int,
                                   flagsValues: Int,
                                   extraFlags: Int)
    {
        context.get()
            ?.startIntentSender(intent, fillInIntent, flagsMask, flagsValues, extraFlags)
    }

    @Throws(SendIntentException::class)
    override fun startIntentSender(intent: IntentSender,
                                   fillInIntent: Intent?,
                                   flagsMask: Int,
                                   flagsValues: Int,
                                   extraFlags: Int,
                                   options: Bundle?)
    {
        context.get()
            ?.startIntentSender(intent, fillInIntent, flagsMask, flagsValues, extraFlags, options)
    }

    override fun sendBroadcast(intent: Intent)
    {
        context.get()?.sendBroadcast(intent)
    }

    override fun sendBroadcast(intent: Intent, receiverPermission: String?)
    {
        context.get()?.sendBroadcast(intent, receiverPermission)
    }

    override fun sendOrderedBroadcast(intent: Intent, receiverPermission: String?)
    {
        context.get()?.sendOrderedBroadcast(intent, receiverPermission)
    }

    override fun sendOrderedBroadcast(intent: Intent,
                                      receiverPermission: String?,
                                      resultReceiver: BroadcastReceiver?,
                                      scheduler: Handler?,
                                      initialCode: Int,
                                      initialData: String?,
                                      initialExtras: Bundle?)
    {
        context.get()?.sendOrderedBroadcast(intent,
                                            receiverPermission,
                                            resultReceiver,
                                            scheduler,
                                            initialCode,
                                            initialData,
                                            initialExtras)
    }

    @SuppressLint("MissingPermission")
    override fun sendBroadcastAsUser(intent: Intent, user: UserHandle)
    {
        context.get()?.sendBroadcastAsUser(intent, user)
    }

    @SuppressLint("MissingPermission")
    override fun sendBroadcastAsUser(intent: Intent, user: UserHandle, receiverPermission: String?)
    {
        context.get()?.sendBroadcastAsUser(intent, user, receiverPermission)
    }

    @SuppressLint("MissingPermission")
    override fun sendOrderedBroadcastAsUser(intent: Intent,
                                            user: UserHandle,
                                            receiverPermission: String?,
                                            resultReceiver: BroadcastReceiver,
                                            scheduler: Handler?,
                                            initialCode: Int,
                                            initialData: String?,
                                            initialExtras: Bundle?)
    {
        context.get()?.sendOrderedBroadcastAsUser(intent,
                                                  user,
                                                  receiverPermission,
                                                  resultReceiver,
                                                  scheduler,
                                                  initialCode,
                                                  initialData,
                                                  initialExtras)
    }

    @SuppressLint("MissingPermission")
    override fun sendStickyBroadcast(intent: Intent)
    {
        context.get()?.sendStickyBroadcast(intent)
    }

    @SuppressLint("MissingPermission")
    override fun sendStickyOrderedBroadcast(intent: Intent,
                                            resultReceiver: BroadcastReceiver,
                                            scheduler: Handler?,
                                            initialCode: Int,
                                            initialData: String?,
                                            initialExtras: Bundle?)
    {
        context.get()?.sendStickyOrderedBroadcast(intent,
                                                  resultReceiver,
                                                  scheduler,
                                                  initialCode,
                                                  initialData,
                                                  initialExtras)
    }

    @SuppressLint("MissingPermission")
    override fun removeStickyBroadcast(intent: Intent)
    {
        context.get()?.removeStickyBroadcast(intent)
    }

    @SuppressLint("MissingPermission")
    override fun sendStickyBroadcastAsUser(intent: Intent, user: UserHandle)
    {
        context.get()?.sendStickyBroadcastAsUser(intent, user)
    }

    @SuppressLint("MissingPermission")
    override fun sendStickyOrderedBroadcastAsUser(intent: Intent,
                                                  user: UserHandle,
                                                  resultReceiver: BroadcastReceiver,
                                                  scheduler: Handler?,
                                                  initialCode: Int,
                                                  initialData: String?,
                                                  initialExtras: Bundle?)
    {
        context.get()?.sendStickyOrderedBroadcastAsUser(intent,
                                                        user,
                                                        resultReceiver,
                                                        scheduler,
                                                        initialCode,
                                                        initialData,
                                                        initialExtras)
    }

    @SuppressLint("MissingPermission")
    override fun removeStickyBroadcastAsUser(intent: Intent, user: UserHandle)
    {
        context.get()?.removeStickyBroadcastAsUser(intent, user)
    }

    override fun registerReceiver(receiver: BroadcastReceiver?, filter: IntentFilter): Intent?
    {
        return context.get()?.registerReceiver(receiver, filter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun registerReceiver(receiver: BroadcastReceiver?,
                                  filter: IntentFilter,
                                  flags: Int): Intent?
    {
        return context.get()?.registerReceiver(receiver, filter, flags)
    }

    override fun registerReceiver(receiver: BroadcastReceiver,
                                  filter: IntentFilter,
                                  broadcastPermission: String?,
                                  scheduler: Handler?): Intent?
    {
        return context.get()
            ?.registerReceiver(receiver, filter, broadcastPermission, scheduler)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun registerReceiver(receiver: BroadcastReceiver,
                                  filter: IntentFilter,
                                  broadcastPermission: String?,
                                  scheduler: Handler?,
                                  flags: Int): Intent?
    {
        return context.get()
            ?.registerReceiver(receiver, filter, broadcastPermission, scheduler, flags)
    }

    override fun unregisterReceiver(receiver: BroadcastReceiver)
    {
        context.get()?.unregisterReceiver(receiver)
    }

    override fun startService(service: Intent): ComponentName?
    {
        return context().startService(service)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun startForegroundService(service: Intent): ComponentName?
    {
        return context()
            .startForegroundService(service)
    }

    override fun stopService(service: Intent): Boolean
    {
        val context = context.get()
        return context?.stopService(service)
               ?: false
    }

    override fun bindService(service: Intent, conn: ServiceConnection, flags: Int): Boolean
    {
        val context = context.get()
        return context?.bindService(service, conn, flags)
               ?: false
    }

    override fun unbindService(conn: ServiceConnection)
    {
        val context = context.get()
        context?.unbindService(conn)
    }

    override fun startInstrumentation(className: ComponentName,
                                      profileFile: String?,
                                      arguments: Bundle?): Boolean
    {
        val context = context.get()
        return context?.startInstrumentation(className, profileFile, arguments)
               ?: false
    }

    override fun getSystemService(name: String): Any?
    {
        return context().getSystemService(name)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun getSystemServiceName(serviceClass: Class<*>): String?
    {
        return context().getSystemServiceName(serviceClass)
    }

    override fun checkPermission(permission: String, pid: Int, uid: Int): Int
    {
        val context = context.get()
        return context?.checkPermission(permission, pid, uid)
               ?: PackageManager.PERMISSION_DENIED
    }

    override fun checkCallingPermission(permission: String): Int
    {
        val context = context.get()
        return context?.checkCallingPermission(permission)
               ?: PackageManager.PERMISSION_DENIED
    }

    override fun checkCallingOrSelfPermission(permission: String): Int
    {
        val context = context.get()
        return context?.checkCallingOrSelfPermission(permission)
               ?: PackageManager.PERMISSION_DENIED
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun checkSelfPermission(permission: String): Int
    {
        val context = context.get()
        return context?.checkSelfPermission(permission)
               ?: PackageManager.PERMISSION_DENIED
    }

    override fun enforcePermission(permission: String, pid: Int, uid: Int, message: String?)
    {
        val context = context.get()
        context?.enforcePermission(permission, pid, uid, message)
    }

    override fun enforceCallingPermission(permission: String, message: String?)
    {
        val context = context.get()
        context?.enforceCallingPermission(permission, message)
    }

    override fun enforceCallingOrSelfPermission(permission: String, message: String?)
    {
        val context = context.get()
        context?.enforceCallingOrSelfPermission(permission, message)
    }

    override fun grantUriPermission(toPackage: String, uri: Uri, modeFlags: Int)
    {
        val context = context.get()
        context?.grantUriPermission(toPackage, uri, modeFlags)
    }

    override fun revokeUriPermission(uri: Uri, modeFlags: Int)
    {
        val context = context.get()
        context?.revokeUriPermission(uri, modeFlags)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun revokeUriPermission(toPackage: String, uri: Uri, modeFlags: Int)
    {
        val context = context.get()
        context?.revokeUriPermission(toPackage, uri, modeFlags)
    }

    override fun checkUriPermission(uri: Uri, pid: Int, uid: Int, modeFlags: Int): Int
    {
        val context = context.get()
        return context?.checkUriPermission(uri, pid, uid, modeFlags)
               ?: PackageManager.PERMISSION_DENIED
    }

    override fun checkCallingUriPermission(uri: Uri, modeFlags: Int): Int
    {
        val context = context.get()
        return context?.checkCallingUriPermission(uri, modeFlags)
               ?: PackageManager.PERMISSION_DENIED
    }

    override fun checkCallingOrSelfUriPermission(uri: Uri, modeFlags: Int): Int
    {
        val context = context.get()
        return context?.checkCallingOrSelfUriPermission(uri, modeFlags)
               ?: PackageManager.PERMISSION_DENIED
    }

    override fun checkUriPermission(uri: Uri?,
                                    readPermission: String?,
                                    writePermission: String?,
                                    pid: Int,
                                    uid: Int,
                                    modeFlags: Int): Int
    {
        val context = context.get()
        return context?.checkUriPermission(uri,
                                           readPermission,
                                           writePermission,
                                           pid,
                                           uid,
                                           modeFlags)
               ?: PackageManager.PERMISSION_DENIED
    }

    override fun enforceUriPermission(uri: Uri, pid: Int, uid: Int, modeFlags: Int, message: String)
    {
        val context = context.get()
        context?.enforceUriPermission(uri, pid, uid, modeFlags, message)
    }

    override fun enforceCallingUriPermission(uri: Uri, modeFlags: Int, message: String)
    {
        val context = context.get()
        context?.enforceCallingUriPermission(uri, modeFlags, message)
    }

    override fun enforceCallingOrSelfUriPermission(uri: Uri, modeFlags: Int, message: String)
    {
        val context = context.get()
        context?.enforceCallingOrSelfUriPermission(uri, modeFlags, message)
    }

    override fun enforceUriPermission(uri: Uri?,
                                      readPermission: String?,
                                      writePermission: String?,
                                      pid: Int,
                                      uid: Int,
                                      modeFlags: Int,
                                      message: String?)
    {
        val context = context.get()
        context?.enforceUriPermission(uri,
                                      readPermission,
                                      writePermission,
                                      pid,
                                      uid,
                                      modeFlags,
                                      message)
    }

    @Throws(PackageManager.NameNotFoundException::class)
    override fun createPackageContext(packageName: String, flags: Int): Context
    {
        return context().createPackageContext(packageName, flags)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Throws(PackageManager.NameNotFoundException::class)
    override fun createContextForSplit(splitName: String): Context
    {
        return context().createContextForSplit(splitName)
    }

    override fun createConfigurationContext(overrideConfiguration: Configuration): Context
    {
        return context().createConfigurationContext(overrideConfiguration)
    }

    override fun createDisplayContext(display: Display): Context
    {
        return context().createDisplayContext(display)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun createDeviceProtectedStorageContext(): Context
    {
        return context().createDeviceProtectedStorageContext()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun isDeviceProtectedStorage(): Boolean
    {
        val context = context.get()
        return context?.isDeviceProtectedStorage
               ?: false
    }
}