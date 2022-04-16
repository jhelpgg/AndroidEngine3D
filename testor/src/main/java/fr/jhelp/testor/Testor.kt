/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.testor

import android.os.Looper
import fr.jhelp.testor.io.createDirectory
import fr.jhelp.testor.io.deleteFull
import fr.jhelp.testor.io.privateDirectory
import fr.jhelp.testor.io.publicDirectory
import fr.jhelp.testor.io.referenceDirectory
import fr.jhelp.testor.security.mockKeyStore

/**
 * Clean all for next test, so that next test can start with a white page
 */
fun cleanForNextTests()
{
    referenceDirectory.deleteFull()
    referenceDirectory.createDirectory()
    privateDirectory.createDirectory()
    publicDirectory.createDirectory()
}

/**
 * Initialize testor
 */
internal fun initializeTestor()
{
    referenceDirectory.createDirectory()
    privateDirectory.createDirectory()
    publicDirectory.createDirectory()
    mockKeyStore()
}

/**
 * For all looper to loop now
 *
 * For thread that use looper like Main/UI thread, loopers not advance by them self.
 * So we have to force them to advance
 */
fun loopAllNow()
{
    Looper.loopAllNow()
}
