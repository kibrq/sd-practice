package ru.hse.shell.command

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import ru.hse.shell.TestUtils
import ru.hse.shell.util.Environment
import java.nio.file.Paths

internal class CdTest {
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

    @Test
    fun `Cd empty`() {
        val io = TestUtils.mockIO()
        val command = CdCommand()
        val env = Environment()
        val result = command.perform(listOf(), io, env)
        assertEquals(0, result.code)
        assertEquals("", io.outputStream.toString())

        val pwd = PwdCommand()
        val pwdResult = pwd.perform(listOf(), io, env)
        assertEquals(0, pwdResult.code)
        assertEquals(System.getProperty("user.home") + System.lineSeparator(), io.outputStream.toString())
    }

    @Test
    fun `Cd to non-existing directory`() {
        val io = TestUtils.mockIO()
        val command = CdCommand()
        val env = Environment()
        val result = command.perform(listOf("hello_there"), io, env)
        assertEquals(1, result.code)
    }


    @Test fun `Cd backwards and upwards`() {
        val curDir = Paths.get(System.getProperty("user.dir"), "src")
        val io = TestUtils.mockIO()
        val command = CdCommand()
        val env = Environment()
        val result = command.perform(listOf("src"), io, env)
        assertEquals(0, result.code)

        val pwd = PwdCommand()
        val pwdResult = pwd.perform(listOf(), io, env)
        assertEquals(0, pwdResult.code)
        assertEquals(curDir.toString() + System.lineSeparator(), io.outputStream.toString())

        command.perform(listOf(".."), io, env)
        assertEquals(0, result.code)
        assertEquals(System.getProperty("user.dir") + "/src/..", env.getCurrentDirectory().toString())
    }
}
