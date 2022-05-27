package ru.hse.hwproj.service

import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.MessageProperties
import org.springframework.stereotype.Service
import ru.hse.repository.checker.CheckerPrototype
import ru.hse.repository.checker.CheckerRepository
import java.util.*

@Service
class CheckerRequestsService(
    private val checkerRepository: CheckerRepository
) : AutoCloseable {
    private val connectionFactory =
        ConnectionFactory().apply {
            host = "localhost"
            port = 5672
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

    fun sendCreateCheckerRequest(dockerfile: String): String? {
        val id = UUID.randomUUID().toString()
        val prototype = CheckerPrototype(id, dockerfile)
        return if (checkerRepository.upload(prototype)) {
            id
        } else {
            null
        }
    }

    fun sendSubmissionCheckRequest(submissionId: Int) {
        val message = submissionId.toString().toByteArray()
        channel.basicPublish("", "submissions_queue", MessageProperties.TEXT_PLAIN, message)
    }
}
