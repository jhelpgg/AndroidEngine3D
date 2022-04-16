/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.testor

import fr.jhelp.testor.io.createDirectory
import fr.jhelp.testor.io.deleteFull
import fr.jhelp.testor.io.privateDirectory
import fr.jhelp.testor.io.publicDirectory
import fr.jhelp.testor.io.referenceDirectory
import fr.jhelp.testor.security.mockKeyStore


fun cleanForNextTests()
{
    referenceDirectory.deleteFull()
    referenceDirectory.createDirectory()
    privateDirectory.createDirectory()
    publicDirectory.createDirectory()
}

internal fun initializeTestor()
{
    referenceDirectory.createDirectory()
    privateDirectory.createDirectory()
    publicDirectory.createDirectory()
    mockKeyStore()
}
