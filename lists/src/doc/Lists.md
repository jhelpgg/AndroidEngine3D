# Additional sets

This modules some additional sets to standards ones

1. [ArrayInt](#arrayint)
2. [Queue](#queue)
3. [SortedArray](#sortedarray)
4. [Extensions tools](#extensions-tools)

### ArrayInt

[ArrayInt](../main/java/fr/jhelp/lists/ArrayInt.kt) represents a dynamic
array of Int. It is more efficient than `ArrayList<Int>`

Elements can be add, insert, remove, get, set  or sort

### Queue

[Queue](../main/java/fr/jhelp/lists/Queue.kt) represents a queue,
FIFO (First In, First Out), of elements

Elements can be enqueue or dequeue

### SortedArray

[SortedArray](../main/java/fr/jhelp/lists/SortedArray.kt) array of sorted elements.

Elements are sort by a given comparator at construction.

For comparable, the method `sortedArray` create a `SortedArray` based on their  
"natural order"

`SortedArray` can be create un `unique`mode. In this mode, when add an element,
it checks if an elements considered equals, for the given comparator at construction,
already exists. If exists, the addition is ignored. So all elements will be unique,
in comparator point of view.

Since its sorted : `add`, `get`, `contains` , `indexOf` and `remove` are O(LN(n))

`removeAt` is O(1)

`removeIf` is O(n)

### Extensions tools

In [Lists](../main/java/fr/jhelp/lists/Lists.kt) can found some extensions:

`transform` on `Iterator` or `Iterable` : creates an `Iterator` or `Iterable`
that contains elements transformed by given action

`smartFilter` on `Iterator` or `Iterable` : creates an `Iterator` or `Iterable`  
that contains only elements filtered by given filter

They are faster and takes less memory than `Iterable.map` and `Iterable.filter`

