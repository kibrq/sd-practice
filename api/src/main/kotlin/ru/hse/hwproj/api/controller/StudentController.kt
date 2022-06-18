package ru.hse.hwproj.api.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.hse.hwproj.api.service.SubmissionService
import ru.hse.hwproj.api.service.TaskService
import ru.hse.hwproj.common.repository.submission.Submission
import ru.hse.hwproj.common.repository.submission.SubmissionPrototype
import ru.hse.hwproj.common.repository.submission.SubmissionView
import ru.hse.hwproj.common.repository.task.Task
import ru.hse.hwproj.common.repository.task.TaskView

/*
 * REST-controller for handling student requests.
 */
@RestController
@RequestMapping("/api/student")
class StudentController(
    private val submissionService: SubmissionService,
    private val taskService: TaskService
) {
    /*
     * Uploads a new submission built from `prototype`.
     */
    @PostMapping("/submissions")
    fun uploadSubmission(@RequestBody prototype: SubmissionPrototype): ResponseEntity<Int> {
        return submissionService.uploadSubmission(prototype)
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
}
