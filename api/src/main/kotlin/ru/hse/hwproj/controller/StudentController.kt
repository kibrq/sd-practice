package ru.hse.hwproj.controller

import org.springframework.web.bind.annotation.*
import ru.hse.core.submission.Submission
import ru.hse.core.task.Task
import ru.hse.hwproj.service.SubmissionService
import ru.hse.hwproj.service.TaskService

@RestController
@RequestMapping("/api/student")
class StudentController(
    private val submissionService: SubmissionService,
    private val taskService: TaskService
) {
    @PostMapping("/submissions/upload")
    fun uploadSubmission(@RequestBody prototype: Submission.Prototype) {
        submissionService.uploadSubmission(prototype)
    }

    @GetMapping("/submissions")
    fun viewSubmission(@RequestBody submissionId: Long): Submission {
        return submissionService.getSubmission(submissionId)
    }

    @GetMapping("/submissions/all")
    fun viewAllSubmissions(): List<Submission> {
        return submissionService.getAllSubmissions()
    }

    @GetMapping("/tasks")
    fun viewTask(@RequestBody taskId: Long): Task {
        return taskService.getTask(taskId)
    }

    @GetMapping("/tasks/all")
    fun viewAllTasks(): List<Task> {
        return taskService.getAllTasks()
    }
}
