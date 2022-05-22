package ru.hse.runner

fun main() {
    repeat(4) {
        CheckerService().receiveTasks()
    }
}
