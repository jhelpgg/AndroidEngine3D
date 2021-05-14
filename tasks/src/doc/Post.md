# Post system

Post system work like a post office.

It have a [PostOffice](../main/java/fr/jhelp/tasks/post/PostOffice.kt),
where [PostUser](../main/java/fr/jhelp/tasks/post/PostUser.kt) register
to send and receive message.

It exists a group notion to able to send message to a user group.

First create a [PostOffice](../main/java/fr/jhelp/tasks/post/PostOffice.kt)

Then implements [PostUser](../main/java/fr/jhelp/tasks/post/PostUser.kt).
The only method to implements is `receive` that is called each time a message
is received.

Then register the user in `PostOffice` with the method `registerUser`.
Each user must have a different name.

Once register the `PostUser` can receive message or send message with its method
`toUser` or `toGroup`.

If the `PostUser` is not registered, it can't send message, so no message are delivered.

The method `receive` of `PostUser` receive the message,
the sender name to able to reply, and a [MessageFor](../main/java/fr/jhelp/tasks/post/MessageFor.kt)
object.

`MessageFor` describes is the message is send privately or to a group of user.

`PostUser` can register to a group in `PostOffice` via `registerToGroup` method

