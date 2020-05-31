# Thread type and tasks

1. [Thread types](#thread-types)
2. [Tasks](#tasks)

### Thread types

Each task can be categorize in an execution environment type.
We called it a [ThreadType](../main/java/fr/jhelp/tasks/ThreadType.kt)

We defined 4 of them:

| ThreadType | Description |
|:---:|-----|
| [IndependentThread](../main/java/fr/jhelp/tasks/ThreadType.kt#L31) | For most of background tasks, that no need a particular environment |
| [MainThread](../main/java/fr/jhelp/tasks/ThreadType.kt#L62) | For and only for all UI operations like change a text, a color, a visibility, ... |
| [IOThread](../main/java/fr/jhelp/tasks/ThreadType.kt#L89) | For Input/Output operation on disk, lik read a file, request a database, ... |
| [NetworkThread](../main/java/fr/jhelp/tasks/ThreadType.kt#L120) | For any Internet interaction like access to a server, download something, ... |

:warning:
> NetworkThread work only if [NetworkStatusManager](../main/java/fr/jhelp/tasks/network/NetworkStatusManager.kt) properly initialized.
> See [Network tasks](NetworkTasks.md)

All tools here give the possibility to play task in one of those thread type

### Tasks

For start launch a task in one of thread type, just use one of method in
[Tasks](../main/java/fr/jhelp/tasks/Tasks.kt)

They a 3 type of launch, start of method name make recognize them.

| Start name | Explanation | Usage recommendation |
|:---:|-----|-----|
| parallel | Just launch the task and return immediately | For tasks no need to be link to other ones |
| launch | Launch the task, return immediately with an object to be able do something when result known or try to cancel the task before the end | For cancelable task or task that the result may be uses by an other task |
| delay | Schedule to launch the task in given dealy. Returns immediately with an object to be able do something when result known or cancel launch before delayed time expires | For planify do something later or implements a timeout |
