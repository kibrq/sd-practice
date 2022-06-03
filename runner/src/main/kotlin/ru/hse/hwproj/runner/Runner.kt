package ru.hse.hwproj.runner

import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.io.File
import java.net.URL

private const val CHECKER = "checker"
private const val CHECKERS = "checkers"
private const val SUBMISSION = "submission"

@Component
@Scope("prototype")
class Runner {
    fun runSubmission(submissionId: Int, checkerId: Int, repositoryUrl: URL): Pair<Int, String> {
        val checkerName = "$CHECKER$checkerId"
        println("Running submission $repositoryUrl with $checkerName...")
        val dir = File(CHECKERS).resolve(checkerName).resolve("$SUBMISSION$submissionId")
        dir.mkdirs()
        val process = ProcessBuilder().apply {
            directory(dir)
            val repoName = repositoryUrl.toString().substringAfterLast('/')
            val repoPath = dir.resolve(repoName).absolutePath
            val gitClone = "git clone $repositoryUrl"
            val dockerRun = "docker run --rm -v $repoPath:/solution $checkerName"
            val mainCommand = "$gitClone && $dockerRun"
            println(mainCommand)

            setCommand(mainCommand)
        }.start()
        return process.getResult()
    }

    fun buildChecker(checkerId: String, checkerContent: String): Pair<Int, String> {
        val checkerName = "$CHECKER$checkerId"
        println("Building $checkerName...")
        val imageDir = File(CHECKERS).resolve(checkerName)
        imageDir.mkdirs()
        imageDir.resolve("Dockerfile").writeText(checkerContent)
        val process = ProcessBuilder().apply {
            directory(imageDir)
            val dockerBuild = "docker build -t $checkerName ."

            setCommand(dockerBuild)
        }.start()
        return process.getResult()
    }

    private fun Process.getResult(): Pair<Int, String> {
        waitFor()
        val message = String(inputStream.readAllBytes())
        val code = exitValue()
        return code to message
    }

    private fun ProcessBuilder.setCommand(cmd: String) {
        val isWindows = System.getProperty("os.name").startsWith("Win")
        val prefix = if (isWindows) listOf("cmd", "/c") else listOf("bash", "-c")
        command(prefix + cmd)
    }
}
