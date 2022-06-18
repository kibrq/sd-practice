package ru.hse.hwproj.integration

import com.rabbitmq.client.ConnectionFactory
import org.junit.jupiter.api.AfterEach
import org.mockito.ArgumentMatchers
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.hse.hwproj.api.service.CheckerRequestsService
import ru.hse.hwproj.common.repository.JooqConfiguration
import ru.hse.hwproj.common.repository.RepositoryConfiguration
import ru.hse.hwproj.common.repository.checker.CheckerPrototype
import ru.hse.hwproj.common.repository.checker.CheckerRepository
import ru.hse.hwproj.common.repository.submission.SubmissionFeedbackRepository
import ru.hse.hwproj.common.repository.submission.SubmissionPrototype
import ru.hse.hwproj.common.repository.submission.SubmissionRepository
import ru.hse.hwproj.common.repository.task.TaskPrototype
import ru.hse.hwproj.common.repository.task.TaskRepository
import ru.hse.hwproj.runner.CheckerService
import ru.hse.hwproj.runner.Runner
import ru.hse.hwproj.testutils.EmbeddedRabbitMQTest
import ru.hse.hwproj.testutils.TestDataSourceConfiguration
import ru.hse.hwproj.testutils.TestRabbitMQConfiguration
import java.io.File
import java.net.URL
import java.time.LocalDateTime
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.test.Test
import kotlin.test.fail

@SpringBootTest(
    classes = [
        TestRabbitMQConfiguration::class,
        TestDataSourceConfiguration::class,
        RepositoryConfiguration::class,
        JooqConfiguration::class,
    ]
)
class SimpleTest(
    @Autowired private val checkerRepository: CheckerRepository,
    @Autowired private val submissionRepository: SubmissionRepository,
    @Autowired private val taskRepository: TaskRepository,
    @Autowired private val submissionFeedbackRepository: SubmissionFeedbackRepository,
    @Autowired private val connectionFactory: ConnectionFactory
) : EmbeddedRabbitMQTest() {
    @AfterEach
    fun cleanUpCheckersDirectory() = File("checkers").deleteRecursively().let { }

    @Test
    fun test() {
        val repositoryUrl = URL("https://github.com/scanhex/zxc")

        val prototype = CheckerPrototype("dockerfile")
        val checkerId = checkerRepository.upload(prototype) ?: throw IllegalStateException("Can't upload checker")

        val taskPrototype = TaskPrototype("task", "description", LocalDateTime.now(), checkerId)
        val taskId = taskRepository.upload(taskPrototype) ?: throw IllegalStateException("Can't upload task")

        val submissionId = submissionRepository.upload(SubmissionPrototype(taskId, repositoryUrl)) ?: return

        // Exceptions in `runSubmission` are handled(logged) by rabbitmq, and we can't test them directly.
        val lock = ReentrantLock()
        val done = lock.newCondition()
        var failReason: String? = null

        val runner = mock<Runner> {
            on {
                runSubmission(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(), any())
            } doAnswer { call ->
                val (argSubmissionId, argCheckerId, argRepositoryUrl) = call.arguments

                for ((name, expected, got) in listOf(
                    Triple("submission id", submissionId, argSubmissionId),
                    Triple("checker id", checkerId, argCheckerId),
                    Triple("repository url", repositoryUrl.toString(), argRepositoryUrl.toString())
                )) {
                    if (expected != got) {
                        failReason = "Wrong $name: expected $expected, got $got"
                    }
                }

                lock.withLock {
                    done.signalAll()
                }

                Pair(0, "good")
            }
        }

        val checkerRequestsService = CheckerRequestsService(checkerRepository, connectionFactory)
        val checkerService = CheckerService(
            submissionRepository,
            checkerRepository,
            taskRepository,
            submissionFeedbackRepository,
            runner,
            connectionFactory
        )

        checkerRequestsService.sendSubmissionCheckRequest(submissionId)
        checkerService.receiveTasks()

        lock.withLock {
            done.await()
        }

        failReason?.let {
            fail(it)
        }
    }
}
