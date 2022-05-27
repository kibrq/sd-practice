package ru.hse.hwproj.controller

import org.springframework.web.bind.annotation.*
import ru.hse.hwproj.service.SubmissionService
import ru.hse.hwproj.service.TaskService
import ru.hse.repository.submission.Submission
import ru.hse.repository.submission.SubmissionPrototype
import ru.hse.repository.submission.SubmissionView
import ru.hse.repository.task.Task
import ru.hse.repository.task.TaskView

@RestController
@RequestMapping("/api/student")
class StudentController(
    private val submissionService: SubmissionService,
    private val taskService: TaskService
) {
    @PostMapping("/submissions/upload")
    fun uploadSubmission(@RequestBody prototype: SubmissionPrototype): Long {
        return submissionService.uploadSubmission(prototype)
    }

    @GetMapping("/submissions")
    fun viewSubmission(@RequestParam submissionId: Long): Submission? {
        return submissionService.getSubmission(submissionId)
    }

    @GetMapping("/submissions/all")
    fun viewAllSubmissions(): List<SubmissionView> {
        return submissionService.getAllSubmissions().map { it.view() }
    }

    @GetMapping("/tasks")
    fun viewTask(@RequestParam taskId: Long): Task? {
        return taskService.getTask(taskId)
    }

    @GetMapping("/tasks/all")
    fun viewAllTasks(): List<TaskView> {
        return taskService.getAllTasks().map { it.view() }
    }
}
