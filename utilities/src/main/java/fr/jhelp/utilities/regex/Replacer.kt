package fr.jhelp.utilities.regex

class Replacer(val regexPart: RegexPart)
{
    private val replacement = StringBuilder()

    fun append(string: String): Replacer
    {
        this.replacement.append(string)
        return this
    }

    operator fun plus(string: String): Replacer =
        this.append(string)


    fun append(group: Group): Replacer
    {
        this.replacement.append(this.regexPart.groupName(group))
        return this
    }

    operator fun plus(group: Group): Replacer =
        this.append(group)

    fun replaceAll(text: String): String =
        this.regexPart.replaceAll(text, this.replacement.toString())
}