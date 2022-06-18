package ru.hse.hwproj.common.repository.utils

fun <T> withinTry(block: () -> T?): T? {
    return try {
        block()
    } catch (e: Exception) {
        null
    }
}
