package ru.hse.shell.command;

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import ru.hse.shell.TestUtils
import ru.hse.shell.util.Environment
import java.nio.file.Paths

public class CdTest {
    private val commandTestData = listOf(
            listOf(".") to Pair(Paths.get(System.getProperty("user.dir")).resolve("."), 0),
            listOf("..") to Pair(Paths.get(System.getProperty("user.dir")).resolve(".."), 0),
            listOf("src") to Pair(Paths.get(System.getProperty("user.dir")).resolve("src"), 0)
    )

    @TestFactory
    fun `Cd command test`() = commandTestData.map { (input, expected) ->
            DynamicTest.dynamicTest("cd $input") {
            val io = TestUtils.mockIO()
            val command = CdCommand()
            val env = Environment()
            val result = command.perform(input, io, env)
            assertEquals(expected.second, result.code)
            assertEquals(expected.first, env.getCurrentDirectory())
        }
    }
}
