package ru.hse.hwproj.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
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
}
