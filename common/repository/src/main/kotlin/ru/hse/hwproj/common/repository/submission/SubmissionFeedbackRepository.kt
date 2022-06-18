package ru.hse.hwproj.common.repository.submission

/*
 * Repository for storing submission feedbacks and uploading them via SubmissionFeedbackPrototype.
 */
interface SubmissionFeedbackRepository {
    /*
     * Creates a submission feedback from `prototype` and saves it.
     */
    fun upload(prototype: SubmissionFeedbackPrototype): Int?

    /*
     * Returns a list of all submission feedbacks with ids in `ids`.
     */
    fun getByIds(ids: List<Int>): List<SubmissionFeedback>

    /*
     * Returns a submission feedback with a specified `id`.
     */
    fun getById(id: Int) = getByIds(listOf(id)).getOrNull(0)
}
