package ru.hse.hwproj.api.service

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import ru.hse.hwproj.api.utils.orElseStatus
import ru.hse.hwproj.api.utils.runOrElseStatus
import ru.hse.hwproj.common.repository.submission.Submission
import ru.hse.hwproj.common.repository.submission.SubmissionPrototype
import ru.hse.hwproj.common.repository.submission.SubmissionRepository

@Service
class SubmissionService(
    private val submissionRepository: SubmissionRepository,
    private val checkerRequestsService: CheckerRequestsService
) {
    fun uploadSubmission(prototype: SubmissionPrototype): ResponseEntity<Int> {
        return submissionRepository.upload(prototype).runOrElseStatus(HttpStatus.BAD_REQUEST) {
            checkerRequestsService.sendSubmissionCheckRequest(it)
        }
    }

    fun getSubmission(id: Int): ResponseEntity<Submission> {
        return submissionRepository.getById(id).orElseStatus(HttpStatus.NOT_FOUND)
    }

    fun getAllSubmissions(): List<Submission> = submissionRepository.getAll()
}
