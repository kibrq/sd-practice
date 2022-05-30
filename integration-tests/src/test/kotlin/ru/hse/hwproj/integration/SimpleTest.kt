package ru.hse.hwproj.integration

import com.rabbitmq.client.ConnectionFactory
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
import java.net.URL
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

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
    @Autowired private val connectionFactory: ConnectionFactory,
) : EmbeddedRabbitMQTest() {

    lateinit var checkerRequestsService: CheckerRequestsService
    lateinit var checkerService: CheckerService

    @Test
    fun test() {
        val checkerIdentifier = "dockerfile"
        val repositoryUrl = URL("https://github.com/scanhex/zxc")

        val runner = mock<Runner> {
            on { run(ArgumentMatchers.anyString(), any()) } doAnswer { call ->
                assertEquals(
                    call.arguments.getOrElse(0) { "" },
                    checkerIdentifier
                )
                assertEquals(
                    call.arguments.getOrElse(1) { "" }.toString(),
                    repositoryUrl.toString()
                )
                Pair(0, "good")
            }
        }


        checkerRequestsService = CheckerRequestsService(checkerRepository, connectionFactory)
        checkerService = CheckerService(
            submissionRepository,
            taskRepository,
            submissionFeedbackRepository,
            connectionFactory,
            runner
        )

        val identifier = checkerRepository.upload(CheckerPrototype("dockerfile")) ?: return
        val taskId =
            taskRepository.upload(TaskPrototype("task", "description", LocalDateTime.now(), identifier)) ?: return
        val submissionId =
            submissionRepository.upload(SubmissionPrototype(taskId, URL("https://github.com/scanhex/zxc"))) ?: return

        checkerRequestsService.sendSubmissionCheckRequest(submissionId)
        checkerService.receiveTasks()
    }
}
