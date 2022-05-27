package ru.hse.runner

import ru.hse.repository.checker.CheckerVerdict
import ru.hse.repository.submission.SubmissionFeedback
import java.net.URL

class Runner {

    fun run(checkerIdentifier: String, repositoryUrl: URL): SubmissionFeedback {
        val process = runProcess(checkerIdentifier, repositoryUrl)
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
