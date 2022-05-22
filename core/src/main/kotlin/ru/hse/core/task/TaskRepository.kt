package ru.hse.core.task

import org.springframework.stereotype.Repository

@Repository
class TaskRepository {
    fun uploadTask(prototype: Task.Prototype): Task = TODO()

    fun getTaskById(taskId: Long): Task = TODO()

    fun getAllTasks(): List<Task> = TODO()
}
