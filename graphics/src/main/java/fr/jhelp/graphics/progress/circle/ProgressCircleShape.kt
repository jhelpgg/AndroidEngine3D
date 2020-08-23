/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.graphics.progress.circle

enum class ProgressCircleShape(val value: Int)
{
    FULL_CIRCLE(0),
    RING(1),
    HALF_RING(2)
}

val Int.progressCircleShape: ProgressCircleShape
    get()
    {
        for (progressCircleShape in ProgressCircleShape.values())
        {
            if (this == progressCircleShape.value)
            {
                return progressCircleShape
            }
        }

        return ProgressCircleShape.HALF_RING
    }