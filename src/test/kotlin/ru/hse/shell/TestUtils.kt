package ru.hse.shell

import ru.hse.shell.util.IO
import java.io.ByteArrayOutputStream
import java.io.InputStream

class TestUtils {
    companion object {
        fun mockIO() = IO(
            inputStream = InputStream.nullInputStream(),
            outputStream = ByteArrayOutputStream(),
            errorStream = ByteArrayOutputStream()
        )
    }
}
