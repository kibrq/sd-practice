package ru.hse.hwproj.api.ipc

import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.MessageProperties
import org.springframework.stereotype.Service

@Service
class RabbitmqPublisher(
    connectionFactory: ConnectionFactory
) : HwprojPublisher, AutoCloseable {
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

    override fun publishChecker(id: Int) {
        val message = id.toString().toByteArray()
        checkersChannel.basicPublish("", "checkers_queue", MessageProperties.TEXT_PLAIN, message)
    }

    override fun publishSubmission(id: Int) {
        val message = id.toString().toByteArray()
        submissionsChannel.basicPublish("", "submissions_queue", MessageProperties.TEXT_PLAIN, message)
    }
}
