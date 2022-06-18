package ru.hse.hwproj.common.repository.submission

interface SubmissionFeedbackRepository {
    fun upload(prototype: SubmissionFeedbackPrototype): Int?

    fun getByIds(ids: List<Int>): List<SubmissionFeedback>

    fun getById(id: Int): SubmissionFeedback? {
        return getByIds(listOf(id)).getOrNull(0)
    }
}
