package ru.hse.core.task

import org.jooq.impl.DefaultDSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import ru.hse.core.JooqConfiguration
import ru.hse.core.RepositoryConfiguration
import ru.hse.core.Tables
import ru.hse.core.TestDataSourceConfiguration
import ru.hse.core.tables.records.CheckersRecord
import java.time.LocalDateTime
import kotlin.test.*

@ActiveProfiles("test")
@SpringBootTest(classes = [
    TestDataSourceConfiguration::class,
    JooqConfiguration::class,
    RepositoryConfiguration::class
])
class TaskRepositoryTest {
    @Autowired
    lateinit var dsl: DefaultDSLContext

    @Autowired
    lateinit var taskRepository: TaskRepository


    @AfterTest
    @BeforeTest
    fun clearDatabase() {
        dsl.delete(Tables.TASKS)
            .execute()
        dsl.delete(Tables.CHECKERS)
            .execute()
    }
    private fun addChecker(checkerIdentifier: String) {
        dsl.insertInto(Tables.CHECKERS)
            .columns(Tables.CHECKERS.fields().asList())
            .values(CheckersRecord(checkerIdentifier, "dockerfile"))
            .execute()
    }

    @Test
    fun `Upload with not existing checker should fail`() {
        val prototype = TaskPrototype(
            name = "task",
            description = "hard task",
            deadlineDate = LocalDateTime.of(2022, 12, 12, 0, 0),
            checkerIdentifier = "not exists"
        )
        assertFalse(taskRepository.upload(prototype))
    }

    @Test
    fun `Upload should return true with existing checker`() {
        addChecker("checker")
        val prototype = TaskPrototype(
            name = "task",
            description = "hard task",
            deadlineDate = LocalDateTime.of(2022, 12, 12, 10, 10),
            checkerIdentifier = "checker"
        )
        assertTrue(taskRepository.upload(prototype))
    }

    @Test
    fun `Get all should contain added task`() {
        addChecker("checker")
        val prototype = TaskPrototype(
            name = "task",
            description = "hard task",
            deadlineDate = LocalDateTime.of(2022, 12, 12, 10, 10),
            checkerIdentifier = "checker"
        )
        assertTrue(taskRepository.upload(prototype))

        val tasks = taskRepository.getAll()
        assertTrue(tasks.any {
            it.name == prototype.name &&
                it.description == prototype.description &&
                it.deadlineDate == prototype.deadlineDate &&
                it.checkerIdentifier == prototype.checkerIdentifier
        })
    }

    @Test
    fun `Get by id should return added task`() {
        addChecker("checker")
        val prototype = TaskPrototype(
            name = "task",
            description = "hard task",
            deadlineDate = LocalDateTime.of(2022, 12, 12, 10, 10),
            checkerIdentifier = "checker"
        )
        assertTrue(taskRepository.upload(prototype))

        val tasks = taskRepository.getAll()
        assertTrue(tasks.any {
            it.name == prototype.name &&
                it.description == prototype.description &&
                it.deadlineDate == prototype.deadlineDate &&
                it.checkerIdentifier == prototype.checkerIdentifier
        })
        val expectedTask = tasks.stream().findFirst().get()
        val task = taskRepository.getTaskById(expectedTask.id)
        assertEquals(task, expectedTask)
    }
}
