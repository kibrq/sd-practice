package ru.hse.shell.util

import java.io.InputStream
import java.io.OutputStream

data class IO(val inputStream: InputStream, val outputStream: OutputStream, val errorStream: OutputStream)
