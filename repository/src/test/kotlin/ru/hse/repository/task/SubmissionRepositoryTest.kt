package ru.hse.repository.task

import org.jooq.impl.DefaultDSLContext
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.testng.annotations.AfterTest
import org.testng.annotations.BeforeTest
import ru.hse.repository.JooqConfiguration
import ru.hse.repository.Tables
import ru.hse.repository.TestDataSourceConfiguration
import ru.hse.repository.submission.SubmissionPrototype
import ru.hse.repository.submission.SubmissionRepository
import ru.hse.repository.tables.records.CheckersRecord
import java.net.URL
import java.time.LocalDateTime
import java.util.*
import kotlin.test.*

@SpringBootTest(
    classes = [
        JooqConfiguration::class,
        TestDataSourceConfiguration::class
    ]
)
class SubmissionRepositoryTest(
    @Autowired private val dsl: DefaultDSLContext
) {
    private val submissionRepository = SubmissionRepository(dsl)
    private val myUrl = URL("https://github.com/scanhex/zxc")

    @AfterTest
    @BeforeTest
    fun clearDatabase() {
        dsl.delete(Tables.TASKS)
            .execute()
        dsl.delete(Tables.CHECKERS)
            .execute()
    }

    private fun addRandomChecker(): String {
        val checkerId = UUID.randomUUID().toString()
        dsl.insertInto(Tables.CHECKERS)
            .columns(Tables.CHECKERS.fields().asList())
            .values(CheckersRecord(checkerId, "dockerfile"))
            .execute()
        return checkerId
    }

    private fun addTask(): Int? {
        val checkerId = addRandomChecker()
        val task = TaskPrototype(
            name = "ruslan",
            description = "ubit ruslana",
            deadlineDate = LocalDateTime.now(),
            checkerIdentifier = checkerId
        ).task()
        dsl.insertInto(Tables.TASKS)
            .columns(Tables.TASKS.fields().asList())
            .values(task)
            .execute()
        return task.id
    }

    @Test
    fun `Upload with not existing task should fail`() {
        val prototype = SubmissionPrototype(
            taskId = 239,
            repositoryUrl = myUrl
        )
        assertNull(submissionRepository.uploadSubmission(prototype))
    }

//    @Test
//    fun `Upload should return id with existing checker`() {
//        addTask(1)
//        val prototype = TaskPrototype(
//            name = "task",
//            description = "hard task",
//            deadlineDate = LocalDateTime.of(2022, 12, 12, 10, 10),
//            checkerIdentifier = "checker"
//        )
//        assertNotNull(taskRepository.upload(prototype))
//    }
//
//    @Test
//    fun `Get all should contain added task`() {
//        addChecker("checker")
//        val prototype = TaskPrototype(
//            name = "task",
//            description = "hard task",
//            deadlineDate = LocalDateTime.of(2022, 12, 12, 10, 10),
//            checkerIdentifier = "checker"
//        )
//        assertNotNull(taskRepository.upload(prototype))
//
//        val tasks = taskRepository.getAll()
//        assertTrue(tasks.any {
//            it.name == prototype.name &&
//                it.description == prototype.description &&
//                it.deadlineDate == prototype.deadlineDate &&
//                it.checkerIdentifier == prototype.checkerIdentifier
//        })
//    }
//
//    @Test
//    fun `Get by id should return added task`() {
//        addChecker("checker")
//        val prototype = TaskPrototype(
//            name = "task",
//            description = "hard task",
//            deadlineDate = LocalDateTime.of(2022, 12, 12, 10, 10),
//            checkerIdentifier = "checker"
//        )
//        assertNotNull(taskRepository.upload(prototype))
//
//        val tasks = taskRepository.getAll()
//        assertTrue(tasks.any {
//            it.name == prototype.name &&
//                it.description == prototype.description &&
//                it.deadlineDate == prototype.deadlineDate &&
//                it.checkerIdentifier == prototype.checkerIdentifier
//        })
//        val expectedTask = tasks.stream().findFirst().get()
//        val task = taskRepository.getTaskById(expectedTask.id)
//        assertEquals(task, expectedTask)
//    }
}
