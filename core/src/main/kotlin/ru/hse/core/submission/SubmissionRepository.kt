package ru.hse.core.submission

import org.springframework.stereotype.Repository

@Repository
class SubmissionRepository {
    fun createSubmission(prototype: Submission.Prototype): Submission = TODO()

    fun getSubmissionById(submissionId: Long): Submission = TODO()

    fun getAllSubmissions(): List<Submission> = TODO()
}
