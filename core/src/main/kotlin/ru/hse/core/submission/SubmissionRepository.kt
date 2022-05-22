package ru.hse.core.submission

class SubmissionRepository {
    fun uploadSubmission(prototype: SubmissionPrototype) = true

    fun getAllTasks() = listOf<Submission>()

    fun getTaskById(submissionId: Long) = Submission()
}
