package ru.hse.hwproj.service

import org.springframework.stereotype.Service
import ru.hse.repository.submission.Submission
import ru.hse.repository.submission.SubmissionPrototype
import ru.hse.repository.submission.SubmissionRepository

@Service
class SubmissionService(
    private val submissionRepository: SubmissionRepository,
    private val checkerRequestsService: CheckerRequestsService
) {
    fun uploadSubmission(prototype: SubmissionPrototype): Int? {
        val submissionId = submissionRepository.upload(prototype) ?: return null
        checkerRequestsService.sendSubmissionCheckRequest(submissionId)
        return submissionId
    }

    fun getSubmission(submissionId: Int): Submission? = submissionRepository.getById(submissionId)

    fun getAllSubmissions(): List<Submission> = submissionRepository.getAll()
}
