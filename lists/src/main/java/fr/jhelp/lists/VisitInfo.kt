package fr.jhelp.lists

internal class VisitInfo(val min: Int, val max: Int, var firstTime: Boolean = true, var info:String="")
{
    val middle = (this.min + this.max) / 2
    val explorable get() = this.firstTime && (this.min + 1 < this.max)
}