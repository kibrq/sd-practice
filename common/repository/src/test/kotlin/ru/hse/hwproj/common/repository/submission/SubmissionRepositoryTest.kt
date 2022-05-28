package ru.hse.hwproj.common.repository.submission

import org.jooq.impl.DefaultDSLContext
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.testng.annotations.AfterTest
import org.testng.annotations.BeforeTest
import ru.hse.hwproj.common.repository.JooqConfiguration
import ru.hse.hwproj.common.repository.RepositoryConfiguration
import ru.hse.hwproj.common.repository.Sequences
import ru.hse.hwproj.common.repository.Tables
import ru.hse.hwproj.common.repository.tables.records.CheckersRecord
import ru.hse.hwproj.testutils.TestDataSourceConfiguration
import java.net.URL
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@SpringBootTest(
    classes = [
        JooqConfiguration::class,
        TestDataSourceConfiguration::class,
        RepositoryConfiguration::class,
    ]
)
class SubmissionRepositoryTest(
    @Autowired private val dsl: DefaultDSLContext,
    @Autowired private val repository: SubmissionRepository,
) {
    private val myUrl = URL("https://github.com/scanhex/zxc")

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

    private fun addRandomChecker(): String {
        val checkerId = UUID.randomUUID().toString()
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

    private fun addTask(): Int? {
        val checkerId = addRandomChecker()

        return dsl.insertInto(Tables.TASKS)
            .columns(Tables.TASKS.fields().asList())
            .values(
                Sequences.TASK_ID_SEQ.nextval(),
                "task",
                LocalDateTime.now(),
                "description",
                LocalDateTime.of(2012, 2, 10, 0, 9),
                checkerId
            )
            .returningResult(Tables.TASKS.ID)
            .fetchOne()
            ?.value1()
    }

    @Test
    fun `Upload with not existing task should fail`() {
        val prototype = SubmissionPrototype(
            taskId = 239,
            repositoryUrl = myUrl
        )
        assertNull(repository.upload(prototype))
    }

    @Test
    fun `Upload should return id with existing task`() {
        val taskId = addTask() ?: return
        val prototype = SubmissionPrototype(
            taskId = taskId,
            repositoryUrl = myUrl,
        )
        assertNotNull(repository.upload(prototype))
    }
}
