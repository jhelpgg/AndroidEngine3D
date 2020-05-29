/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database

import fr.jhelp.tasks.promise.Promise

internal sealed class DatabaseOrder

internal class OrderStore<DS : DataStorable>(val key: String, val dataStorable: DS) :
    DatabaseOrder()

internal class OrderRead<DS : DataStorable>(val key: String, val promise: Promise<DS>) :
    DatabaseOrder()

internal object OrderShutDown : DatabaseOrder()

internal class OrderDelete(val key: String) : DatabaseOrder()

