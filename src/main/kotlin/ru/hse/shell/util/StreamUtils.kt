package ru.hse.shell.util

import java.io.OutputStream

/*
 * Utility class to work with IO Streams.
 */
class StreamUtils {
    companion object {
        /*
         * Writes to output the given string with adding or not a newline at the end.
         */
        fun writeToStream(output: OutputStream, string: String?, addNewline: Boolean = true) {
            var message = string ?: return
            if (addNewline) {
                message += System.lineSeparator()
            }
            output.write(message.toByteArray())
        }
    }
}
