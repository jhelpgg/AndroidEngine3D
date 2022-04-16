/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.io

import java.io.File
import java.util.Stack

fun File.createDirectory(): Boolean
{
    if (this.exists())
    {
        return this.isDirectory
    }

    return this.mkdirs()
}

fun File.createFile(): Boolean
{
    if (this.exists())
    {
        return this.isFile
    }

    if (!this.parentFile!!.createDirectory())
    {
        return false
    }

    return try
    {
        this.createNewFile()
    }
    catch (_: Exception)
    {
        false
    }
}

fun File.deleteFull(): Boolean
{
    if (!this.exists())
    {
        return true
    }

    val fileStack = Stack<File>()
    fileStack.push(this)

    while (fileStack.isNotEmpty())
    {
        val file = fileStack.pop()

        if (file.isDirectory)
        {
            val children = file.listFiles()

            if (children != null && children.isNotEmpty())
            {
                fileStack.push(file)

                for (child in children)
                {
                    fileStack.push(child)
                }
            }
            else
            {
                try
                {
                    if (!this.delete())
                    {
                        this.deleteOnExit()
                        return false
                    }
                }
                catch (_: Exception)
                {
                    this.deleteOnExit()
                    return false
                }
            }
        }
        else
        {
            try
            {
                if (!this.delete())
                {
                    this.deleteOnExit()
                    return false
                }
            }
            catch (_: Exception)
            {
                this.deleteOnExit()
                return false
            }
        }
    }

    return true
}
