package ru.hse.hwproj.runner

import java.net.URL

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
            val dockerRun = "docker run -v $repoName:/solution $checkerIdentifier"
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
