package ru.hse.hwproj.runner

import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.net.URL

@Component
@Scope("prototype")
class Runner {
    fun run(checkerIdentifier: String, repositoryUrl: URL): Pair<Int, String> {
        val process = runProcess(checkerIdentifier, repositoryUrl)
        return process.getResult()
    }

    private fun Process.getResult(): Pair<Int, String> {
        waitFor()
        val message = String(inputStream.readAllBytes())
        val code = exitValue()
        return Pair(code, message)
    }

    private fun runProcess(checkerIdentifier: String, url: URL): Process {
        return ProcessBuilder().apply {
            val repoName = url.toString().substringAfterLast('/')
            val gitClone = "git clone $url"
            val dockerRun = "docker run -v -v /var/run/docker.sock/$repoName:/solution $checkerIdentifier"
            val mainCommand = "$gitClone && $dockerRun"

            val isWindows = System.getProperty("os.name").startsWith("Win")

            if (isWindows) {
                command("cmd", "/c", mainCommand)
            } else {
                command(mainCommand)
            }
        }.start()
    }
}
