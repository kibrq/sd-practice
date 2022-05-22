package ru.hse.hwproj.controller

import org.springframework.web.bind.annotation.*
import ru.hse.core.submission.Submission
import ru.hse.core.task.Task
import ru.hse.hwproj.service.CheckerRequestsService
import ru.hse.hwproj.service.SubmissionService
import ru.hse.hwproj.service.TaskService

@RestController
@RequestMapping("/api/teacher")
class TeacherController(
    private val submissionService: SubmissionService,
    private val checkerRequestsService: CheckerRequestsService,
    private val taskService: TaskService
) {
    @PostMapping("/tasks/upload")
    fun uploadTask(@RequestBody prototype: Task.TaskPrototype) {
        taskService.uploadTask(prototype)
    }

    @PostMapping("/checkers/upload")
    fun uploadChecker(@RequestBody dockerfile: String) {
        checkerRequestsService.sendCreateCheckerRequest(dockerfile)
    }

    @GetMapping("/submissions")
    fun viewSubmission(@RequestParam submissionId: Long): Submission {
        return submissionService.getSubmission(submissionId)
    }

    @GetMapping("/submissions/all")
    fun viewAllSubmissions(): List<Submission> {
        return submissionService.getAllSubmissions()
    }
}
