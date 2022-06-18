package ru.hse.hwproj.common.repository.submission

import org.jooq.TableField
import ru.hse.hwproj.common.repository.Tables
import ru.hse.hwproj.common.repository.tables.records.SubmissionsRecord

/*
 * Repository for storing submissions and uploading them via SubmissionPrototype.
 */
interface SubmissionRepository {
    /*
     * Creates a submission from `prototype` and saves it.
     */
    fun upload(prototype: SubmissionPrototype): Int?

    /*
     * Returns a list of all submissions with ids in `ids`.
     */
    fun getByIds(ids: List<Int>): List<Submission>

    /*
     * Returns a submission with a specified `id`.
     */
    fun getById(submissionId: Int) = getByIds(listOf(submissionId)).getOrNull(0)

    /*
     * Returns all available submissions.
     */
    fun getAll(): List<Submission>

    /*
     * Updates a submission record with specified `submissionId`.
     */
    fun <T> updateById(submissionId: Int, vararg pairs: Pair<TableField<SubmissionsRecord, T>, T>)

    /*
     * Updates a submission with specified `submissionId` by setting a new feedback.
     */
    fun updateResultId(submissionId: Int, submissionFeedbackId: Int) =
        updateById(submissionId, Pair(Tables.SUBMISSIONS.RESULT_ID, submissionFeedbackId))
}
