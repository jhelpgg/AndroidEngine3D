package fr.jhelp.tasks.post

import fr.jhelp.tasks.forEachAsync
import fr.jhelp.tasks.parallel
import java.util.Collections

/**
 * Post office deliver messages between registered users.
 *
 * User can register from zero to several delivery group
 *
 * User can send message to a specific user or to a group of users
 *
 * To use post office service, user must to register in it first
 *
 * Each user have a unique name.
 *
 * To receive message, : at least register. Then may register to one or several groups (To receive this group message)
 *
 * To send message the user must
 * 1. Be registered
 * 2. Know the user name (for private message) or group name (for send to group)
 * 3. Use one one method : [PostUser.toUser] or [PostUser.toGroup]
 */
class PostOffice<M>
{
    private val users = HashMap<String, PostUser<M>>()
    private val groups = HashMap<String, ArrayList<PostUser<M>>>()

    /** Set of registered users */
    val usersSet: Set<String>
        get() = synchronized(this.users) {
            Collections.unmodifiableSet(this.users.keys)
        }

    /** Set of existing groups */
    val groupsSet: Set<String>
        get() = synchronized(this.groups) {
            Collections.unmodifiableSet(this.groups.keys)
        }

    // Must be called in synchronized(this.users) block
    private fun userName(postUser: PostUser<M>): String
    {
        for (entry in this.users.entries)
        {
            if (entry.value == postUser)
            {
                return entry.key
            }
        }

        throw IllegalArgumentException("User $postUser not registered")
    }

    internal fun postToUser(sender: PostUser<M>, userName: String, message: M): Boolean
    {
        synchronized(this.users)
        {
            val senderName = this.userName(sender)
            val postUser = this.users[userName] ?: return false
            parallel { postUser.receive(senderName, MessageFor(true, ""), message) }
            return true
        }
    }

    internal fun postToGroup(sender: PostUser<M>, groupName: String, message: M): Boolean
    {
        synchronized(this.groups)
        {
            val senderName = this.userName(sender)
            val users = this.groups[groupName] ?: return false
            val messageFor = MessageFor(false, groupName)
            users.forEachAsync { postUser -> postUser.receive(senderName, messageFor, message) }
            return true
        }
    }

    /**
     * Register user to post service
     *
     * @return `true` if registration done. Else its means an other user already have the given name
     */
    fun registerUser(userName: String, postUser: PostUser<M>): Boolean
    {
        synchronized(this.users)
        {
            if (this.users[userName] != null)
            {
                return false
            }

            this.users[userName] = postUser
            return true
        }
    }

    /**
     * Indicates if a user with given name is registered
     */
    fun userExists(userName: String) =
        synchronized(this.users) { this.users.containsKey(userName) }

    /**
     * Register a user to a group
     */
    fun registerToGroup(groupName: String, postUser: PostUser<M>)
    {
        synchronized(this.groups)
        {
            this.groups.getOrPut(groupName, { ArrayList() }).add(postUser)
        }
    }

    /**
     * Unregister user from group
     */
    fun unregisterFromGroup(groupName: String, postUser: PostUser<M>)
    {
        synchronized(this.groups)
        {
            val users = this.groups[groupName] ?: return
            users.remove(postUser)

            if (users.isEmpty())
            {
                this.groups.remove(groupName)
            }
        }
    }

    /**
     * Unregister user form all group it is in
     */
    fun unregisterFromAllGroups(postUser: PostUser<M>)
    {
        synchronized(this.groups)
        {
            val toRemove = ArrayList<String>()

            for (entry in this.groups.entries)
            {
                entry.value.remove(postUser)

                if (entry.value.isEmpty())
                {
                    toRemove.add(entry.key)
                }
            }

            toRemove.forEach { key -> this.groups.remove(key) }
        }
    }

    /**
     * Indicates if a group with given name exists
     */
    fun groupExists(groupName: String) =
        synchronized(this.groups) { this.groups.containsKey(groupName) }
}