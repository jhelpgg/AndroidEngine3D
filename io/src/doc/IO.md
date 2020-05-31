# Input/Output tools

Here some tools for simplify Input/Output tasks

1. [Read/write tools](#readwrite-tools)
2. [StreamArray](#streamarray)

### Read/write tools

In [IO](../main/java/fr/jhelp/io/IO.kt) their tools for simplify InputStream read, OutputStream write.

The `write` extension on `InputStream`, copy all data of the `InputStream`  
to given `OutputStream`. The method no close the streams

The `readFull` extension on `InputStream`, assure to fill the given byte array as many bytes as possible.
Some type of streams, needs sometimes to be read several time to fill a buffer,
the method assure to it just the right number of times to fill the buffer, or reach the end of the stream.

The `treatInputOutputStream` method will do operation on an `InputStream`
and an `OutputStream`. The method takes care to call the producers to create the streams,
and manage the properly the close and error management. Once open,
the operation is invoked with streams.

The `treatInputStream` method will do operation on an `InputStream`.
The method takes care to call the producer to create the stream,
and manage the properly the close and error management. Once open,
the operation is invoked with the stream.

The `treatOutputStream` method will do operation on an `OutputStream`.
The method takes care to call the producer to create the stream,
and manage the properly the close and error management. Once open,
the operation is invoked with the stream.

The `parseFromStream` read a stream given by the producer and use the parser
to create an object. The method takes care to call the producer to create the stream,
and manage the properly the close and error management. Once open, stream is given to the parser
to create the object

The `readLines` read a stream as a text, and callback a line reader on each line read.
The method takes care to call the producer to create the stream,
and manage the properly the close and error management.
Once open, stream is read as a text, and each line are given to the line reader.

### StreamArray

[StreamArray](../main/java/fr/jhelp/io/StreamArray.kt) is a dynamic byte array, with associated
`InputStream` (for read on it) and `OutputStream` (for write on it).

