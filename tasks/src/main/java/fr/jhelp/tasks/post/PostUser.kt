/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.tasks.post

/**
 * User of [PostOffice]
 */
interface PostUser<M>
{
    /**
     * Called when received a message
     *
     * @param senderName Post user sender name (Useful for reply to him)
     * @param messageFor Indicates if message is private or for a group
     * @param message Delivered message
     */
    fun receive(senderName: String, messageFor: MessageFor, message: M)

    /**
     * Post a message for a specific user
     *
     * @param userName User name to send the message
     * @param postOffice Post office to use for delivery
     * @param message Message to send
     * @return `true` if message will be delivered.
     * Else it means the user not registered in specified post office
     */
    fun toUser(userName: String, postOffice: PostOffice<M>, message: M) =
        postOffice.postToUser(this, userName, message)

    /**
     * Post a message to a group
     *
     * @param groupName Group name receiver
     * @param postOffice Post office to use for delivery
     * @param message Message to send
     * @return `true` if message will be delivered.
     * Else it means the group not exists in specified post office
     */
    fun toGroup(groupName: String, postOffice: PostOffice<M>, message: M) =
        postOffice.postToGroup(this, groupName, message)
}