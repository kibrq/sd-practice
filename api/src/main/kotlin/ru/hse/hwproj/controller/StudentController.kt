package ru.hse.hwproj.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.hse.hwproj.service.SubmissionService
import ru.hse.hwproj.service.TaskService

@RestController
@RequestMapping("/api/student")
class StudentController(
    private val submissionService: SubmissionService,
    private val taskService: TaskService
) {

}
