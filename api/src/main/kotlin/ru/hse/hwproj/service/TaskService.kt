package ru.hse.hwproj.service

import org.springframework.context.annotation.ComponentScan
import org.springframework.stereotype.Service
import ru.hse.repository.task.Task
import ru.hse.repository.task.TaskPrototype
import ru.hse.repository.task.TaskRepository

@Service
@ComponentScan("ru.hse.repository")
class TaskService(
    private val taskRepository: TaskRepository
) {
    fun uploadTask(prototype: TaskPrototype): Int? = taskRepository.upload(prototype)

    fun getTask(taskId: Long): Task? = taskRepository.getTaskById(taskId)

    fun getAllTasks(): List<Task> = taskRepository.getAll()
}
