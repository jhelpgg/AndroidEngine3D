# Android 3D engine and other tools

This package is cut in several modules, it includes :
* Some utilities
* Thread management based on coroutines and network tasks do as soon as internet available.
  Easy way to choose in each thread types tasks are done
* 3D engine
* Some images manipulations
* Database management in NoSQL spirit

Documentation about :
1. Utilities : [Menu](utilities/src/doc/Menu.md)
    1. [Formal operations](utilities/src/doc/formal/Formal.md)
        1. [Presentation](utilities/src/doc/formal/Formal.md#presentation)
        2. [Actual mathematical function types](utilities/src/doc/formal/Formal.md#actual-mathematical-function-types)
           1. [Constants](utilities/src/doc/formal/Formal.md#constants)
           2. [Variables](utilities/src/doc/formal/Formal.md#variables)
           3. [Unary minus](utilities/src/doc/formal/Formal.md#unary-minus)
           4. [Addition](utilities/src/doc/formal/Formal.md#addition)
           5. [Subtraction](utilities/src/doc/formal/Formal.md#subtraction)
           6. [Multiplication](utilities/src/doc/formal/Formal.md#multiplication)
           7. [Division](utilities/src/doc/formal/Formal.md#division)
           8. [Cosinus](utilities/src/doc/formal/Formal.md#cosinus)
           9. [Sinus](utilities/src/doc/formal/Formal.md#sinus)
        3. [Possible operations](utilities/src/doc/formal/Formal.md#possible-operations)
           1. [Simplification](utilities/src/doc/formal/Formal.md#simplification)
           2. [Replace](utilities/src/doc/formal/Formal.md#replace)
           3. [Derivative](utilities/src/doc/formal/Formal.md#derivative)
           4. [Collect variables](utilities/src/doc/formal/Formal.md#collect-variables)
    2. [Regular expression](utilities/src/doc/regex/Regex.md)
        1. [Presentation](utilities/src/doc/regex/Regex.md#presentation)
        2. [Base expressions](utilities/src/doc/regex/Regex.md#base-expressions)
        3. [Repetition](utilities/src/doc/regex/Regex.md#repetition)
        4. [Combination](utilities/src/doc/regex/Regex.md#combination)
        5. [Capturing groups](utilities/src/doc/regex/Regex.md#capturing-groups)
        6. [Matching](utilities/src/doc/regex/Regex.md#matching)
        7. [Replacement](utilities/src/doc/regex/Regex.md#replacement)
           1. [Basic replacement](utilities/src/doc/regex/Regex.md#basic-replacement)
           2. [Replace a capturing group](utilities/src/doc/regex/Regex.md#replace-a-capturing-group)
        8. [Existing regular expressions](utilities/src/doc/regex/Regex.md#existing-regular-expressions)
        9. [Memory and performance note](utilities/src/doc/regex/Regex.md#memory-and-performance-note)
2. Task management : [Task management](tasks/src/doc/Menu.md)
    1. [Thread type and tasks](tasks/src/doc/ThreadTypeAndTasks.md)
        1. [Thread types](tasks/src/doc/ThreadTypeAndTasks.md#thread-types)
        2. [Tasks](tasks/src/doc/ThreadTypeAndTasks.md#tasks)
    2. [Network tasks](tasks/src/doc/NetworkTasks.md)
    3. [Promise and future result](tasks/src/doc/PromiseAndFutureResult.md)
        1. [Make a promise](tasks/src/doc/PromiseAndFutureResult.md#make-a-promise)
        2. [Chain to future result](tasks/src/doc/PromiseAndFutureResult.md#chain-to-future-result)
        3. [Cancellation management](tasks/src/doc/PromiseAndFutureResult.md#cancellation-management)
           1. [`canceled` example](tasks/src/doc/PromiseAndFutureResult.md#canceled-example)
           2. [`register` example](tasks/src/doc/PromiseAndFutureResult.md#register-example)
    4. [Observable](tasks/src/doc/Observable.md)
    5. [Task chain](tasks/src/doc/TaskChain.md)
    6. [Emitter](tasks/src/doc/Emitter.md)
    7. [Post system](tasks/src/doc/Post.md)
    8. [Async tools](tasks/src/doc/Async.md)
        1. [forEachAsync](tasks/src/doc/Async.md#foreachasync)
        2. [emit](tasks/src/doc/Async.md#emit)

