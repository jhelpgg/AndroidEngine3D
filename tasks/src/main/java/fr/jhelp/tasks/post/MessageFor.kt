package fr.jhelp.tasks.post

/**
 * Indicates the message receiver's nature
 *
 * @param privateMassage If `true` the group is ignored, the message is for the receiver only.
 * Else the receiver group is specified
 * @param group Group name of receivers. Empty if private message
 */
data class MessageFor(val privateMassage: Boolean, val group: String)
