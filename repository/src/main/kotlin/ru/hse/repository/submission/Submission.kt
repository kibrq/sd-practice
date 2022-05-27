package ru.hse.repository.submission

import org.jooq.impl.DefaultDSLContext
import org.springframework.stereotype.Component
import ru.hse.repository.Tables
import ru.hse.repository.checker.CheckerVerdict
import java.net.URL
import java.time.LocalDateTime


class Submission(
    val id: Int,
    val taskId: Int,
    val date: LocalDateTime,
    val result: SubmissionFeedback?,
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
    val taskId: Int,
    val repositoryUrl: URL
)

data class SubmissionView(
    val id: Int,
    val taskId: Int,
    val dateString: String, // "EEE MMM dd HH:mm:ss zzz yyyy"
    val verdict: CheckerVerdict?
)

data class SubmissionFeedback(
    val id: Int,
    val verdict: CheckerVerdict,
    val comments: String
)

data class SubmissionFeedbackPrototype(
    val verdict: CheckerVerdict,
    val comments: String
)

@Component
class SubmissionFeedbackRepository(private val dsl: DefaultDSLContext) {
    fun upload(prototype: SubmissionFeedbackPrototype): Int? {
        return dsl.insertInto(Tables.SUBMISSION_FEEDBACKS)
            .columns(Tables.SUBMISSION_FEEDBACKS.VERDICT, Tables.SUBMISSION_FEEDBACKS.COMMENTS)
            .values(prototype.verdict.toString().lowercase(), prototype.comments)
            .returningResult(Tables.SUBMISSION_FEEDBACKS.ID)
            .fetchOne()
            ?.value1()
    }

    fun getById(id: Int): SubmissionFeedback? {
        return getByIds(listOf(id)).getOrNull(0)
    }

    fun getByIds(ids: List<Int>): List<SubmissionFeedback> {
        return dsl.select()
            .from(Tables.SUBMISSION_FEEDBACKS)
            .where(Tables.SUBMISSIONS.ID.`in`(ids))
            .fetch()
            .into(SubmissionFeedback::class.java)
    }
}


@Component
class SubmissionRepository(
    private val dsl: DefaultDSLContext,
) {
    fun upload(prototype: SubmissionPrototype): Int? {
        return dsl.insertInto(Tables.SUBMISSIONS)
            .columns(Tables.SUBMISSIONS.TASK_ID, Tables.SUBMISSIONS.DATE, Tables.SUBMISSIONS.RESULT_ID, Tables.SUBMISSIONS.REPOSITORY_URL)
            .values(prototype.taskId, LocalDateTime.now(), null, prototype.repositoryUrl.toString())
            .returningResult(Tables.SUBMISSIONS.ID)
            .fetchOne()
            ?.value1()
    }

    fun getByIds(ids: List<Int>): List<Submission> {
        return dsl.select()
            .from(Tables.SUBMISSIONS)
            .where(Tables.SUBMISSIONS.ID.`in`(ids))
            .fetch()
            .into(Submission::class.java)
    }

    fun getById(submissionId: Int): Submission? {
        return getByIds(listOf(submissionId)).getOrNull(0)
    }

    fun getAll(): List<Submission> {
        return dsl.select()
            .from(Tables.SUBMISSIONS)
            .fetch()
            .into(Submission::class.java)
    }

    fun update(submissionId: Long, feedback: SubmissionFeedback) {
        println("$submissionId is ${feedback.verdict}")
    }
}
