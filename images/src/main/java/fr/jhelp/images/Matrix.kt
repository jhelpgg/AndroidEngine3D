package fr.jhelp.images

import fr.jhelp.utilities.angle.AngleFloat

class Matrix
{
    var determinant = 0.0f
        private set

    /**
     * Matrix position 0,0
     */
    private var matrix00 = 0.0f

    /**
     * Matrix position 0,1
     */
    private var matrix01 = 0.0f

    /**
     * Matrix position 0,2
     */
    private var matrix02 = 0.0f

    /**
     * Matrix position 1,0
     */
    private var matrix10 = 0.0f

    /**
     * Matrix position 1,1
     */
    private var matrix11 = 0.0f

    /**
     * Matrix position 1,2
     */
    private var matrix12 = 0.0f

    /**
     * Matrix position 2,0
     */
    private var matrix20 = 0.0f

    /**
     * Matrix position 2,1
     */
    private var matrix21 = 0.0f

    /**
     * Matrix position 2,2
     */
    private var matrix22 = 0.0f

    init
    {
        this.toIdentity()
    }

    fun toIdentity()
    {
        this.matrix00 = 1.0f
        this.matrix10 = 0.0f
        this.matrix20 = 0.0f
        this.matrix01 = 0.0f
        this.matrix11 = 1.0f
        this.matrix21 = 0.0f
        this.matrix02 = 0.0f
        this.matrix12 = 0.0f
        this.matrix22 = 1.0f
        this.determinant = 1.0f
    }

    fun toRotate(centerX: Float, centerY: Float, angle: AngleFloat)
    {
        /**
         * <pre>
         * x=centerX;
         * y=centerY;
         * c=cos(angle)
         * s=sin(angle)
         *
         *     |1 0 -x| | c s 0| |1 0 x|
         * R = |0 1 -y|x|-s c 0|x|0 1 y|
         *     |0 0  1| | 0 0 1| |0 0 1|
         *
         *     | c s -x| |1 0 x|
         * R = |-s c -y|x|0 1 y|
         *     | 0 0  1| |0 0 1|
         *
         *     | c s cx+sy-x|
         * R = |-s c cy-sx-y|
         *     | 0 0    1   |
         *
         * </pre>
         */
        val cos = angle.cos()
        val sin = angle.sin()
        this.matrix00 = cos
        this.matrix10 = sin
        this.matrix20 = cos * centerX + sin * centerY - centerX
        this.matrix01 = -sin
        this.matrix11 = cos
        this.matrix21 = cos * centerY - sin * centerX - centerY
        this.matrix02 = 0.0f
        this.matrix12 = 0.0f
        this.matrix22 = 1.0f
        this.determinant = 1.0f
    }

    fun transform(x: Float, y: Float) = Point2D(
        (this.matrix00 * x) + (this.matrix10 * y) + this.matrix20,
        (this.matrix01 * x) + (this.matrix11 * y) + this.matrix21)

}

fun obtainRotateMatrix(centerX: Float, centerY: Float, angle: AngleFloat): Matrix
{
    val matrix = Matrix()
    matrix.toRotate(centerX, centerY, angle)
    return matrix
}