# Textures

[fr.jhelp.engine.scene.Texture](../../main/java/fr/jhelp/engine/scene/Texture.kt) represents an image
apply on a object via [fr.jhelp.engine.scene.Material](../../main/java/fr/jhelp/engine/scene/Material.kt).

Resources used to creates one [Texture](../../main/java/fr/jhelp/engine/scene/Texture.kt) can be different.
Like application resources, input stream, bitmap, or empty.

Texture width and height are always a power of 2 form 1 to 512 (1, 2, 4, 8, 16, 32, 64, 128, 256 or 512).
If the source have not to the good sizes, it will be scaled to nearest one.

A texture can be sealed or not. A sealed texture take less memory, but can't be modified.

For not sealed texture, its possible to modify them with methods : 
* `bitmap` : give the texture embed bitmap. `null` if the texture is sealed.
* `canvas` : give a canvas for draw on texture. `null` if the texture is sealed.
* `paint` : give a paint for draw on texture. `null` if the texture is sealed.

When a texture changed, have to call `refresh` to see the last modifications.

The extension `draw` (In [DSL](../../main/java/fr/jhelp/engine/DSL.kt)) call the given function with
the `bitmap`, `canvas` and `paint` to use for draw. The function is called only if texture not `null`
and not sealed.
