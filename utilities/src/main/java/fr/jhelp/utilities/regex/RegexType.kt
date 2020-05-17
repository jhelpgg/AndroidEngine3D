package fr.jhelp.utilities.regex

internal enum class RegexType
{
    TEXT,
    OR,
    GROUP,
    ZERO_OR_MORE,
    ZERO_OR_ONE,
    ONE_OR_MORE,
    EXACTLY,
    AT_LEAST,
    BETWEEN,
    CHARACTERS,
    SAME_AS,
    WHITE_SPACE,
    ANY,
    UNION
}