package fr.jhelp.tasks.strand

import fr.jhelp.tasks.IndependentThread
import fr.jhelp.tasks.ThreadType


class Strand<I>(interfaceType: Class<I>, instance: I, threadType: ThreadType = IndependentThread)
{
    private val caller = strandCaller(interfaceType, instance, threadType)

    operator fun invoke(): I = this.caller
}