package ru.hse.hwproj.service

import org.springframework.stereotype.Service
import ru.hse.core.submission.Submission
import ru.hse.core.submission.SubmissionPrototype
import ru.hse.core.submission.SubmissionRepository

@Service
class SubmissionService(
    private val submissionRepository: SubmissionRepository,
    private val checkerRequestsService: CheckerRequestsService
) {
    fun uploadSubmission(prototype: SubmissionPrototype) = submissionRepository.uploadSubmission(prototype)

    fun getSubmission(submissionId: Long): Submission? = submissionRepository.getSubmissionById(submissionId)

    fun getAllSubmissions(): List<Submission> = submissionRepository.getAllSubmissions()
}
