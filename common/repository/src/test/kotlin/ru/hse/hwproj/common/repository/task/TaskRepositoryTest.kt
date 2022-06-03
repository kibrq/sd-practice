package ru.hse.hwproj.common.repository.task

import org.jooq.impl.DefaultDSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.hse.hwproj.common.repository.JooqConfiguration
import ru.hse.hwproj.common.repository.RepositoryConfiguration
import ru.hse.hwproj.common.repository.Tables
import ru.hse.hwproj.common.repository.tables.records.CheckersRecord
import ru.hse.hwproj.testutils.TestDataSourceConfiguration
import java.time.LocalDateTime
import java.util.*
import kotlin.test.*

@SpringBootTest(
    classes = [
        JooqConfiguration::class,
        TestDataSourceConfiguration::class,
        RepositoryConfiguration::class,
    ]
)
class TaskRepositoryTest(
    @Autowired private val dsl: DefaultDSLContext,
    @Autowired private val repository: TaskRepository,
) {
    private val random = Random()

    @AfterTest
    @BeforeTest
    fun clearDatabase() {
        dsl.delete(Tables.SUBMISSIONS)
            .execute()
        dsl.delete(Tables.TASKS)
            .execute()
        dsl.delete(Tables.CHECKERS)
            .execute()
    }

    private fun addRandomChecker(): Int {
        val checkerId = random.nextInt(1000)
        dsl.insertInto(Tables.CHECKERS)
            .columns(Tables.CHECKERS.fields().asList())
            .values(
                CheckersRecord(
                    checkerId,
                    "dockerfile"
                )
            )
            .execute()
        return checkerId
    }

    @Test
    fun `Upload with not existing checker should fail`() {
        val prototype = TaskPrototype(
            name = "task",
            description = "hard task",
            deadlineDate = LocalDateTime.of(2022, 12, 12, 0, 0),
            checkerId = 1000 // does not exist
        )
        assertNull(repository.upload(prototype))
    }

    @Test
    fun `Upload should return id with existing checker`() {
        val checkerId = addRandomChecker()
        val prototype = TaskPrototype(
            name = "task",
            description = "hard task",
            deadlineDate = LocalDateTime.of(2022, 12, 12, 10, 10),
            checkerId = checkerId
        )
        assertNotNull(repository.upload(prototype))
    }

    @Test
    fun `Get all should contain added task`() {
        val checkerId = addRandomChecker()
        val prototype = TaskPrototype(
            name = "task",
            description = "hard task",
            deadlineDate = LocalDateTime.of(2022, 12, 12, 10, 10),
            checkerId = checkerId
        )
        assertNotNull(repository.upload(prototype))

        val tasks = repository.getAll()
        assertTrue(tasks.any {
            it.name == prototype.name &&
                it.description == prototype.description &&
                it.deadlineDate == prototype.deadlineDate &&
                it.checkerId == prototype.checkerId
        })
    }

    @Test
    fun `Get by id should return added task`() {
        val checkerId = addRandomChecker()
        val prototype = TaskPrototype(
            name = "task",
            description = "hard task",
            deadlineDate = LocalDateTime.of(2022, 12, 12, 10, 10),
            checkerId = checkerId
        )
        assertNotNull(repository.upload(prototype))

        val tasks = repository.getAll()
        assertTrue(tasks.any {
            it.name == prototype.name &&
                it.description == prototype.description &&
                it.deadlineDate == prototype.deadlineDate &&
                it.checkerId == prototype.checkerId
        })
        val expectedTask = tasks.stream().findFirst().get()
        val task = repository.getById(expectedTask.id)
        assertEquals(task, expectedTask)
    }
}
