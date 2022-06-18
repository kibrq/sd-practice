package ru.hse.hwproj.runner

import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.Delivery
import org.springframework.stereotype.Service
import ru.hse.hwproj.common.repository.checker.CheckerRepository
import ru.hse.hwproj.common.repository.checker.CheckerVerdict
import ru.hse.hwproj.common.repository.submission.SubmissionFeedbackPrototype
import ru.hse.hwproj.common.repository.submission.SubmissionFeedbackRepository
import ru.hse.hwproj.common.repository.submission.SubmissionRepository
import ru.hse.hwproj.common.repository.task.TaskRepository

/*
 * Service for building checker docker images and checking submissions.
 */
@Service
class CheckerService(
    private val submissionRepository: SubmissionRepository,
    private val checkerRepository: CheckerRepository,
    private val taskRepository: TaskRepository,
    private val submissionFeedbackRepository: SubmissionFeedbackRepository,
    private val runner: Runner,
    connectionFactory: ConnectionFactory
) : AutoCloseable {
    private val connection = connectionFactory.newConnection()
    private val submissionsChannel = connection.createChannel()
    private val checkersChannel = connection.createChannel()

    init {
        submissionsChannel.queueDeclare("submissions_queue", false, false, false, null)
        checkersChannel.queueDeclare("checkers_queue", false, false, false, null)
    }

    override fun close() {
        checkersChannel.close()
        submissionsChannel.close()
        connection.close()
    }

    /*
     * Start receiving tasks for building checker docker images and checking submissions.
     */
    fun startReceivingTasks() {
        submissionsChannel.basicConsume("submissions_queue", true, ::receiveSubmission) { _ -> }
        checkersChannel.basicConsume("checkers_queue", true, ::receiveNewChecker) { _ -> }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun receiveSubmission(consumerTag: String, message: Delivery) {
        val submissionId = String(message.body).toIntOrNull() ?: return
        val submission = submissionRepository.getById(submissionId) ?: return
        val task = taskRepository.getById(submission.taskId) ?: return
        val (code, resultMessage) = runner.runSubmission(submissionId, task.checkerId, submission.repositoryUrl)
        println(code)
        println(resultMessage)
        val feedback = SubmissionFeedbackPrototype(
            CheckerVerdict.valueOf(code == 0),
            resultMessage.takeLast(1023)
        )
        val submissionFeedbackId = submissionFeedbackRepository.upload(feedback) ?: return
        submissionRepository.updateResultId(submissionId, submissionFeedbackId)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun receiveNewChecker(consumerTag: String, message: Delivery) {
        val checkerId = String(message.body)
        val checker = checkerRepository.getById(checkerId) ?: return
        val (code, resultMessage) = runner.buildChecker(checkerId, checker.dockerfile)
        println(code)
        println(resultMessage)
    }
}
