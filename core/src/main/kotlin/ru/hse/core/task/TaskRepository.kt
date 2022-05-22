package ru.hse.core.task

import org.springframework.stereotype.Component

@Component
class TaskRepository {
    fun uploadTask(prototype: Task.TaskPrototype): Task = TODO(prototype.toString())

    fun getTaskById(taskId: Long): Task = TODO(taskId.toString())

    fun getAllTasks(): List<Task> = TODO()
}
