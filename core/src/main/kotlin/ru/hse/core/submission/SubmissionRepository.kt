package ru.hse.core.submission

import org.springframework.stereotype.Component

@Component
class SubmissionRepository {
    fun uploadSubmission(prototype: Submission.SubmissionPrototype): Submission = TODO(prototype.toString())

    fun getSubmissionById(submissionId: Long): Submission = TODO(submissionId.toString())

    fun getAllSubmissions(): List<Submission> = TODO()

    fun updateSubmissionResult(submissionId: Long, feedback: SubmissionFeedback): Nothing = TODO()
}
