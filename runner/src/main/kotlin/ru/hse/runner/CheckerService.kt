package ru.hse.runner

import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.Delivery

class CheckerService : AutoCloseable {
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
        channel.basicConsume("submissions_queue", true, ::receiveMessage) { _ -> }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun receiveMessage(consumerTag: String, message: Delivery) {
        val taskId = String(message.body).toLong()
        runner.run(taskId)
    }
}
