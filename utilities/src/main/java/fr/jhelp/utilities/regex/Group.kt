package fr.jhelp.utilities.regex

/**
 * Represents a capturing group
 */
class Group internal constructor(regexGroup: RegexGroup) : RegexPart(regexGroup)
{
   val id get() = this.regexElement.count
}