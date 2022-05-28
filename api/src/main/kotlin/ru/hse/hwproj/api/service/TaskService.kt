package ru.hse.hwproj.api.service

import org.springframework.context.annotation.ComponentScan
import org.springframework.stereotype.Service
import ru.hse.hwproj.common.repository.task.Task
import ru.hse.hwproj.common.repository.task.TaskPrototype
import ru.hse.hwproj.common.repository.task.TaskRepository

@Service
@ComponentScan("ru.hse.repository")
class TaskService(
    private val taskRepository: TaskRepository
) {
    fun uploadTask(prototype: TaskPrototype): Int? = taskRepository.upload(prototype)

    fun getTask(taskId: Int): Task? = taskRepository.getById(taskId)

    fun getAllTasks(): List<Task> = taskRepository.getAll()
}
