# Scene organization

1. [Introduction](#introduction)
1. [Objects](#objects)
1. [Clone](#clone)
1. [Predefined objects](#predefined-objects)
    1. [Plane](plane/Plane.md)
    1. [Box](box/Box.md)
    1. [Sphere](sphere/Sphere.md)
    1. [Revolution](revolution/Revolution.md)
    1. [Field3D](field3D/Field3D.md)
    1. [Dice](dice/Dice.md)
    1. [Robot](robot/Robot.md)

### Introduction

A [fr.jhelp.engine.scene.Scene3D](../../main/java/fr/jhelp/engine/scene/Scene3D.kt) describes the 3D scene graph.
(Get it from [fr.jhelp.engine.view.View3D](../../main/java/fr/jhelp/engine/view/View3D.kt))

It contains a **`background`** color, that change the 3D background. by example :

````kotlin
import fr.jhelp.engine.scene.BLACK
// ...
scene3D.backgroundColor = BLACK
````

It can start an animation or stop it before it finished with **`play`** and **`stop`** methods.
More about animations in [Animations](../animations/Animations.md).

All 3D objects inherits from [fr.jhelp.engine.scene.Node3D](../../main/java/fr/jhelp/engine/scene/Node3D.kt).

Each node have a [fr.jhelp.engine.scene.Position3D](../../main/java/fr/jhelp/engine/scene/Position3D.kt)
to describes its location. The location is relative to the node's parent location. 

To be considered on scene, each node have to be attach (directly or indirectly) to scene's root.
The **`root`** filed of [Scene3D](../../main/java/fr/jhelp/engine/scene/Scene3D.kt) is the scene's root.

A node may have no consistence, they are here to put several nodes in same group.

### Objects

An [fr.jhelp.engine.scene.Object3D](../../main/java/fr/jhelp/engine/scene/Object3D.kt) is a [Node3D](../../main/java/fr/jhelp/engine/scene/Node3D.kt)
with a 3D appearance. It is composed of :
* A mesh: Set of triangle that describes the [Object3D](../../main/java/fr/jhelp/engine/scene/Object3D.kt) shape.
* A material : Color and texture applied to the object: See [Material](../Material/Material.md) and [Texture](../Material/Texture.md)

For closed object like a box, sphere, ... the inside is never see by the user. So render inside part
is useless, the **`doubleFace`** field specified if inside is render or not.  The value can be changed anytime.

An [Object3D](../../main/java/fr/jhelp/engine/scene/Object3D.kt) can be sealed or not. 
A sealed object take less memory but it's mesh can't change anymore.

Methods **`addTriangle`** and **`addSquare`** modify the mesh object (does nothing if object is sealed),
by add respectively 1 or 2 triangles.

Triangles in mesh are made of 3 vertex. A vertex is composed of :
* 3D coordinates (x, y, z) relative to object coordinate system
* UV (u,v) : coordinates in texture. Those coordinates are express in percent of texture width and height 
  to bo easily adapted to any texture size.
  
The special method **`showWire`** creates a special texture for the object that shows the objects triangles. 
Have to call it **before** add the object to the scene to avoid any trouble.

### Clone

[Object3D](../../main/java/fr/jhelp/engine/scene/Object3D.kt) can be heavy in memory, 
to able use the exact same shape several times, it is smarter to use a [fr.jhelp.engine.scene.Clone3D](../../main/java/fr/jhelp/engine/scene/Clone3D.kt)

Clones have their own material and position.

### Predefined objects

In package **`fr.jhelp.engine.scene.geom`** can find some predefined [Object3D](../../main/java/fr/jhelp/engine/scene/Object3D.kt):

* [fr.jhelp.engine.scene.geom.Plane](../../main/java/fr/jhelp/engine/scene/geom/Plane.kt) : Plane in 3D. 
  See [Plane](plane/Plane.md)
* [fr.jhelp.engine.scene.geom.Box](../../main/java/fr/jhelp/engine/scene/geom/Box.kt) : A box.
  See [Box](box/Box.md)
* [fr.jhelp.engine.scene.geom.Sphere](../../main/java/fr/jhelp/engine/scene/geom/Sphere.kt) : A sphere.
  See [Sphere](sphere/Sphere.md)
* [fr.jhelp.engine.scene.geom.Revolution](../../main/java/fr/jhelp/engine/scene/geom/Revolution.kt) : A revolution, that is to say a path turned around an axis.
  See [Revolution](revolution/Revolution.md)
* [fr.jhelp.engine.scene.geom.Field3D](../../main/java/fr/jhelp/engine/scene/geom/Field3D.kt) : A filed represents an equation Z = f(X, Y).
  See [Field3D](field3D/Field3D.md)

Their also more complex objects :
* [fr.jhelp.engine.scene.geom.dice.Dice](../../main/java/fr/jhelp/engine/scene/geom/dice/Dice.kt) : A textured dice with animation.
  See [Dice](dice/Dice.md)
* [fr.jhelp.engine.scene.geom.robot.Robot](../../main/java/fr/jhelp/engine/scene/geom/robot/Robot.kt) : A textured robot with animation.
  See [Robot](robot/Robot.md)



 