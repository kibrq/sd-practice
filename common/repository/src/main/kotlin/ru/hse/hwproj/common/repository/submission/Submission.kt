package ru.hse.hwproj.common.repository.submission

import org.jooq.impl.DSL
import org.jooq.impl.DefaultDSLContext
import org.springframework.stereotype.Component
import ru.hse.hwproj.common.repository.Sequences
import ru.hse.hwproj.common.repository.Tables
import ru.hse.hwproj.common.repository.checker.CheckerVerdict
import ru.hse.hwproj.common.repository.utils.withinTry
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
    val comments: String,
)

@Component
class SubmissionFeedbackRepositoryImpl(private val dsl: DefaultDSLContext) : SubmissionFeedbackRepository {
    override fun upload(prototype: SubmissionFeedbackPrototype): Int? {
        return dsl.insertInto(Tables.SUBMISSION_FEEDBACKS)
            .columns(Tables.SUBMISSION_FEEDBACKS.VERDICT, Tables.SUBMISSION_FEEDBACKS.COMMENTS)
            .values(prototype.verdict.toString().lowercase(), prototype.comments)
            .returningResult(Tables.SUBMISSION_FEEDBACKS.ID)
            .fetchOne()
            ?.value1()
    }

    override fun getByIds(ids: List<Int>): List<SubmissionFeedback> {
        return dsl.select()
            .from(Tables.SUBMISSION_FEEDBACKS)
            .where(Tables.SUBMISSIONS.ID.`in`(ids))
            .fetch()
            .into(SubmissionFeedback::class.java)
    }
}


@Component
class SubmissionRepositoryImpl(
    private val dsl: DefaultDSLContext,
) : SubmissionRepository {
    override fun upload(prototype: SubmissionPrototype): Int? {
        return withinTry {
            dsl.insertInto(Tables.SUBMISSIONS)
                .columns(Tables.SUBMISSIONS.fields().asList())
                .values(
                    Sequences.SUBMISSION_ID_SEQ.nextval().cast(Int::class.java),
                    DSL.value(prototype.taskId),
                    DSL.value(LocalDateTime.now()),
                    null,
                    DSL.value(prototype.repositoryUrl.toString())
                )
                .returningResult(Tables.SUBMISSIONS.ID)
                .fetchOne()
                ?.value1()
        }
    }

    override fun getByIds(ids: List<Int>): List<Submission> {
        return dsl.select()
            .from(Tables.SUBMISSIONS)
            .where(Tables.SUBMISSIONS.ID.`in`(ids))
            .fetch()
            .into(Submission::class.java)
    }

    override fun getAll(): List<Submission> {
        return dsl.select()
            .from(Tables.SUBMISSIONS)
            .fetch()
            .into(Submission::class.java)
    }

    override fun update(submissionId: Int, feedback: SubmissionFeedback) {
        println("$submissionId is ${feedback.verdict}")
    }
}
