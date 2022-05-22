package ru.hse.runner

import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.Delivery
import ru.hse.core.submission.SubmissionRepository
import ru.hse.core.task.TaskRepository

class CheckerService : AutoCloseable {
    private val submissionRepository = SubmissionRepository()
    private val taskRepository = TaskRepository()

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
        val submissionId = String(message.body).toLong()
        val submission = submissionRepository.getSubmissionById(submissionId)
        val task = taskRepository.getTaskById(submission.taskId)
        val submissionFeedback = runner.run(task.checkerIdentifier, submission.repositoryUrl)
        submissionRepository.updateSubmissionResult(submissionId, submissionFeedback)
    }
}
