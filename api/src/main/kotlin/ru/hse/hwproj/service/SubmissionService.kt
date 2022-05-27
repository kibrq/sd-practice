package ru.hse.hwproj.service

import org.springframework.stereotype.Service
import ru.hse.repository.submission.Submission
import ru.hse.repository.submission.SubmissionPrototype
import ru.hse.repository.submission.SubmissionRepository

@Service
class SubmissionService(
    private val submissionRepository: SubmissionRepository
) {
    fun uploadSubmission(prototype: SubmissionPrototype): Boolean = submissionRepository.uploadSubmission(prototype)

    fun getSubmission(submissionId: Long): Submission? = submissionRepository.getSubmissionById(submissionId)

    fun getAllSubmissions(): List<Submission> = submissionRepository.getAllSubmissions()
}
