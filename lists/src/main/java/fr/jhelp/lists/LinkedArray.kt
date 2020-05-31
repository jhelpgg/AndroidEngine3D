/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.lists

import java.util.Stack

private val EMPTY = LinkedArray<Any>()

/**
 * Obtain an empty linked array
 */
fun <T> emptyLinkedArray() = EMPTY as LinkedArray<T>

/**
 * Create a linked array of given elements
 */
fun <T> linkedArrayOf(vararg elements: T): LinkedArray<T>
{
    var array = emptyLinkedArray<T>()

    for (index in elements.size - 1 downTo 0)
    {
        array = elements[index] U array
    }

    return array
}

/**
 * Linked array, to do Camel like operations on array
 *
 * To have an instance use [emptyLinkedArray] or [linkedArrayOf]
 *
 * The [U] (for union) operation is the concatenation operation, by example :
 *
 * ````
 * a U [b, c, d]    ->  [a, b, c, d]
 * [b, c, d] U a    ->  [b, c, d, a]
 * [a, b] U [c, d]  ->  [a, b, c ,d]
 * ...
 * ````
 *
 */
class LinkedArray<T> private constructor(private val element: T?,
                                         private val queue: LinkedArray<T>?, val size: Int)
{
    /**
     * Indicates if linked array is empty
     */
    val empty = this.element == null

    /**
     * Indicates if linked array is not empty
     */
    val notEmpty = this.element != null

    internal constructor() : this(null, null, 0)

    /**
     * The head (first element) of the *linked array
     *
     * @throws NoSuchElementException if the linked array is empty
     */
    @Throws(NoSuchElementException::class)
    fun head(): T =
        this.element
        ?: throw NoSuchElementException("Array is empty")

    /**
     * Linked array queue.
     *
     * Queue is the linked array without its head
     *
     * Empty linked array have itself has queue
     */
    fun queue(): LinkedArray<T> =
        when
        {
            this.empty         -> this
            this.queue == null -> emptyLinkedArray()
            else               -> this.queue
        }

    /**
     * Create linked array with given element as head and this linked array as queue
     *
     * It exist the [U] notation for convenience :
     * ````Kotlin
     * val linkedArray = linkedArrayOf("of", "concatenation")
     * val array1 = linkedArray.aHead("Test")
     * val array2 = "Test" U linkedArray
     * ````
     *
     * `array1` and `array2` will both contains : `["Test", "of", "concatenation"]`
     */
    fun ahead(element: T): LinkedArray<T> =
        when
        {
            this.empty -> LinkedArray(element, null, 1)
            else       -> LinkedArray(element, this, this.size + 1)
        }

    /**
     * Create linked array represents this linked array concatenate with given element at the end
     *
     * ````Kotlin
     * val linkedArray = linkedArrayOf("An", "other", "concatenation")
     * val array = linkedArray U "test"
     * ````
     *
     * `array` will contains `["An", "other", "concatenation", "test"]
     */
    infix fun U(element: T): LinkedArray<T>
    {
        if (this.empty)
        {
            return LinkedArray(element, null, 1)
        }

        val stack = Stack<LinkedArray<T>>()
        var current = this

        while (current.queue != null)
        {
            stack.push(current)
            current = current.queue!!
        }

        current = LinkedArray(current.element, LinkedArray(element, null, 1), 2)

        while (stack.isNotEmpty())
        {
            current = LinkedArray(stack.pop().element, current, current.size + 1)
        }

        return current
    }

    /**
     * Create a linked array represents concatenation of this linked array and given one
     *
     * ````Kotlin
     * val array1 = linkedArrayOf("An", "other")
     * val array2 = linkedArrayOf("concatenation", "test")
     * val arrayResult = array1 U array2
     * ````
     *
     * `arrayResult` will contains : `["An", "other", "concatenation", "test"]`
     */
    infix fun U(array: LinkedArray<T>): LinkedArray<T>
    {
        if (this.empty)
        {
            return array
        }

        if (array.empty)
        {
            return this
        }

        val stack = Stack<LinkedArray<T>>()
        var current = this

        while (current.queue != null)
        {
            stack.push(current)
            current = current.queue!!
        }

        current = LinkedArray(current.element, array, 1 + array.size)

        while (stack.isNotEmpty())
        {
            current = LinkedArray(stack.pop().element, current, current.size + 1)
        }

        return current
    }

    /**
     * String representation
     */
    override fun toString(): String
    {
        if (this.empty)
        {
            return "[]"
        }

        val stringBuilder = StringBuilder().append("[")
        var afterFirst = false
        var current: LinkedArray<T>? = this

        while (current != null)
        {
            if (afterFirst)
            {
                stringBuilder.append(", ")
            }

            afterFirst = true
            stringBuilder.append(current.element)
            current = current.queue
        }

        return stringBuilder.append("]").toString()
    }

    override fun equals(other: Any?): Boolean
    {
        if (this === other)
        {
            return true
        }

        if (null == other || other !is LinkedArray<*> || this.size != other.size)
        {
            return false
        }

        var thisCurrent: LinkedArray<T>? = this
        var otherCurrent: LinkedArray<*>? = other

        while (thisCurrent != null && otherCurrent != null)
        {
            if (thisCurrent.element != otherCurrent.element)
            {
                return false
            }

            thisCurrent = thisCurrent.queue
            otherCurrent = otherCurrent.queue
        }

        return true
    }

    override fun hashCode(): Int
    {
        if (this.empty)
        {
            return 0
        }

        var hash = 0
        var current: LinkedArray<T>? = this

        while (current != null)
        {
            hash = hash * 31 + current.element.hashCode()
            current = current.queue
        }

        return hash
    }
}

/**
 * Create linked array with given element as head and this linked array as queue
 *
 * ````Kotlin
 * val linkedArray = linkedArrayOf("of", "concatenation")
 * val array = "Test" U linkedArray
 * ````
 *
 * `array` will contains : `["Test", "of", "concatenation"]`
 */
infix fun <T> T.U(array: LinkedArray<T>) = array.ahead(this)
