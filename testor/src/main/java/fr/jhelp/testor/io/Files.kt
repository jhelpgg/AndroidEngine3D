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

/**
 * The tested project root directory
 */
internal val rootSource: File = File(File("").absolutePath).createDirectory()

/**
 * Reference directory where put files during the test
 */
internal val referenceDirectory: File = File(rootSource, "testorFiles").createDirectory()

/**
 * Emulated application private directory
 */
val privateDirectory: File = File(referenceDirectory, "private").createDirectory()

/**
 * Emulates device public shared directory
 */
val publicDirectory: File = File(referenceDirectory, "public").createDirectory()

/**
 * Create if necessary and get directory for a database
 */
fun databaseDirectory(databaseName: String): File =
    File(privateDirectory, "databases/$databaseName").createDirectory()

/**
 * Create a directory and its parent if necessary
 *
 * @return Created directory
 * @throws IOException if already exists and not a directory or if creation failed
 */
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

/**
 * Create a file and its parent if necessary
 *
 * @return Created file
 * @throws IOException if already exists and not a file or if creation failed
 */
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

/**
 * Delete a file. If file denotes a directory, all the hierarchy is deleted
 *
 * @return `true` if complete deletion succeed. `false` indicates a failure. If it was a directory, some elements in the hierarchy may be deleted
 */
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