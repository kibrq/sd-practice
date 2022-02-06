package ru.hse.shell

import ru.hse.shell.util.IO
import java.io.ByteArrayOutputStream
import java.io.InputStream

/*
 * Utility class for writing tests.
 */
class TestUtils {
    companion object {
        /*
         * Returns a mock IO with a null inputStream and ByteArray output/error streams.
         */
        fun mockIO() = IO(
            inputStream = InputStream.nullInputStream(),
            outputStream = ByteArrayOutputStream(),
            errorStream = ByteArrayOutputStream()
        )
    }
}
