package ru.hse.hwproj.runner

import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.Delivery
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import ru.hse.hwproj.common.repository.checker.CheckerVerdict
import ru.hse.hwproj.common.repository.submission.SubmissionFeedbackPrototype
import ru.hse.hwproj.common.repository.submission.SubmissionFeedbackRepository
import ru.hse.hwproj.common.repository.submission.SubmissionRepository
import ru.hse.hwproj.common.repository.task.TaskRepository
import java.util.concurrent.atomic.AtomicInteger

object CheckerServiceIdHolder {
    val currentId = AtomicInteger(0)
}

@Component
@Scope("prototype")
class CheckerService(
    private val submissionRepository: SubmissionRepository,
    private val taskRepository: TaskRepository,
    private val submissionFeedbackRepository: SubmissionFeedbackRepository,
    private val connectionFactory: ConnectionFactory,
) : AutoCloseable {
    val id = CheckerServiceIdHolder.currentId.incrementAndGet()
    private val runner = Runner()

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
        val (code, resultMessage) = runner.run(task.checkerIdentifier, submission.repositoryUrl)
        submissionFeedbackRepository.upload(SubmissionFeedbackPrototype(CheckerVerdict.valueOf(code == 0), resultMessage))
    }
}
