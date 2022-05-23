package ru.hse.hwproj.service

import org.springframework.context.annotation.ComponentScan
import org.springframework.stereotype.Service
import ru.hse.core.task.Task
import ru.hse.core.task.TaskPrototype
import ru.hse.core.task.TaskRepository

@ComponentScan(basePackages = ["ru.hse.core"])
@Service
class TaskService(
    private val taskRepository: TaskRepository
) {
    fun uploadTask(prototype: TaskPrototype): Boolean = taskRepository.upload(prototype)

    fun getTask(taskId: Long): Task? = taskRepository.getTaskById(taskId)

    fun getAllTasks(): List<Task> = taskRepository.getAll() as List<Task>
}
