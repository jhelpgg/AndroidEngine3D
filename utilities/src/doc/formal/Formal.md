# Formal operations

1. [Presentation](#presentation)
2. [Actual mathematical function types](#actual-mathematical-function-types)
   1. [Constants](#constants)
   2. [Variables](#variables)
   3. [Unary minus](#unary-minus)
   4. [Addition](#addition)
   5. [Subtraction](#subtraction)
   6. [Multiplication](#multiplication)
   7. [Division](#division)
   8. [Cosinus](#cosinus)
   9. [Sinus](#sinus)
3. [Possible operations](#possible-operations)
   1. [Simplification](#simplification)
   2. [Replace](#replace)
   3. [Derivative](#derivative)
   4. [Collect variables](#collect-variables)

### Presentation

The class
[fr.jhelp.utilities.formal.MathFunction](../../main/java/fr/jhelp/utilities/formal/MathFunction.kt)
represents a mathematical function can be write in formal way.

To represents, by example, the function :

````
x + 3y - cos(t)
````

It could be write :

````kotlin
import fr.jhelp.utilities.formal.X
import fr.jhelp.utilities.formal.Y
import fr.jhelp.utilities.formal.T
import fr.jhelp.utilities.formal.times
import fr.jhelp.utilities.formal.plus
import fr.jhelp.utilities.formal.minus
import fr.jhelp.utilities.formal.cos

val f = X + 3 * Y - cos(T)
````

They are symbolic manipulation, by example, if you have :

````kotlin
import fr.jhelp.utilities.formal.X
import fr.jhelp.utilities.formal.Y
import fr.jhelp.utilities.formal.T
import fr.jhelp.utilities.formal.times
import fr.jhelp.utilities.formal.plus
import fr.jhelp.utilities.formal.minus

val f = X + 3 * Y 
val g = X + 2 * Y
val h = f - g 
println(f)
println(g)
println(h)
````

It will print :

````
X + 3.0 * Y
X + 2.0 * Y
(X + 3.0 * Y) - (X + 2.0 * Y)
````

Now its possible to have a reduced version of h by :

````kotlin
import fr.jhelp.utilities.formal.X
import fr.jhelp.utilities.formal.Y
import fr.jhelp.utilities.formal.T
import fr.jhelp.utilities.formal.times
import fr.jhelp.utilities.formal.plus
import fr.jhelp.utilities.formal.minus

val f = X + 3 * Y 
val g = X + 2 * Y
val h = (f - g).simplifyMax() 
println(f)
println(g)
println(h)
````

To obtain :

````
X + 3.0 * Y
X + 2.0 * Y
Y
````

If you are curious, its possible to spy the simplification steps:

````kotlin
import fr.jhelp.utilities.formal.X
import fr.jhelp.utilities.formal.Y
import fr.jhelp.utilities.formal.T
import fr.jhelp.utilities.formal.times
import fr.jhelp.utilities.formal.plus
import fr.jhelp.utilities.formal.minus

val f = X + 3 * Y
val g = X + 2 * Y
println(f)
println(g)
val h = (f - g).simplifyMax { function -> println("> $function") }
println(h)
````

To have

````
X + 3.0 * Y
X + 2.0 * Y
> (X + 3.0 * Y) - (X + 2.0 * Y)
> (3.0 * Y) - (2.0 * Y)
> 1.0 * Y
> Y
> Y
Y
````

### Actual mathematical function types

##### Constants

[Constant](../../main/java/fr/jhelp/utilities/formal/Constant.kt)
represents a number. Like above, lot of operation transform any number
to **Constant** in implicit way.  
It's possible to be explicit by :
* Create the object with the constructor
* Use the extension "const"

````kotlin
import fr.jhelp.utilities.formal.Constant
import fr.jhelp.utilities.formal.const
import kotlin.math.E
import kotlin.math.PI

val constant1 = Constant(PI)
val constant2 = E.const
````

The special constant **INVALID** represents an invalid constant. It can
be the result of simplification when do an indeterminate operation like
divide by zero.

##### Variables

[Variable](../../main/java/fr/jhelp/utilities/formal/Variable.kt)
represents a symbolic variable. Like **x** and **y** in `f(x, y) = x² - y²`

It exists predefined variables : **`X, Y, Z, T, A, alpha, epsilon`**

But any String that respects variable convention name, can represents a
variable, to convert it, two ways:
* Create **Variable** object
* Use **variable** extension

````kotlin
import fr.jhelp.utilities.formal.Variable
import fr.jhelp.utilities.formal.variable

val variable1 = Variable("delta")
val variable2 = "omega".variable
````

Name are case sensitive, so **Omega** is different from **omega**

Variable convention name :
1. Start with a letter
2. Follow by zero or more : letter, digit or underscore

##### Unary minus

[UnaryMinus](../../main/java/fr/jhelp/utilities/formal/UnaryMinus.kt)
represents the unary minus operation.

It takes the opposite, like in `f(x) = -x`

Its possible to create the object with constructor, but usually, it is
create with the operator :

````kotlin
import fr.jhelp.utilities.formal.X
import fr.jhelp.utilities.formal.unaryMinus

val f = -X
````

##### Addition

[Addition](../../main/java/fr/jhelp/utilities/formal/Addition.kt)
represents an addition between two functions

Its possible to create the object with constructor, but usually, it is
create with the operator :

````kotlin
import fr.jhelp.utilities.formal.X
import fr.jhelp.utilities.formal.Y
import fr.jhelp.utilities.formal.plus

val f = X + Y
````

##### Subtraction

[Subtraction](../../main/java/fr/jhelp/utilities/formal/Subtraction.kt)
represents a subtraction between two functions

Its possible to create the object with constructor, but usually, it is
create with the operator :

````kotlin
import fr.jhelp.utilities.formal.X
import fr.jhelp.utilities.formal.Y
import fr.jhelp.utilities.formal.minus

val f = X - Y
````

##### Multiplication

[Multiplication](../../main/java/fr/jhelp/utilities/formal/Multiplication.kt)
represents a multiplication between two functions

Its possible to create the object with constructor, but usually, it is
create with the operator :

````kotlin
import fr.jhelp.utilities.formal.X
import fr.jhelp.utilities.formal.Y
import fr.jhelp.utilities.formal.times

val f = X * Y
````

##### Division

[Division](../../main/java/fr/jhelp/utilities/formal/Division.kt)
represents a division between two functions

Its possible to create the object with constructor, but usually, it is
create with the operator :

````kotlin
import fr.jhelp.utilities.formal.X
import fr.jhelp.utilities.formal.Y
import fr.jhelp.utilities.formal.div

val f = X / Y
````

###### Cosinus

[Cosinus](../../main/java/fr/jhelp/utilities/formal/Cosinus.kt)
represents the cosinus function

Can be create with constructor or utilities method :

````kotlin
import fr.jhelp.utilities.formal.Cosinus
import fr.jhelp.utilities.formal.T
import fr.jhelp.utilities.formal.cos

val f1 = Cosinus(T)
val f2 = cos(T)
````

###### Sinus

[Sinus](../../main/java/fr/jhelp/utilities/formal/Sinus.kt) represents
the sinus function

Can be create with constructor or utilities method :

````kotlin
import fr.jhelp.utilities.formal.Sinus
import fr.jhelp.utilities.formal.T
import fr.jhelp.utilities.formal.sin

val f1 = Sinus(T)
val f2 = sin(T)
````

### Possible operations

##### Simplification

Like we said above, its possible to simplify a mathematical function
with **simplifyMax**

##### Replace

It's possible to replace a variable by an other function with
**replace** method:

````kotlin
import fr.jhelp.utilities.formal.X
import fr.jhelp.utilities.formal.Y
import fr.jhelp.utilities.formal.Z
import fr.jhelp.utilities.formal.plus
import fr.jhelp.utilities.formal.unaryMinus

        val f = X + Y + Z
        println("f = $f")
        val g = f.replace(X, -Y).simplifyMax { function -> println(" > $function") }
        println("g = $g")
        val h = f.replace(X, 1).replace(Y, 2).replace(Z, 3).simplifyMax { function -> println(" > $function") }
        println("h = $h")
````

Will print :

````
f = X + Y + Z
 > -Y + Y + Z
 > Z + (Y - Y)
 > 0.0 + Z
 > Z
 > Z
g = Z
 > 1.0 + 2.0 + 3.0
 > 3.0 + 3.0
 > 6.0
 > 6.0
h = 6.0
````

##### Derivative

It' possible to compute the derivative of a function from a variable :

By example compute derivative of `f(x,y) = 3x + 4y + 2xy` by X

````kotlin
import fr.jhelp.utilities.formal.X
import fr.jhelp.utilities.formal.Y
import fr.jhelp.utilities.formal.plus
import fr.jhelp.utilities.formal.times

        val f = 3 * X + 4 * Y + 2 * X * Y
        val fd = f.derivative(X).simplifyMax()
        println("$f -x-> $fd")
````

Will print :

````
3.0 * X + 4.0 * Y + 2.0 * X * Y -x-> 3.0 + 2.0 * Y
````

##### Collect variables

It's possible to get all variables used by a mathematical function:

````kotlin
import fr.jhelp.utilities.formal.T
import fr.jhelp.utilities.formal.Variable
import fr.jhelp.utilities.formal.X
import fr.jhelp.utilities.formal.Y
import fr.jhelp.utilities.formal.alpha
import fr.jhelp.utilities.formal.cos
import fr.jhelp.utilities.formal.div
import fr.jhelp.utilities.formal.epsilon
import fr.jhelp.utilities.formal.minus
import fr.jhelp.utilities.formal.plus
import fr.jhelp.utilities.formal.sin
import fr.jhelp.utilities.formal.times
import java.util.TreeSet
import kotlin.math.PI

        val f = 3 * X + 4 * Y + 2 * X * Y + cos(T) / sin(alpha - (PI * epsilon))
        val collector = TreeSet<Variable>()
        f.collectVariables(collector)

        for (variable in collector)
        {
            println(variable)
        }
````

Will print

````
alpha
epsilon
T
X
Y
````

