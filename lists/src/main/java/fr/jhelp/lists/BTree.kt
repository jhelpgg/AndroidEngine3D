package fr.jhelp.lists

import java.util.Stack
import java.util.concurrent.atomic.AtomicReference

fun <C : Comparable<C>> bTree() =
    BTree<C>(NaturalComparator())

class BTree<T>(private val comparator: Comparator<T>)
{
    private val tree = SortedArray(this.comparator)

    val size get() = this.tree.size

    val empty get() = this.tree.empty

    val notEmpty get() = this.tree.notEmpty


    fun add(element: T)
    {
        this.tree.add(element)
    }

    fun add(branch: BTree<T>)
    {
        this.tree.addAll(branch.tree)
    }

    fun addAll(iterable: Iterable<T>)
    {
        this.tree.addAll(iterable)
    }

    /**
     * Visit the tree with left to right deep algorithm.
     *
     * A tree with : `A, B, C ,D, E, F`:
     *
     *         C
     *        / \
     *       /   \
     *      A     E
     *       \   / \
     *       B  D   F
     *
     * Will be visit on the order : `B, A, D, F, E, C`
     *
     * The visitor returns a boolean to know if it wants to continue the visit or not
     */
    fun visitLeftRightDeep(visitor: (T) -> Boolean)
    {
        if (this.tree.empty)
        {
            return
        }

        val stack = Stack<VisitInfo>()
        stack.push(VisitInfo(0, this.size - 1))

        while (stack.isNotEmpty())
        {
            val visitInfo = stack.pop()

            when
            {
                visitInfo.explorable ->
                {
                    visitInfo.firstTime = false
                    stack.push(visitInfo)
                    stack.push(VisitInfo(visitInfo.middle + 1, visitInfo.max))
                    stack.push(VisitInfo(visitInfo.min, visitInfo.middle - 1))
                }
                visitInfo.firstTime  ->
                {
                    if (!visitor(this.tree[visitInfo.max]))
                    {
                        return
                    }

                    if (visitInfo.min < visitInfo.max)
                    {
                        if (!visitor(this.tree[visitInfo.min]))
                        {
                            return
                        }
                    }
                }
                else                 ->
                    if (!visitor(this.tree[visitInfo.middle]))
                    {
                        return
                    }
            }
        }
    }

    /**
     * Collects elements from the tree with left to right deep algorithm order.
     *
     * A tree with : `A, B, C ,D, E, F`:
     *
     *         C
     *        / \
     *       /   \
     *      A     E
     *       \   / \
     *       B  D   F
     *
     * Nodes will be tests on the order : `B, A, D, F, E, C`
     *
     * @param condition Condition on element to be collected or not
     * @param collector Collector of matching elements
     */
    fun collectLeftRightDeep(condition: (T) -> Boolean, collector: (T) -> Unit)
    {
        this.visitLeftRightDeep(visitorCollector(condition, collector))
    }

    /**
     * Search an element from the tree with left to right deep algorithm order.
     *
     * A tree with : `A, B, C ,D, E, F`:
     *
     *         C
     *        / \
     *       /   \
     *      A     E
     *       \   / \
     *       B  D   F
     *
     * Will be searched on the order : `B, A, D, F, E, C`
     *
     * @return The first element meet that match the condition or `null` if not found
     */
    fun searchLeftRightDeep(condition: (T) -> Boolean): T?
    {
        val atomicValue = AtomicReference<T>()
        this.visitLeftRightDeep(visitorSearch(condition, atomicValue::set))
        return atomicValue.get()
    }

    /**
     * Visit the tree with right to left deep algorithm.
     *
     * A tree with : `A, B, C ,D, E, F`:
     *
     *         C
     *        / \
     *       /   \
     *      A     E
     *       \   / \
     *       B  D   F
     *
     * Will be visit on the order : `F, D, E, B, A, C`
     *
     * The visitor returns a boolean to know if it wants to continue the visit or not
     */
    fun visitRightLeftDeep(visitor: (T) -> Boolean)
    {
        if (this.tree.empty)
        {
            return
        }

        val stack = Stack<VisitInfo>()
        stack.push(VisitInfo(0, this.size - 1))

        while (stack.isNotEmpty())
        {
            val visitInfo = stack.pop()

            when
            {
                visitInfo.explorable ->
                {
                    visitInfo.firstTime = false
                    stack.push(visitInfo)
                    stack.push(VisitInfo(visitInfo.min, visitInfo.middle - 1))
                    stack.push(VisitInfo(visitInfo.middle + 1, visitInfo.max))
                }
                visitInfo.firstTime  ->
                {
                    if (!visitor(this.tree[visitInfo.max]))
                    {
                        return
                    }

                    if (visitInfo.min < visitInfo.max)
                    {
                        if (!visitor(this.tree[visitInfo.min]))
                        {
                            return
                        }
                    }
                }
                else                 ->
                    if (!visitor(this.tree[visitInfo.middle]))
                    {
                        return
                    }
            }
        }
    }

    /**
     * Collects elements from the tree with right to left deep algorithm order.
     *
     * A tree with : `A, B, C ,D, E, F`:
     *
     *         C
     *        / \
     *       /   \
     *      A     E
     *       \   / \
     *       B  D   F
     *
     * Nodes will be tests on the order : `F, D, E, B, A, C`
     *
     * @param condition Condition on element to be collected or not
     * @param collector Collector of matching elements
     */
    fun collectRightLeftDeep(condition: (T) -> Boolean, collector: (T) -> Unit)
    {
        this.visitRightLeftDeep(visitorCollector(condition, collector))
    }

    /**
     * Search an element from the tree with right to left deep algorithm order.
     *
     * A tree with : `A, B, C ,D, E, F`:
     *
     *         C
     *        / \
     *       /   \
     *      A     E
     *       \   / \
     *       B  D   F
     *
     * Will be searched on the order : `F, D, E, B, A, C`
     *
     * @return The first element meet that match the condition or `null` if not found
     */
    fun searchRightLeftDeep(condition: (T) -> Boolean): T?
    {
        val atomicValue = AtomicReference<T>()
        this.visitRightLeftDeep(visitorSearch(condition, atomicValue::set))
        return atomicValue.get()
    }

    /**
     * Visit the tree with right to left algorithm.
     *
     * A tree with : `A, B, C ,D, E, F`:
     *
     *         C
     *        / \
     *       /   \
     *      A     E
     *       \   / \
     *       B  D   F
     *
     * Will be visit on the order : `C, E, F, D, A, B`
     *
     * The visitor returns a boolean to know if it wants to continue the visit or not
     */
    fun visitRightLeft(visitor: (T) -> Boolean)
    {
        if (this.tree.empty)
        {
            return
        }

        val queue = Queue<VisitInfo>()
        queue.enqueue(VisitInfo(0, this.size - 1))

        while (queue.notEmpty)
        {
            val visitInfo = queue.dequeue()

            when
            {
                visitInfo.explorable ->
                {
                    visitInfo.firstTime = false
                    queue.enqueue(visitInfo)
                    queue.enqueue(VisitInfo(visitInfo.middle + 1, visitInfo.max))
                    queue.enqueue(VisitInfo(visitInfo.min, visitInfo.middle - 1))
                }
                visitInfo.firstTime  ->
                {
                    if (!visitor(this.tree[visitInfo.min]))
                    {
                        return
                    }

                    if (visitInfo.min < visitInfo.max)
                    {
                        if (!visitor(this.tree[visitInfo.max]))
                        {
                            return
                        }
                    }
                }
                else                 ->
                    if (!visitor(this.tree[visitInfo.middle]))
                    {
                        return
                    }
            }
        }
    }

    /**
     * Collects elements from the tree with right to left algorithm order.
     *
     * A tree with : `A, B, C ,D, E, F`:
     *
     *         C
     *        / \
     *       /   \
     *      A     E
     *       \   / \
     *       B  D   F
     *
     * Nodes will be tests on the order : `C, E, F, D, A, B`
     *
     * @param condition Condition on element to be collected or not
     * @param collector Collector of matching elements
     */
    fun collectRightLeft(condition: (T) -> Boolean, collector: (T) -> Unit)
    {
        this.visitRightLeft(visitorCollector(condition, collector))
    }

    /**
     * Search an element from the tree with right to left  algorithm order.
     *
     * A tree with : `A, B, C ,D, E, F`:
     *
     *         C
     *        / \
     *       /   \
     *      A     E
     *       \   / \
     *       B  D   F
     *
     * Will be searched on the order : `C, E, F, D, A, B`
     *
     * @return The first element meet that match the condition or `null` if not found
     */
    fun searchRightLeft(condition: (T) -> Boolean): T?
    {
        val atomicValue = AtomicReference<T>()
        this.visitRightLeft(visitorSearch(condition, atomicValue::set))
        return atomicValue.get()
    }

    /**
     * Visit the tree with left to right algorithm.
     *
     * A tree with : `A, B, C ,D, E, F`:
     *
     *         C
     *        / \
     *       /   \
     *      A     E
     *       \   / \
     *       B  D   F
     *
     * Will be visit on the order : `C, A, B, E, D, F`
     *
     * The visitor returns a boolean to know if it wants to continue the visit or not
     */
    fun visitLeftRight(visitor: (T) -> Boolean)
    {
        if (this.tree.empty)
        {
            return
        }

        val queue = Queue<VisitInfo>()
        queue.enqueue(VisitInfo(0, this.size - 1))

        while (queue.notEmpty)
        {
            val visitInfo = queue.dequeue()

            when
            {
                visitInfo.explorable ->
                {
                    visitInfo.firstTime = false
                    queue.enqueue(visitInfo)
                    queue.enqueue(VisitInfo(visitInfo.min, visitInfo.middle - 1))
                    queue.enqueue(VisitInfo(visitInfo.middle + 1, visitInfo.max))
                }
                visitInfo.firstTime  ->
                {
                    if (!visitor(this.tree[visitInfo.min]))
                    {
                        return
                    }

                    if (visitInfo.min < visitInfo.max)
                    {
                        if (!visitor(this.tree[visitInfo.max]))
                        {
                            return
                        }
                    }
                }
                else                 ->
                    if (!visitor(this.tree[visitInfo.middle]))
                    {
                        return
                    }
            }
        }
    }

    /**
     * Collects elements from the tree with left to right algorithm order.
     *
     * A tree with : `A, B, C ,D, E, F`:
     *
     *         C
     *        / \
     *       /   \
     *      A     E
     *       \   / \
     *       B  D   F
     *
     * Nodes will be tests on the order : `C, A, B, E, D, F`
     *
     * @param condition Condition on element to be collected or not
     * @param collector Collector of matching elements
     */
    fun collectLeftRight(condition: (T) -> Boolean, collector: (T) -> Unit)
    {
        this.visitLeftRight(visitorCollector(condition, collector))
    }

    /**
     * Search an element from the tree with left to right algorithm order.
     *
     * A tree with : `A, B, C ,D, E, F`:
     *
     *         C
     *        / \
     *       /   \
     *      A     E
     *       \   / \
     *       B  D   F
     *
     * Will be searched on the order : `C, A, B, E, D, F`
     *
     * @return The first element meet that match the condition or `null` if not found
     */
    fun searchLeftRight(condition: (T) -> Boolean): T?
    {
        val atomicValue = AtomicReference<T>()
        this.visitLeftRight(visitorSearch(condition, atomicValue::set))
        return atomicValue.get()
    }

    /**
     * Visit the tree with left to right head algorithm.
     *
     * A tree with : `A, B, C ,D, E, F`:
     *
     *         C
     *        / \
     *       /   \
     *      A     E
     *       \   / \
     *       B  D   F
     *
     * Will be visit on the order : `C, A, E, B, D, F`
     *
     * The visitor returns a boolean to know if it wants to continue the visit or not
     */
    fun visitLeftRightHead(visitor: (T) -> Boolean)
    {
        val queue = Queue<VisitInfo>()
        queue.enqueue(VisitInfo(0, this.size - 1))

        while (queue.notEmpty)
        {
            val visitInfo = queue.dequeue()

            if (!visitor(this.tree[visitInfo.middle]))
            {
                return
            }

            if (visitInfo.explorable)
            {
                queue.enqueue(VisitInfo(visitInfo.min, visitInfo.middle - 1))
                queue.enqueue(VisitInfo(visitInfo.middle + 1, visitInfo.max))
            }
            else if (visitInfo.min < visitInfo.max)
            {
                queue.enqueue(VisitInfo(visitInfo.max, visitInfo.max))
            }
        }
    }

    /**
     * Collects elements from the tree with left to right head algorithm order.
     *
     * A tree with : `A, B, C ,D, E, F`:
     *
     *         C
     *        / \
     *       /   \
     *      A     E
     *       \   / \
     *       B  D   F
     *
     * Nodes will be tests on the order : `C, A, E, B, D, F`
     *
     * @param condition Condition on element to be collected or not
     * @param collector Collector of matching elements
     */
    fun collectLeftRightHead(condition: (T) -> Boolean, collector: (T) -> Unit)
    {
        this.visitLeftRightHead(visitorCollector(condition, collector))
    }

    /**
     * Search an element from the tree with left to right head algorithm order.
     *
     * A tree with : `A, B, C ,D, E, F`:
     *
     *         C
     *        / \
     *       /   \
     *      A     E
     *       \   / \
     *       B  D   F
     *
     * Will be searched on the order : `C, A, E, B, D, F`
     *
     * @return The first element meet that match the condition or `null` if not found
     */
    fun searchLeftRightHead(condition: (T) -> Boolean): T?
    {
        val atomicValue = AtomicReference<T>()
        this.visitLeftRightHead(visitorSearch(condition, atomicValue::set))
        return atomicValue.get()
    }

    /**
     * Visit the tree with right to left head algorithm.
     *
     * A tree with : `A, B, C ,D, E, F`:
     *
     *         C
     *        / \
     *       /   \
     *      A     E
     *       \   / \
     *       B  D   F
     *
     * Will be visit on the order : `C, E, A, F, D, B`
     *
     * The visitor returns a boolean to know if it wants to continue the visit or not
     */
    fun visitRightLeftHead(visitor: (T) -> Boolean)
    {
        val queue = Queue<VisitInfo>()
        queue.enqueue(VisitInfo(0, this.size - 1))

        while (queue.notEmpty)
        {
            val visitInfo = queue.dequeue()

            if (!visitor(this.tree[visitInfo.middle]))
            {
                return
            }

            if (visitInfo.explorable)
            {
                queue.enqueue(VisitInfo(visitInfo.middle + 1, visitInfo.max))
                queue.enqueue(VisitInfo(visitInfo.min, visitInfo.middle - 1))
            }
            else if (visitInfo.min < visitInfo.max)
            {
                queue.enqueue(VisitInfo(visitInfo.max, visitInfo.max))
            }
        }
    }

    /**
     * Collects elements from the tree with right to left head algorithm order.
     *
     * A tree with : `A, B, C ,D, E, F`:
     *
     *         C
     *        / \
     *       /   \
     *      A     E
     *       \   / \
     *       B  D   F
     *
     * Nodes will be tests on the order : `C, E, A, F, D, B`
     *
     * @param condition Condition on element to be collected or not
     * @param collector Collector of matching elements
     */
    fun collectRightLeftHead(condition: (T) -> Boolean, collector: (T) -> Unit)
    {
        this.visitRightLeftHead(visitorCollector(condition, collector))
    }

    /**
     * Search an element from the tree with right to left head algorithm order.
     *
     * A tree with : `A, B, C ,D, E, F`:
     *
     *         C
     *        / \
     *       /   \
     *      A     E
     *       \   / \
     *       B  D   F
     *
     * Will be searched on the order : `C, E, A, F, D, B`
     *
     * @return The first element meet that match the condition or `null` if not found
     */
    fun searchRightLeftHead(condition: (T) -> Boolean): T?
    {
        val atomicValue = AtomicReference<T>()
        this.visitRightLeftHead(visitorSearch(condition, atomicValue::set))
        return atomicValue.get()
    }

    fun removeIf(condition: (T) -> Boolean)
    {
        this.tree.removeIf(condition)
    }

    fun clear()
    {
        this.tree.clear()
    }

    fun headValue(): T =
        if (this.empty) throw IllegalStateException("The tree is empty")
        else this.tree[(this.size - 1) / 2]

    fun leftBranch(): BTree<T>
    {
        val size = this.tree.size
        val tree = BTree(this.comparator)
        val max = (size - 1) / 2

        for (index in 0 until max)
        {
            tree.tree.add(this.tree[index])
        }

        return tree
    }

    fun rightBranch(): BTree<T>
    {
        val size = this.tree.size
        val tree = BTree(this.comparator)
        val min = (size - 1) / 2 + 1

        for (index in min until size)
        {
            tree.tree.add(this.tree[index])
        }

        return tree
    }


    override fun toString(): String
    {
        if (this.tree.empty)
        {
            return "EMPTY"
        }

        val stringBuilder = StringBuilder()
            .append("\n")

        val queue = Queue<VisitInfo>()
        queue.enqueue(VisitInfo(0, this.size - 1, true, ""))

        while (queue.notEmpty)
        {
            val visitInfo = queue.dequeue()

            when
            {
                visitInfo.explorable ->
                {
                    visitInfo.firstTime = false
                    queue.enqueue(visitInfo)
                    val header =
                        if (visitInfo.info.isEmpty())
                        {
                            "\n|"
                        }
                        else
                        {
                            "${visitInfo.info} |"
                        }


                    queue.enqueue(VisitInfo(visitInfo.min, visitInfo.middle - 1, true, header))
                    queue.enqueue(VisitInfo(visitInfo.middle + 1, visitInfo.max, true, header))
                }
                visitInfo.firstTime  ->
                {
                    if (visitInfo.info.isNotEmpty())
                    {
                        stringBuilder
                            .append(visitInfo.info)
                            .append("-")
                    }

                    stringBuilder.append(this.tree[visitInfo.min])

                    if (visitInfo.min < visitInfo.max)
                    {
                        if (visitInfo.info.isEmpty())
                        {
                            stringBuilder.append("\n|-")
                        }
                        else
                        {
                            stringBuilder.append("${visitInfo.info} |-")
                        }


                        stringBuilder.append(this.tree[visitInfo.max])
                    }
                }
                else                 ->
                {
                    if (visitInfo.info.isNotEmpty())
                    {
                        stringBuilder
                            .append(visitInfo.info)
                            .append("-")
                    }

                    stringBuilder.append(this.tree[visitInfo.middle])
                }
            }
        }

        return stringBuilder.toString()
    }

    fun compact(): String
    {
        if (this.tree.empty)
        {
            return "{}"
        }

        val stringBuilder = StringBuilder()

        val queue = Queue<VisitInfo>()
        queue.enqueue(VisitInfo(0, this.size - 1, true, ""))

        while (queue.notEmpty)
        {
            val visitInfo = queue.dequeue()

            when
            {
                visitInfo.explorable ->
                {
                    visitInfo.firstTime = false
                    val oldInfo = visitInfo.info
                    visitInfo.info = "{"
                    queue.enqueue(visitInfo)
                    queue.enqueue(VisitInfo(visitInfo.min, visitInfo.middle - 1, true, ";"))
                    queue.enqueue(VisitInfo(visitInfo.middle + 1, visitInfo.max, true, "}$oldInfo"))
                }
                visitInfo.firstTime  ->
                {
                    stringBuilder.append(this.tree[visitInfo.min])

                    if (visitInfo.min < visitInfo.max)
                    {
                        stringBuilder
                            .append("{")
                            .append(this.tree[visitInfo.max])
                            .append("}")
                    }

                    stringBuilder.append(visitInfo.info)
                }
                else                 ->
                {
                    stringBuilder
                        .append(this.tree[visitInfo.middle])
                        .append(visitInfo.info)
                }
            }
        }

        return stringBuilder.toString()
    }
}