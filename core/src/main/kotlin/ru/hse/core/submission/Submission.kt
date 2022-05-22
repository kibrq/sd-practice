package ru.hse.core.submission

import ru.hse.core.checker.CheckerVerdict
import java.net.URL
import java.util.*

class Submission private constructor(
    val id: Long,
    val date: Date,
    val result: SubmissionFeedback?,
    val taskId: Long,
    val repositoryUrl: URL
) {
    data class SubmissionPrototype(
        val taskId: Long,
        val repositoryUrl: URL
    ) {
        fun task() = Submission(
            id = idCounter++,
            date = calendar.time,
            result = null,
            taskId = taskId,
            repositoryUrl = repositoryUrl
        )
    }

    fun view() = SubmissionView(
        id = id,
        taskId = taskId,
        dateString = date.toString(),
        verdict = result?.verdict
    )

    companion object {
        private val calendar: Calendar = Calendar.getInstance()
        private var idCounter = 0L
    }
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
