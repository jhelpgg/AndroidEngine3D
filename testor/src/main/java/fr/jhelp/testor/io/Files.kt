/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.testor.io

import java.io.File
import java.io.IOException
import java.util.Stack

internal val rootSource: File = File(File("").absolutePath).createDirectory()

internal val referenceDirectory: File =File(rootSource, "testorFiles").createDirectory()

val privateDirectory: File = File(referenceDirectory, "private").createDirectory()

val publicDirectory: File = File(referenceDirectory, "public").createDirectory()

fun databaseDirectory(databaseName: String): File =
    File(privateDirectory, "databases/$databaseName").createDirectory()

@Throws(IOException::class)
fun File.createDirectory(): File
{
    if (this.exists())
    {
        if (!this.isDirectory)
        {
            throw IOException("The file '${this.absolutePath}' is not a directory")
        }

        return this
    }

    if (!this.mkdirs())
    {
        throw IOException("Can't create directory '${this.absolutePath}'")
    }

    return this
}

@Throws(IOException::class)
fun File.createFile(): File
{
    if (this.exists())
    {
        if (!this.isFile)
        {
            throw IOException("The file '${this.absolutePath}' is not a file")
        }

        return this
    }

    this.parentFile.createDirectory()

    if (!this.createNewFile())
    {
        throw IOException("Can't create file '${this.absolutePath}'")
    }

    return this
}

fun File.deleteFull(): Boolean
{
    if (!this.exists())
    {
        return true
    }

    val stack = Stack<File>()
    stack.push(this)

    while (stack.isNotEmpty())
    {
        val file = stack.pop()

        if (file.isDirectory)
        {
            val children = file.listFiles()

            if (children == null || children.isEmpty())
            {
                if (!file.delete())
                {
                    return false
                }
            }
            else
            {
                stack.push(file)

                for (child in children)
                {
                    stack.push(child)
                }
            }
        }
        else if (!file.delete())
        {
            return false
        }
    }

    return true
}