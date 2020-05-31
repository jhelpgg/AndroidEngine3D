/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.lists

/**Return always true*/
private val TRUE = { _: Any? -> true }

/**Return always false*/
private val FALSE = { _: Any? -> false }

/**
 * Condition that's accept all
 */
fun <T> alwaysTrue() = TRUE as ((T) -> Boolean)

/**
 * Condition that's accept none
 */
fun <T> alwaysFalse() = FALSE as ((T) -> Boolean)

/**
 * Reverse a condition
 */
val <T> ((T) -> Boolean).not
    get() =
        when (this)
        {
            TRUE  -> alwaysFalse()
            FALSE -> alwaysTrue()
            else  -> { element: T -> this(element).not() }
        }

/**
 * Create condition that's `true` only if `this` condition and given one are `true`
 */
infix fun <T> ((T) -> Boolean).and(other: (T) -> Boolean) =
    when (this)
    {
        TRUE  -> other
        FALSE -> alwaysFalse()
        else  ->
            when (other)
            {
                TRUE  -> this
                FALSE -> alwaysFalse()
                else  -> { element: T -> this(element) && other(element) }
            }
    }

/**
 * Create condition that's `true` if `this` condition or given one are `true`
 */
infix fun <T> ((T) -> Boolean).or(other: (T) -> Boolean) =
    when (this)
    {
        TRUE  -> alwaysTrue()
        FALSE -> other
        else  ->
            when (other)
            {
                TRUE  -> alwaysTrue()
                FALSE -> this
                else  -> { element: T -> this(element) || other(element) }
            }
    }

/**
 * Create condition that's `true` if `this` condition or given one are `true` but not the both
 */
infix fun <T> ((T) -> Boolean).xor(other: (T) -> Boolean) =
    when (this)
    {
        TRUE  -> other.not
        FALSE -> other
        else  ->
            when (other)
            {
                TRUE  -> this.not
                FALSE -> this
                else  -> { element: T -> (this(element) xor other(element)) }
            }
    }

/**
 * Create condition that's `true` only if `this` condition and given boolean are `true`
 */
infix fun <T> ((T) -> Boolean).and(other: Boolean) =
    if (other) this else alwaysFalse()

/**
 * Create condition that's `true` if `this` condition or given boolean are `true`
 */
infix fun <T> ((T) -> Boolean).or(other: Boolean) =
    if (other) alwaysTrue() else this

/**
 * Create condition that's `true` if `this` condition or given boolean are `true` butnot the both
 */
infix fun <T> ((T) -> Boolean).xor(other: Boolean) =
    if (other) this.not else this

/**
 * Create condition that's `true` only if `this` boolean and given condition one are `true`
 */
infix fun <T> Boolean.and(other: (T) -> Boolean) =
    if (this) other else alwaysFalse()

/**
 * Create condition that's `true` if `this` boolean or given condition one are `true`
 */
infix fun <T> Boolean.or(other: (T) -> Boolean) =
    if (this) alwaysTrue() else other

/**
 * Create condition that's `true` if `this` boolean or given condition are `true` but not the both
 */
infix fun <T> Boolean.xor(other: (T) -> Boolean) =
    if (this) other.not else other
