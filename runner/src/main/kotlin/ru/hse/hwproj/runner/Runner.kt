package ru.hse.hwproj.runner

import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.io.File
import java.net.URL

@Component
@Scope("prototype")
class Runner {
    fun run(checkerIdentifier: String, repositoryUrl: URL): Pair<Int, String> {
        val process = ProcessBuilder().apply {
            val repoName = repositoryUrl.toString().substringAfterLast('/')
            val gitClone = "git clone $repositoryUrl"
            val dockerRun = "docker run -v $repoName:/solution $checkerIdentifier"
            val mainCommand = "$gitClone && $dockerRun"

            val isWindows = System.getProperty("os.name").startsWith("Win")

            if (isWindows) {
                command("cmd", "/c", mainCommand)
            } else {
                command(mainCommand)
            }
        }.start()
        return process.getResult()
    }

    fun build(checkerIdentifier: String, checkerContent: String): Pair<Int, String> {
        val file = File("./$checkerIdentifier/Dockerfile")
        file.parentFile.mkdirs()
        file.createNewFile()
        file.writeBytes(checkerContent.toByteArray())
        val process = ProcessBuilder().apply {
            val cdForImage = "cd $checkerIdentifier"
            val dockerBuild = "docker build -t $checkerIdentifier"
            val mainCommand = "$cdForImage && $dockerBuild"

            val isWindows = System.getProperty("os.name").startsWith("Win")

            if (isWindows) {
                command("cmd", "/c", mainCommand)
            } else {
                command(mainCommand)
            }
        }.start()
        return process.getResult()
    }

    private fun Process.getResult(): Pair<Int, String> {
        waitFor()
        val message = String(inputStream.readAllBytes())
        val code = exitValue()
        return Pair(code, message)
    }
}
