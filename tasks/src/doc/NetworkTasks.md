# Network tasks

As said in [Thread type and tasks](ThreadTypeAndTasks.md),
the [NetworkThread](../main/java/fr/jhelp/tasks/ThreadType.kt#L120) waits Intenet connection for do tasks.

Do be able know and react to network status , have to initialize : [NetworkStatusManager](../main/java/fr/jhelp/tasks/network/NetworkStatusManager.kt)

This class requires the permissions:
* android.Manifest.permission.INTERNET for Internet tasks
* android.Manifest.permission.ACCESS_NETWORK_STATE for know and react to network state change.

Use the method [initialize](../main/java/fr/jhelp/tasks/network/NetworkStatusManager.kt#L38)

It can be safely invoke several times

To stop the management : [destroy](../main/java/fr/jhelp/tasks/network/NetworkStatusManager.kt#L53)
method

