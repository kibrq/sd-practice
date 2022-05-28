package ru.hse.hwproj.common.repository.submission

interface SubmissionRepository {
    fun upload(prototype: SubmissionPrototype): Int?

    fun getByIds(ids: List<Int>): List<Submission>

    fun getById(submissionId: Int): Submission? {
        return getByIds(listOf(submissionId)).getOrNull(0)
    }

    fun getAll(): List<Submission>

    fun update(submissionId: Int, feedback: SubmissionFeedback)
}
