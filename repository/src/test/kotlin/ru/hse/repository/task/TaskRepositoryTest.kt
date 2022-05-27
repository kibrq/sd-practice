package ru.hse.repository.task

import org.jooq.impl.DefaultDSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.hse.repository.JooqConfiguration
import ru.hse.repository.Tables
import ru.hse.repository.TestDataSourceConfiguration
import ru.hse.repository.tables.records.CheckersRecord
import java.time.LocalDateTime
import kotlin.test.*

@SpringBootTest(
    classes = [
        JooqConfiguration::class,
        TestDataSourceConfiguration::class
    ]
)
class TaskRepositoryTest(
    @Autowired private val dsl: DefaultDSLContext
) {
    private val taskRepository = TaskRepository(dsl)

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
        assertFalse(taskRepository.upload(prototype) != null)
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
        assertTrue(taskRepository.upload(prototype) != null)
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
        assertTrue(taskRepository.upload(prototype) != null)

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
        assertTrue(taskRepository.upload(prototype) != null)

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
