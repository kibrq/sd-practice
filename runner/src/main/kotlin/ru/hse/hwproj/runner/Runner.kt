package ru.hse.hwproj.runner

import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.io.File
import java.net.URL

@Component
@Scope("prototype")
class Runner {
    fun runSubmission(checkerIdentifier: String, repositoryUrl: URL): Pair<Int, String> {
        println("Running submission $repositoryUrl with $checkerIdentifier...")
        val imageDir = File("checkers/$checkerIdentifier")
        val process = ProcessBuilder().apply {
            directory(imageDir)
            val repoName = repositoryUrl.toString().substringAfterLast('/')
            val repoPath = imageDir.resolve(repoName).absolutePath
            val gitClone = "git clone $repositoryUrl"
            val dockerRun = "docker run -v $repoPath:/solution $checkerIdentifier"
            val mainCommand = "$gitClone && $dockerRun"
            println(mainCommand)

            setCommand(mainCommand)
        }.start()
        return process.getResult()
    }

    fun buildChecker(checkerIdentifier: String, checkerContent: String): Pair<Int, String> {
        println("Building checker $checkerIdentifier...")
        val imageDir = File("checkers/$checkerIdentifier")
        imageDir.mkdirs()
        imageDir.resolve("Dockerfile").writeText(checkerContent)
        val process = ProcessBuilder().apply {
            directory(imageDir)
            val dockerBuild = "docker build -t $checkerIdentifier ."

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
