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

    fun getChecker(checkerId: String): Checker? = checkerRepository.getById(checkerId)

    fun getAllCheckers(): List<Checker> = checkerRepository.getAll()

    fun sendCreateCheckerRequest(dockerfile: String): String? {
        val prototype = CheckerPrototype(dockerfile)
        val checkerId = checkerRepository.upload(prototype) ?: return null
        val message = checkerId.toByteArray()
        checkersChannel.basicPublish("", "checkers_queue", MessageProperties.TEXT_PLAIN, message)
        return checkerId
    }

    fun sendSubmissionCheckRequest(submissionId: Int) {
        val message = submissionId.toString().toByteArray()
        submissionsChannel.basicPublish("", "submissions_queue", MessageProperties.TEXT_PLAIN, message)
    }
}
