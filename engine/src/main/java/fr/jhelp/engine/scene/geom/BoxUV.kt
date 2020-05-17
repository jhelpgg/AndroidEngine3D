package fr.jhelp.engine.scene.geom

/**
 * Describes how UV are put on each box face.
 *
 * By default texture is repeat on each face.
 * @param face Describes how UV are put on face
 * @param back Describes how UV are put on back
 * @param top Describes how UV are put on top
 * @param bottom Describes how UV are put on bottom
 * @param left Describes how UV are put on left
 * @param right Describes how UV are put on right
 */
open class BoxUV(val face: FaceUV = FaceUV(), val back: FaceUV = FaceUV(),
                 val top: FaceUV = FaceUV(), val bottom: FaceUV = FaceUV(),
                 val left: FaceUV = FaceUV(), val right: FaceUV = FaceUV())