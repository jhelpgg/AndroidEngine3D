package fr.jhelp.engine.scene.geom

/**
 * Describes how UV are put in a box face.
 *
 * The face is covered by the texture.
 * @param minU Left U on face
 * @param maxU Right U on face
 * @param minV Up V on face
 * @param maxV Down V on face
 */
class FaceUV(val minU: Float = 0f, val maxU: Float = 1f, val minV: Float = 0f, val maxV: Float = 1f)