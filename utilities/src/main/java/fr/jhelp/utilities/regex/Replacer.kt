package fr.jhelp.utilities.regex

/**
 * Replacer for replace matching text in regular expression
 */
class Replacer internal constructor(val regexPart: RegexPart)
{
    private val replacement = StringBuilder()

    /**
     * Just append text in replacement
     */
    fun append(string: String): Replacer
    {
        this.replacement.append(string)
        return this
    }

    /**
     * Just append text in replacement
     */
    operator fun plus(string: String): Replacer =
        this.append(string)

    /**
     * Append what is capture by group in replacement.
     *
     * The group must be inside the regular expression that creates this replacer
     */
    fun append(group: Group): Replacer
    {
        this.replacement.append(this.regexPart.groupName(group))
        return this
    }

    /**
     * Append what is capture by group in replacement.
     *
     * The group must be inside the regular expression that creates this replacer
     */
    operator fun plus(group: Group): Replacer =
        this.append(group)

    /**
     * Do replacement for given String
     *
     * Example:
     *
     * ````Kotlin
     *  val group = ')'.regexOut().zeroOrMore().group()
     *  val regex = '('.regex() + group + ')'.regex()
     *  val replacer = regex.replacer() + "{" + group + "}"
     *  val result = replacer.replaceAll(text)
     * ````
     *In the given example, if `text` is **`"hello (85, call) something, (bob)"`** then `result` will be :
     * **`"hello {85, call} something, {bob}"`**
     */
    fun replaceAll(text: String): String =
        this.regexPart.replaceAll(text, this.replacement.toString())
}