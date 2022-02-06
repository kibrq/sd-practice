package ru.hse.shell.util

import java.io.InputStream
import java.io.OutputStream

/*
 * IO encapsulates input, output and error streams for a command or a statement.
 */
data class IO(val inputStream: InputStream, val outputStream: OutputStream, val errorStream: OutputStream)
