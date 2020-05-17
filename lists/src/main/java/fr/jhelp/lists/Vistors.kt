package fr.jhelp.lists

fun <T> visitorCollector(condition: (T) -> Boolean, collector: (T) -> Unit) =
    { element: T ->
        if (condition(element))
        {
            collector(element)
        }

        true
    }

fun <T> visitorSearch(condition: (T) -> Boolean, collector: (T) -> Unit) =
    { element: T ->
        if (condition(element))
        {
            collector(element)
            false
        }
        else
        {
            true
        }
    }