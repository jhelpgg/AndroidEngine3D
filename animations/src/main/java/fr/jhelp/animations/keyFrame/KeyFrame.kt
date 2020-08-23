/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.animations.keyFrame

import fr.jhelp.animations.interpoolation.Interpolation

/**
 * Key frame description
 * @param frame Frame to play the key frame
 * @param value Object value at frame position
 * @param interpolation Interpolation to use for go to the frame
 * @param V Object value type
 */
internal class KeyFrame<V>(val frame: Int, var value: V, var interpolation: Interpolation) :
    Comparable<KeyFrame<V>>
{
    /**
     * Compare with an other key frame.
     *
     *    +--------------+----------------+
     *    |  Comparison  |     Result     |
     *    +--------------+----------------+
     *    | this < other | Negative value |
     *    | this = other | Zero value     |
     *    | this > other | Positive value |
     *    +--------------+----------------+
     *
     * @param other Key frame to compare with
     * @return Comparison result
     */
    override fun compareTo(other: KeyFrame<V>) = this.frame - other.frame

    /**
     * Modify the value and interpolation linked to the frame
     * @param value New value
     * @param interpolation New interpolation
     */
    fun set(value: V, interpolation: Interpolation)
    {
        this.value = value
        this.interpolation = interpolation
    }
}
