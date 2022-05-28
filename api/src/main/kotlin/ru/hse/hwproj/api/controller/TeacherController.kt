package ru.hse.hwproj.api.controller

import org.springframework.web.bind.annotation.*
import ru.hse.hwproj.api.service.CheckerRequestsService
import ru.hse.hwproj.api.service.SubmissionService
import ru.hse.hwproj.api.service.TaskService
import ru.hse.hwproj.common.repository.checker.Checker
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
    fun viewTask(@RequestParam taskId: Int): Task? {
        return taskService.getTask(taskId)
    }

    @GetMapping("/tasks/all")
    fun viewAllTasks(): List<TaskView> {
        return taskService.getAllTasks().map { it.view() }
    }

    @PostMapping("/tasks/upload")
    fun uploadTask(@RequestBody prototype: TaskPrototype): Int? {
        return taskService.uploadTask(prototype)
    }

    @GetMapping("/checkers")
    fun viewChecker(@RequestParam checkerId: String): Checker? {
        return checkerRequestsService.getChecker(checkerId)
    }

    @GetMapping("/checkers/all")
    fun viewAllCheckers(): List<Checker> {
        return checkerRequestsService.getAllCheckers()
    }

    @PostMapping("/checkers/upload")
    fun uploadChecker(@RequestBody dockerfile: String): String? {
        return checkerRequestsService.sendCreateCheckerRequest(dockerfile)
    }

    @GetMapping("/submissions")
    fun viewSubmission(@RequestParam submissionId: Int): Submission? {
        return submissionService.getSubmission(submissionId)
    }

    @GetMapping("/submissions/all")
    fun viewAllSubmissions(): List<SubmissionView> {
        return submissionService.getAllSubmissions().map { it.view() }
    }
}
