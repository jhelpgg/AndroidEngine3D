/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.tasks.post

/**
 * Indicates the message receiver's nature
 *
 * @param privateMassage If `true` the group is ignored, the message is for the receiver only.
 * Else the receiver group is specified
 * @param group Group name of receivers. Empty if private message
 */
data class MessageFor(val privateMassage: Boolean, val group: String)
