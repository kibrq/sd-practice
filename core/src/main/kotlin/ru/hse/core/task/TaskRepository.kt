package ru.hse.core.task

class TaskRepository {
    fun uploadTask(prototype: TaskPrototype) = true

    fun getAllTasks() = listOf<Task>()

    fun getTaskById(taskId: Long) = Task()
}
