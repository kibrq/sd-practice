package ru.hse.hwproj.api.service

import org.springframework.context.annotation.ComponentScan
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import ru.hse.hwproj.api.utils.orElseStatus
import ru.hse.hwproj.common.repository.task.Task
import ru.hse.hwproj.common.repository.task.TaskPrototype
import ru.hse.hwproj.common.repository.task.TaskRepository

@Service
@ComponentScan("ru.hse.repository")
class TaskService(
    private val taskRepository: TaskRepository
) {
    fun uploadTask(prototype: TaskPrototype): ResponseEntity<Int> {
        return taskRepository.upload(prototype).orElseStatus(HttpStatus.NOT_FOUND)
    }

    fun getTask(id: Int): ResponseEntity<Task> {
        return taskRepository.getById(id).orElseStatus(HttpStatus.BAD_REQUEST)
    }

    fun getAllTasks(): List<Task> = taskRepository.getAll()
}
