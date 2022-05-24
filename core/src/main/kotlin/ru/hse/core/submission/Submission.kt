package ru.hse.core.submission

import org.jooq.DSLContext
import org.jooq.impl.DefaultDSLContext
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import ru.hse.core.Tables
import ru.hse.core.checker.CheckerVerdict
import ru.hse.core.tables.records.SubmissionsRecord
import java.net.URL
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicLong


object SubmissionIdHolder {
    val currentId = AtomicLong(0)
}
class Submission(
    val id: Long,
    val date: LocalDateTime,
    val result: SubmissionFeedback?,
    val taskId: Long,
    val repositoryUrl: URL
) {
    fun view() = SubmissionView(
        id = id,
        taskId = taskId,
        dateString = date.toString(),
        verdict = result?.verdict
    )
}

data class SubmissionPrototype(
    val taskId: Long,
    val repositoryUrl: URL
) {
    fun task() = Submission(
        id = SubmissionIdHolder.currentId.incrementAndGet(),
        date = LocalDateTime.now(),
        result = null,
        taskId = taskId,
        repositoryUrl = repositoryUrl
    )
}

data class SubmissionView(
    val id: Long,
    val taskId: Long,
    val dateString: String, // "EEE MMM dd HH:mm:ss zzz yyyy"
    val verdict: CheckerVerdict?
)

data class SubmissionFeedback(
    val verdict: CheckerVerdict,
    val comments: String
)


class SubmissionRepository(private val dsl: DefaultDSLContext) {
    fun uploadSubmission(prototype: SubmissionPrototype) = dsl.insertInto(Tables.SUBMISSIONS)
            .columns(Tables.SUBMISSIONS.fields().asList())
            .values(SubmissionsRecord(
                SubmissionIdHolder.currentId.incrementAndGet().toInt(),
                prototype.taskId.toInt(),
                LocalDateTime.now(),
                null,
                prototype.repositoryUrl.toString(),
            ))
            .execute().let { it == 0 }

    fun getSubmissionById(submissionId: Long): Submission? = dsl.select()
            .from(Tables.SUBMISSIONS)
            .where(Tables.SUBMISSIONS.ID.eq(submissionId.toInt()))
            .fetchOne()
            ?.into(Submission::class.java)

    fun getAllSubmissions(): List<Submission> = dsl.select()
        .from(Tables.SUBMISSIONS)
        .fetch()
        .into(Submission::class.java)

    fun updateSubmissionResult(submissionId: Long, feedback: SubmissionFeedback): Nothing = TODO()
}

