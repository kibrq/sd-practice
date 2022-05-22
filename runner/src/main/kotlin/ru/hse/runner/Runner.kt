package ru.hse.runner

import ru.hse.core.checker.CheckerVerdict
import ru.hse.core.submission.SubmissionFeedback
import ru.hse.core.submission.SubmissionRepository
import ru.hse.core.task.TaskRepository
import java.net.URL

class Runner {
    private val submissionRepository = SubmissionRepository()
    private val taskRepository = TaskRepository()


    fun run(submissionId: Long): SubmissionFeedback {
        val submission = submissionRepository.getSubmissionById(submissionId)
        val task = taskRepository.getTaskById(submission.taskId)
        val process = runProcess(task.checkerIdentifier, submission.repositoryUrl)
        return process.getResult()
    }

    private fun Process.getResult(): SubmissionFeedback {
        waitFor()
        val message = String(inputStream.readAllBytes())
        val code = exitValue()
        return SubmissionFeedback(CheckerVerdict.valueOf(code == 0), message)
    }

    private fun runProcess(checker_Identifier: String, url: URL): Process {
        return ProcessBuilder().apply {
            if (System.getProperty("os.name").startsWith("Win")) command(
                "cmd",
                "/c",
                "git clone $url " +
                    "&& docker run -v ${url.toString().substringAfterLast('/')}:/solution $checker_Identifier"
            )
            else command(
                "git clone $url " +
                    "&& docker run -v ${url.toString().substringAfterLast('/')}:/solution $checker_Identifier"
            )
        }.start()
    }
}
