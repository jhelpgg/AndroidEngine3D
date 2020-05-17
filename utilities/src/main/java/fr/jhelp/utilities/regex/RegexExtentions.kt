package fr.jhelp.utilities.regex

import fr.jhelp.utilities.text.BasicCharactersInterval
import fr.jhelp.utilities.text.CharactersInterval

/**
 * Transform a string to regex that capture exactly the string
 * @throws IllegalArgumentException If string is empty
 */
@Throws(IllegalArgumentException::class)
fun String.regexText(): RegexPart
{
    if (this.isEmpty())
    {
        throw IllegalArgumentException("text must not be empty")
    }

    return RegexPart(RegexText(this))
}

operator fun RegexPart.plus(text: String) =
    this + text.regexText()

/**
 * Capture a regex or an other
 */
infix fun RegexPart.OR(regexPart: RegexPart) =
    RegexPart(RegexOr(this.regexElement, regexPart.regexElement))

/**
 * Transform character interval to regex that accept the interval
 */
fun CharactersInterval.regexIn() = RegexPart(RegexCharacters(this))

/**
 * Transform character interval to regex that accept the interval
 */
fun BasicCharactersInterval.regexIn() = this.toCharactersInterval().regexIn()

/**
 * Transform character interval to regex that refuse the interval
 */
fun CharactersInterval.regexOut() = RegexPart(RegexNegateCharacters(this))

/**
 * Transform character interval to regex that refuse the interval
 */
fun BasicCharactersInterval.regexOut() = this.toCharactersInterval().regexOut()

/**
 * Transform character array to regex that accept the characters inside the array
 */
fun CharArray.regex(): RegexPart
{
    val charactersInterval = CharactersInterval()
    this.forEach { charactersInterval += it }
    return charactersInterval.regexIn()
}

/**
 * Transform character array to regex that refuse the characters inside the array
 */
fun CharArray.regexOut(): RegexPart
{
    val charactersInterval = CharactersInterval()
    this.forEach { charactersInterval += it }
    return charactersInterval.regexOut()
}

/**
 * Transform character to regex that accept the character
 */
fun Char.regex() = charArrayOf(this).regex()

/**
 * Transform character to regex that refuse the character
 */
fun Char.regexOut() = charArrayOf(this).regexOut()

/**
 * Transform a regex to a capture group
 */
fun RegexPart.group() = Group(RegexGroup(this.regexElement))

/**
 * Repeat regex zero or more time
 */
fun RegexPart.zeroOrMore() = RegexPart(RegexZeroOrMore(this.regexElement))

/**
 * Repeat regex zero or one time
 */
fun RegexPart.zeroOrOne() = RegexPart(RegexZeroOrOne(this.regexElement))

/**
 * Repeat regex one or more time
 */
fun RegexPart.oneOrMore() = RegexPart(RegexOneOrMore(this.regexElement))

/**
 * Repeat regex at least the given time
 * @throws IllegalArgumentException If specified repetition is negative
 */
@Throws(IllegalArgumentException::class)
fun RegexPart.atLeast(number: Int) =
    when
    {
        number < 0  -> throw IllegalArgumentException("number must be >= 0")
        number == 0 -> this.zeroOrMore()
        number == 1 -> this.oneOrMore()
        else        -> RegexPart(RegexAtLeast(this.regexElement, number))
    }

/**
 * Repeat regex exactly a number of time
 * @throws IllegalArgumentException If number of repetition is negative or zero
 */
@Throws(IllegalArgumentException::class)
fun RegexPart.exactly(number: Int) =
    when
    {
        number <= 0 -> throw IllegalArgumentException("number must be > 0")
        number == 1 -> this
        else        -> RegexPart(RegexExactly(this.regexElement, number))
    }

/**
 * Repeat regex a number of time inside an interval
 * @throws IllegalArgumentException If minimum is negative or maximum<minimum
 */
@Throws(IllegalArgumentException::class)
fun RegexPart.between(minimum: Int, maximum: Int) =
    when
    {
        minimum < 0                  -> throw IllegalArgumentException("minimum must be >= 0")
        minimum > maximum            ->
            throw IllegalArgumentException("minimum ($minimum) not <= maximum ($maximum)")
        minimum == 0 && maximum == 1 -> this.zeroOrOne()
        minimum == maximum           -> this.exactly(minimum)
        else                         -> RegexPart(RegexBetween(this.regexElement, minimum, maximum))
    }

/**
 * Permits to match exactly what is was match by the group
 */
fun Group.same() = RegexPart(RegexSameAs(this.regexElement as RegexGroup))

/**Match to white spaces*/
val WHITE_SPACE = RegexPart(RegexWhiteSpace)

/**Match to any characters*/
val ANY = RegexPart(RegexAny)

/**
 * Add two regex to match this and then given
 */
operator fun RegexPart.plus(regexPart: RegexPart): RegexPart
{
    val regexUnion = RegexUnion()
    regexUnion += this.regexElement
    regexUnion += regexPart.regexElement
    return RegexPart(regexUnion)
}