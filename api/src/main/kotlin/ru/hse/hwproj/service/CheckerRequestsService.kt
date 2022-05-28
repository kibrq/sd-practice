package ru.hse.hwproj.service

import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.MessageProperties
import org.springframework.stereotype.Service
import ru.hse.repository.checker.Checker
import ru.hse.repository.checker.CheckerPrototype
import ru.hse.repository.checker.CheckerRepository

@Service
class CheckerRequestsService(
    private val checkerRepository: CheckerRepository
) : AutoCloseable {
    private val connectionFactory =
        ConnectionFactory().apply {
            host = "rabbitmq-container"
            port = 5672
            username = "maxim"
            password = "maxim"
        }

    private val connection = connectionFactory.newConnection()
    private val channel = connection.createChannel()

    init {
        channel.queueDeclare("submissions_queue", false, false, false, null)
    }

    override fun close() {
        channel.close()
        connection.close()
    }

    fun getChecker(checkerId: String): Checker? = checkerRepository.getById(checkerId)

    fun getAllCheckers(): List<Checker> = checkerRepository.getAll()

    fun sendCreateCheckerRequest(dockerfile: String): String? {
        val prototype = CheckerPrototype(dockerfile)
        return checkerRepository.upload(prototype)
    }

    fun sendSubmissionCheckRequest(submissionId: Int) {
        val message = submissionId.toString().toByteArray()
        channel.basicPublish("", "submissions_queue", MessageProperties.TEXT_PLAIN, message)
    }
}
