package fr.jhelp.utilities.extensions

val StackTraceElement.description get() =
   "${this.className}.${this.methodName} at ${this.lineNumber}"

