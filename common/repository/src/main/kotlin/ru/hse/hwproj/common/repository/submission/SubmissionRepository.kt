package ru.hse.hwproj.common.repository.submission

import org.jooq.TableField
import ru.hse.hwproj.common.repository.Tables
import ru.hse.hwproj.common.repository.tables.records.SubmissionsRecord

interface SubmissionRepository {
    fun upload(prototype: SubmissionPrototype): Int?

    fun getByIds(ids: List<Int>): List<Submission>

    fun getById(submissionId: Int): Submission? {
        return getByIds(listOf(submissionId)).getOrNull(0)
    }

    fun getAll(): List<Submission>

    fun <T> updateById(submissionId: Int, vararg pairs: Pair<TableField<SubmissionsRecord, T>, T>)

    fun updateResultId(submissionId: Int, submissionFeedbackId: Int) =
        updateById(submissionId, Pair(Tables.SUBMISSIONS.RESULT_ID, submissionFeedbackId))
}
