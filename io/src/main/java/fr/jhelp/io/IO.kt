/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.io

import fr.jhelp.utilities.extensions.ifNotNull
import fr.jhelp.utilities.logError
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.min

/**
 * Copy data of an [InputStream] to an [OutputStream]
 */
fun InputStream.write(outputStream: OutputStream)
{
    val buffer = ByteArray(4096)
    var read = this.read(buffer)

    while (read >= 0)
    {
        outputStream.write(buffer, 0, read)
        read = this.read(buffer)
    }

    outputStream.flush()
}

/**
 * Read data form [InputStream] and assure to fill the given array as many bytes as possible
 *
 * @param array Array to fill
 * @param offset Offset to start write inside the array.
 *               If not given it takes the array's first element
 * @param length Number maximum bytes to read (This is the number try to achieve).
 *               If not given, it is set to go from the offset to the end of the array
 * @return Number of data effectively read
 */
fun InputStream.readFull(array: ByteArray, offset: Int = 0, length: Int = array.size - offset): Int
{
    var offsetLocal = offset
    var left = min(array.size - offsetLocal, length)

    if (left <= 0)
    {
        return 0
    }

    var total = 0

    var read = this.read(array, offsetLocal, left)

    if (read < 0)
    {
        return -1
    }

    offsetLocal += read
    total += read
    left -= read

    while (read >= 0 && left > 0)
    {
        read = this.read(array, offsetLocal, left)

        if (read >= 0)
        {
            offsetLocal += read
            total += read
            left -= read
        }
    }

    return total
}

/**
 * Manage properly an input and an output streams, to simplify the open, close and error management
 * @param producerInput Function that create the input stream
 * @param producerOutput Function that create the output stream
 * @param operation Operation to do with input and output streams
 * @param onError Called if error happen
 * @param I Input stream type
 * @param O Output stream type
 * @return **`true`** If complete operation succeed without exception
 */
inline fun <I : InputStream, O : OutputStream> treatInputOutputStream(producerInput: () -> I,
                                                                      producerOutput: () -> O,
                                                                      operation: (I, O) -> Unit,
                                                                      onError: (IOException) -> Unit = {
                                                                          logError(it)
                                                                          { "Issue on treat input/output streams!" }
                                                                      }): Boolean
{
    var ioException: IOException? = null
    var inputStream: I? = null
    var outputStream: O? = null

    try
    {
        inputStream = producerInput()
        outputStream = producerOutput()
        operation(inputStream, outputStream)
        outputStream.flush()
    }
    catch (io: IOException)
    {
        ioException = io
    }
    catch (e: Exception)
    {
        ioException = IOException("Failed to do operation!", e)
    }
    finally
    {
        if (outputStream != null)
        {
            try
            {
                outputStream.close()
            }
            catch (ignored: Exception)
            {
            }

        }

        if (inputStream != null)
        {
            try
            {
                inputStream.close()
            }
            catch (ignored: Exception)
            {
            }

        }
    }

    if (ioException != null)
    {
        onError(ioException)
        return false
    }

    return true
}

/**
 * Manage properly an input stream, to simplify the open, close and error management
 * @param producer Function that create the input stream
 * @param operation Operation to do with input stream
 * @param onError Called if error happen
 * @param I Input stream type
 * @return **`true`** If complete operation succeed without exception
 */
inline fun <I : InputStream> treatInputStream(producer: () -> I,
                                              operation: (I) -> Unit,
                                              onError: (IOException) -> Unit = {
                                                  logError(it) { "Failed to treat input stream!" }
                                              }): Boolean
{
    var ioException: IOException? = null
    var inputStream: I? = null

    try
    {
        inputStream = producer()
        operation(inputStream)
    }
    catch (io: IOException)
    {
        ioException = io
    }
    catch (e: Exception)
    {
        ioException = IOException("Failed to do operation!", e)
    }
    finally
    {
        if (inputStream != null)
        {
            try
            {
                inputStream.close()
            }
            catch (ignored: Exception)
            {
            }

        }
    }

    if (ioException != null)
    {
        onError(ioException)
        return false
    }

    return true
}

/**
 * Manage properly an output streams to simplify the open, close and error management
 * @param producer Function that create the output stream
 * @param operation Operation to do with output stream
 * @param onError Called if error happen
 * @param O Output stream type
 * @return **`true`** If complete operation succeed without exception
 */
inline fun <O : OutputStream> treatOutputStream(producer: () -> O,
                                                operation: (O) -> Unit,
                                                onError: (IOException) -> Unit = {
                                                    logError(it) { "Failed to treat output stream" }
                                                }): Boolean
{
    var ioException: IOException? = null
    var outputStream: O? = null

    try
    {
        outputStream = producer()
        operation(outputStream)
        outputStream.flush()
    }
    catch (io: IOException)
    {
        ioException = io
    }
    catch (e: Exception)
    {
        ioException = IOException("Failed to do operation!", e)
    }
    finally
    {
        if (outputStream != null)
        {
            try
            {
                outputStream.close()
            }
            catch (ignored: Exception)
            {
            }

        }
    }

    if (ioException != null)
    {
        onError(ioException)
        return false
    }

    return true
}

/**
 * Parse from stream an object.
 *
 * Simplify stream and exception management
 * @param producer Create the input stream
 * @param parser Parser to apply to the stream (Should throw [IOException] on read issue or stream not valid for parsed type)
 * @param I Input stream type
 * @param T Parsed object type
 * @return Parsed object
 * @throws IOException On reading issue or stream not valid for parsed type
 */
@Throws(IOException::class)
inline fun <I : InputStream, T> parseFromStream(producer: () -> I, parser: (I) -> T): T
{
    val result = AtomicReference<T>()
    val exception = AtomicReference<IOException>()
    treatInputStream(producer,
                     { result.set(parser(it)) },
                     { exception.set(it) })
    exception.ifNotNull { throw it }
    return result.get()
}

/**
 * Parse from stream an object.
 *
 * Don't have to manage try/catch
 * @param producer Create the input stream
 * @param parser Parser to apply to the stream (Should throw [IOException] on read issue or stream not valid for parsed type)
 * @param defaultValue Default value return on reading issue or if stream not valid
 * @param I Input stream type
 * @param T Parsed object type
 * @return Parsed object or default value on issue
 */
inline fun <I : InputStream, T> parseFromStream(producer: () -> I,
                                                parser: (I) -> T,
                                                defaultValue: T) =
    try
    {
        parseFromStream(producer, parser)
    }
    catch (exception: IOException)
    {
        defaultValue
    }

/**
 * Read text lines in given stream
 * @param producerInput Function that create the stream to read
 * @param lineReader Called on each line read. The parameter is the read line
 * @param onError Action to do on error
 * @param I Input stream type
 * @return **`true`** If complete operation succeed without exception
 */
inline fun <I : InputStream> readLines(producerInput: () -> I,
                                       lineReader: (String) -> Unit,
                                       onError: (IOException) -> Unit = {
                                           logError(it) { "Failed to read lines!!" }
                                       }) =
    treatInputStream(producerInput,
                     { inputStream ->
                         val bufferedReader = BufferedReader(InputStreamReader(inputStream,
                                                                               "UTF-8"),
                                                             4096);
                         var line = bufferedReader.readLine()

                         while (line != null)
                         {
                             lineReader(line)
                             line = bufferedReader.readLine()
                         }

                         bufferedReader.close();
                     }, onError)