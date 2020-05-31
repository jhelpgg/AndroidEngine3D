/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.lists

/**
 * Queue of elements
 */
class Queue<T> : Iterable<T>
{
    private var head: QueueElement<T>? = null
    private var tail: QueueElement<T>? = null
    var size = 0
        private set
    val empty get() = this.head == null
    val notEmpty get() = this.head != null

    @Throws(IllegalStateException::class)
    private fun checkNotEmpty()
    {
        if (this.head == null)
        {
            throw IllegalStateException("The queue is empty")
        }
    }

    /**
     * Enqueue element at the end on the queue
     */
    fun enqueue(element: T)
    {
        if (this.head == null)
        {
            this.head = QueueElement(element)
            this.tail = this.head
            this.size = 1
            return
        }

        val tail = this.tail!!
        tail.next = QueueElement(element)
        this.tail = tail.next
        this.size++
    }

    /**
     * Get first element on the queue
     *
     * @throws IllegalStateException If the queue is empty
     */
    @Throws(IllegalStateException::class)
    fun peek(): T
    {
        this.checkNotEmpty()
        return this.head!!.element
    }

    /**
     * Get the first element of the que and remove it form the queue
     *
     * @throws IllegalStateException If the queue is empty
     */
    @Throws(IllegalStateException::class)
    fun dequeue(): T
    {
        this.checkNotEmpty()
        val head = this.head!!
        val value = head.element
        this.head = head.next
        this.size--

        if (this.head == null)
        {
            this.tail = null
        }

        return value
    }

    /**
     * Insert element as first element of the queue
     */
    fun ahead(element: T)
    {
        if (this.head == null)
        {
            this.head = QueueElement(element)
            this.tail = this.head
            this.size = 1
            return
        }

        this.head = QueueElement(element, this.head)
        this.size++
    }

    /**
     * String representation
     */
    override fun toString(): String
    {
        val stringBuilder = StringBuilder()
        stringBuilder.append('[')
        var afterFirst = false
        var current = this.head

        while (current != null)
        {
            if (afterFirst)
            {
                stringBuilder.append(", ")
            }

            afterFirst = true
            stringBuilder.append(current.element)
            current = current.next
        }

        stringBuilder.append(']')
        return stringBuilder.toString()
    }

    /**
     * Remove all elements that matches the given condition
     */
    fun removeIf(condition: (T) -> Boolean)
    {
        var current = this.head

        while (current != null && condition(current.element))
        {
            this.head = current.next
            this.size--
            current = current.next
        }

        if (current == null)
        {
            this.tail = null
            return
        }

        var parent: QueueElement<T> = current
        current = current.next

        while (current != null)
        {
            if (condition(current.element))
            {
                parent.next = current.next
                this.size--
            }
            else
            {
                parent = current
            }

            current = current.next
        }
    }

    /**
     * Iterator on elements queue
     */
    override fun iterator(): Iterator<T> = QueueIterator(this.head)

    /**
     * Search and return the first element that matches the condition
     *
     * May return `null` if no matching element
     */
    fun search(condition: (T) -> Boolean): T?
    {
        var current = this.head

        while (current != null)
        {
            if (condition(current.element))
            {
                return current.element
            }

            current = current.next
        }

        return null
    }

    /**
     * Clear the queue
     */
    fun clear()
    {
        this.head = null
        this.tail = null
        this.size = 0
    }
}