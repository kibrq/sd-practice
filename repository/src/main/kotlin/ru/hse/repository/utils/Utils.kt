package ru.hse.repository.utils

fun<T> withinTry(block: (Unit) -> T?): T? {
    return try {
        block.invoke(Unit)
    } catch (e: Exception) {
        null
    }
}
