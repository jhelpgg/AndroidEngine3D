package fr.jhelp.lists

class NaturalComparator<C : Comparable<C>> : Comparator<C>
{
    override fun compare(comparable1: C, comparable2: C): Int =
        comparable1.compareTo(comparable2)
}