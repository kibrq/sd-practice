package ru.hse.hwproj.runner

import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.Delivery
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import ru.hse.hwproj.common.repository.checker.CheckerRepository

@Component
@Scope("prototype")
class ImageCreationService(
    private val checkerRepository: CheckerRepository,
    connectionFactory: ConnectionFactory,
    private val runner: Runner
) : AutoCloseable {
    private val connection = connectionFactory.newConnection()
    private val channel = connection.createChannel()

    init {
        channel.queueDeclare("checkers_queue", false, false, false, null)
    }

    override fun close() {
        channel.close()
        connection.close()
    }

    fun receiveCheckers() {
        channel.basicConsume("checkers_queue", true, ::receiveNewChecker) { _ -> }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun receiveNewChecker(consumerTag: String, message: Delivery) {
        val checkerId = String(message.body)
        val checker = checkerRepository.getById(checkerId)
        if (checker != null) {
            runner.build(checkerId, checker.dockerfile)
        }
    }
}
