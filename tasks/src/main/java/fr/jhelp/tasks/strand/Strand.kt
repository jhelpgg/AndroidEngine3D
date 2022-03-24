/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.tasks.strand

import fr.jhelp.tasks.ThreadType

/**
 * Make a strand. Strand guarantee:
 * * Each method a called in given [ThreadType]
 * * Each method are execute atomically, that is to say, when a method called,
 *   other method called are queued until the method finished
 * * Called order is respected.
 * * Thread safe
 *
 * To create a Strand, need an interface to implements, and an implementation of this interface.
 *
 * The given implementation must not used anywhere else,
 * its methods are called by the strand when their turn comes in specified thread type.
 *
 * The object to use is given by the [invoke] method. It will provides all interface methods,
 * they will be called in Strand way.
 *
 * @param interfaceType Interface class to implements. It must be an interface else an exception is throw
 * @param instance The interface implementation reference. This instance must be use only their
 * @param threadType Thread type where execute the method
 */
class Strand<I>(interfaceType: Class<I>, instance: I, threadType: ThreadType = ThreadType.SHORT)
{
    private val caller = strandCaller(interfaceType, instance, threadType)

    /**
     * Object instance to use method call respects the Strand contract
     */
    operator fun invoke(): I = this.caller
}