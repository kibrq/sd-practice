package ru.hse.hwproj.common.repository.task

interface TaskRepository {
    fun upload(prototype: TaskPrototype): Int?

    fun getByIds(ids: List<Int>): List<Task>

    fun getById(id: Int): Task? {
        return getByIds(listOf(id)).getOrNull(0)
    }

    fun getAll(): List<Task>
}
