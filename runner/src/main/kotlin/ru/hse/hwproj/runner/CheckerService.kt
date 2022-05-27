package ru.hse.hwproj.runner

import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.Delivery
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import ru.hse.repository.submission.SubmissionRepository
import ru.hse.repository.task.TaskRepository
import java.util.concurrent.atomic.AtomicInteger

object CheckerServiceIdHolder {
    val currentId = AtomicInteger(0)
}

@Component
@Scope("prototype")
@ComponentScan("ru.hse.repository")
class CheckerService(
    private val submissionRepository: SubmissionRepository,
    private val taskRepository: TaskRepository
) : AutoCloseable {
    val id = CheckerServiceIdHolder.currentId.incrementAndGet()
    private val runner = Runner()

    private val connectionFactory by lazy {
        ConnectionFactory().apply {
            host = "localhost"
            port = 5672
        }
    }

    private val connection by lazy {
        connectionFactory.newConnection()
    }

    private val channel by lazy {
        connection.createChannel()
    }

    override fun close() {
        channel.close()
        connection.close()
    }

    fun receiveTasks() {
        channel.queueDeclare("submissions_queue", false, false, false, null)
        channel.basicConsume("submissions_queue", true, ::receiveSubmission) { _ -> }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun receiveSubmission(consumerTag: String, message: Delivery) {
        val submissionId = String(message.body).toInt()
        val submission = submissionRepository.getById(submissionId) ?: return
        val task = taskRepository.getById(submission.taskId) ?: return
        val submissionFeedback = runner.run(task.checkerIdentifier, submission.repositoryUrl)
        submissionRepository.update(submissionId, submissionFeedback)
    }
}
