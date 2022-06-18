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

/*
 * REST-controller for handling teacher requests.
 */
@RestController
@RequestMapping("/api/teacher")
class TeacherController(
    private val submissionService: SubmissionService,
    private val checkerRequestsService: CheckerRequestsService,
    private val taskService: TaskService
) {
    /*
     * Returns one task specified by a request parameter `id`.
     */
    @GetMapping("/tasks")
    fun viewTask(@RequestParam id: Int): ResponseEntity<Task> {
        return taskService.getTask(id)
    }

    /*
     * Returns a list of all tasks as their short versions.
     */
    @GetMapping("/tasks/list")
    fun viewAllTasks(): List<TaskView> {
        return taskService.getAllTasks().map { it.view() }
    }

    /*
     * Uploads a new task built from `prototype`.
     */
    @PostMapping("/tasks")
    fun uploadTask(@RequestBody prototype: TaskPrototype): ResponseEntity<Int> {
        return taskService.uploadTask(prototype)
    }

    /*
     * Returns one checker specified by a request parameter `id`.
     */
    @GetMapping("/checkers")
    fun viewChecker(@RequestParam id: String): ResponseEntity<Checker> {
        return checkerRequestsService.getChecker(id)
    }

    /*
     * Returns a list of all checkers.
     */
    @GetMapping("/checkers/list")
    fun viewAllCheckers(): List<Checker> {
        return checkerRequestsService.getAllCheckers()
    }

    /*
     * Uploads a new checker built from `prototype`.
     */
    @PostMapping("/checkers")
    fun uploadChecker(@RequestBody prototype: CheckerPrototype): ResponseEntity<Int> {
        return checkerRequestsService.sendCreateCheckerRequest(prototype)
    }

    /*
     * Returns one submission specified by a request parameter `id`.
     */
    @GetMapping("/submissions")
    fun viewSubmission(@RequestParam id: Int): ResponseEntity<Submission> {
        return submissionService.getSubmission(id)
    }

    /*
     * Returns a list of all submissions as their short versions.
     */
    @GetMapping("/submissions/list")
    fun viewAllSubmissions(): List<SubmissionView> {
        return submissionService.getAllSubmissions().map { it.view() }
    }
}
