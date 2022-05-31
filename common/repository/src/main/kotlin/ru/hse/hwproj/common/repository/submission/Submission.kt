package ru.hse.hwproj.common.repository.submission

import org.jooq.TableField
import org.jooq.impl.DSL
import org.jooq.impl.DSL.row
import org.jooq.impl.DefaultDSLContext
import org.springframework.stereotype.Component
import ru.hse.hwproj.common.repository.Sequences
import ru.hse.hwproj.common.repository.Tables
import ru.hse.hwproj.common.repository.checker.CheckerVerdict
import ru.hse.hwproj.common.repository.tables.pojos.SubmissionFeedbacks
import ru.hse.hwproj.common.repository.tables.pojos.Submissions
import ru.hse.hwproj.common.repository.tables.records.SubmissionsRecord
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

    companion object {
        fun ofPojos(submissions: Submissions, submissionFeedbacks: SubmissionFeedbacks) = Submission(
            id = submissions.id,
            taskId = submissions.taskId,
            date = submissions.date,
            result = SubmissionFeedback.ofPojos(submissionFeedbacks),
            repositoryUrl = URL(submissions.repositoryUrl)
        )
    }
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

class SubmissionFeedback(
    val id: Int,
    val verdict: CheckerVerdict,
    val comments: String,
) {
    companion object {
        fun ofPojos(submissionFeedbacks: SubmissionFeedbacks): SubmissionFeedback? = submissionFeedbacks.id?.let {
            SubmissionFeedback(
                id = submissionFeedbacks.id,
                verdict = CheckerVerdict.valueOf(submissionFeedbacks.verdict.uppercase()),
                comments = submissionFeedbacks.comments
            )
        }
    }
}

data class SubmissionFeedbackPrototype(
    val verdict: CheckerVerdict,
    val comments: String,
)

@Component
class SubmissionFeedbackRepositoryImpl(private val dsl: DefaultDSLContext) : SubmissionFeedbackRepository {
    override fun upload(prototype: SubmissionFeedbackPrototype): Int? {
        return dsl.insertInto(Tables.SUBMISSION_FEEDBACKS)
            .columns(Tables.SUBMISSION_FEEDBACKS.fields().asList())
            .values(Sequences.SUBMISSION_FEEDBACK_ID_SEQ.nextval(), prototype.verdict.toString().lowercase(), prototype.comments)
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
            .leftJoin(Tables.SUBMISSION_FEEDBACKS)
            .on(Tables.SUBMISSIONS.RESULT_ID.eq(Tables.SUBMISSION_FEEDBACKS.ID))
            .where(Tables.SUBMISSIONS.ID.`in`(ids))
            .fetch { r ->
                val submissions = r.into(Tables.SUBMISSIONS).into(Submissions::class.java)
                val submissionFeedbacks = r.into(Tables.SUBMISSION_FEEDBACKS).into(SubmissionFeedbacks::class.java)
                Submission.ofPojos(submissions, submissionFeedbacks)
            }
    }

    override fun getAll(): List<Submission> {
        return dsl.select()
            .from(Tables.SUBMISSIONS)
            .fetch()
            .into(Submission::class.java)
    }

    override fun <T> updateById(submissionId: Int, vararg pairs: Pair<TableField<SubmissionsRecord, T>, T>) {
        dsl.update(Tables.SUBMISSIONS)
            .set(row(pairs.map { it.first }), row(pairs.map { it.second }))
            .execute()
    }
}
