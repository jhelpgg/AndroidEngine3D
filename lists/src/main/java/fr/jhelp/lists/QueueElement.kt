package fr.jhelp.lists

internal class QueueElement<T>(val element: T, var next: QueueElement<T>?=null)
