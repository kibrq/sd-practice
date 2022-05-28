package ru.hse.hwproj.api.service

import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.MessageProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.hse.hwproj.common.repository.checker.Checker
import ru.hse.hwproj.common.repository.checker.CheckerPrototype
import ru.hse.hwproj.common.repository.checker.CheckerRepository

@Service
class CheckerRequestsService(
    @Autowired private val checkerRepository: CheckerRepository,
    @Autowired private val connectionFactory: ConnectionFactory,
) : AutoCloseable {

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
