package ru.hse.shell.util

import java.io.OutputStream

class StreamUtils {
    companion object {
        fun writeToStream(output: OutputStream, string: String?, addNewline: Boolean = true) {
            var message = string ?: return
            if (addNewline) {
                message += System.lineSeparator()
            }
            output.write(message.toByteArray())
        }
    }
}
