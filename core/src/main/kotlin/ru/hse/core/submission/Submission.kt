package ru.hse.core.submission

import org.springframework.stereotype.Component
import ru.hse.core.checker.CheckerVerdict
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


@Component
class SubmissionRepository {
    fun uploadSubmission(prototype: SubmissionPrototype): Submission = TODO(prototype.toString())

    fun getSubmissionById(submissionId: Long): Submission = TODO(submissionId.toString())

    fun getAllSubmissions(): List<Submission> = TODO()

    fun updateSubmissionResult(submissionId: Long, feedback: SubmissionFeedback): Nothing = TODO()
}

