package ru.hse.hwproj.api.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.hse.hwproj.api.service.CheckerRequestsService
import ru.hse.hwproj.api.service.SubmissionService
import ru.hse.hwproj.api.service.TaskService
import ru.hse.hwproj.common.repository.checker.Checker
import ru.hse.hwproj.common.repository.checker.CheckerPrototype
import ru.hse.hwproj.common.repository.submission.Submission
import ru.hse.hwproj.common.repository.submission.SubmissionView
import ru.hse.hwproj.common.repository.task.Task
import ru.hse.hwproj.common.repository.task.TaskPrototype
import ru.hse.hwproj.common.repository.task.TaskView

@RestController
@RequestMapping("/api/teacher")
class TeacherController(
    private val submissionService: SubmissionService,
    private val checkerRequestsService: CheckerRequestsService,
    private val taskService: TaskService
) {
    @GetMapping("/tasks")
    fun viewTask(@RequestParam id: Int): ResponseEntity<Task> {
        return taskService.getTask(id)
    }

    @GetMapping("/tasks/list")
    fun viewAllTasks(): List<TaskView> {
        return taskService.getAllTasks().map { it.view() }
    }

    @PostMapping("/tasks")
    fun uploadTask(@RequestBody prototype: TaskPrototype): ResponseEntity<Int> {
        return taskService.uploadTask(prototype)
    }

    @GetMapping("/checkers")
    fun viewChecker(@RequestParam id: String): ResponseEntity<Checker> {
        return checkerRequestsService.getChecker(id)
    }

    @GetMapping("/checkers/list")
    fun viewAllCheckers(): List<Checker> {
        return checkerRequestsService.getAllCheckers()
    }

    @PostMapping("/checkers")
    fun uploadChecker(@RequestBody prototype: CheckerPrototype): ResponseEntity<Int> {
        return checkerRequestsService.sendCreateCheckerRequest(prototype)
    }

    @GetMapping("/submissions")
    fun viewSubmission(@RequestParam id: Int): ResponseEntity<Submission> {
        return submissionService.getSubmission(id)
    }

    @GetMapping("/submissions/list")
    fun viewAllSubmissions(): List<SubmissionView> {
        return submissionService.getAllSubmissions().map { it.view() }
    }
}
