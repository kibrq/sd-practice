package ru.hse.shell.command;

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.hse.shell.TestUtils
import ru.hse.shell.util.Environment

public class LsTest {
    private val expected = "README.md\n" +
            "gradle\n" +
            "gradlew\n" +
            "description\n" +
            "build.gradle.kts\n" +
            "settings.gradle.kts\n" +
            "gradle.properties\n" +
            "build\n" +
            "gradlew.bat\n" +
            "src\n"

    @Test
    fun `Ls command test`() {
        val io = TestUtils.mockIO()
        val command = LsCommand()
        val env = Environment()
        val result = command.perform(listOf(), io, env)
        Assertions.assertEquals(0, result.code)
        Assertions.assertEquals(expected, io.outputStream.toString())
    }
}
