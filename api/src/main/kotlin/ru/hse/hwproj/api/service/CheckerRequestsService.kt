package ru.hse.hwproj.api.service

import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.MessageProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import ru.hse.hwproj.api.utils.orElseStatus
import ru.hse.hwproj.api.utils.runOrElseStatus
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

    fun getChecker(id: String): ResponseEntity<Checker> {
        return checkerRepository.getById(id).orElseStatus(HttpStatus.NOT_FOUND)
    }

    fun getAllCheckers(): List<Checker> = checkerRepository.getAll()

    fun sendCreateCheckerRequest(prototype: CheckerPrototype): ResponseEntity<Int> {
        return checkerRepository.upload(prototype).runOrElseStatus(HttpStatus.BAD_REQUEST) {
            val message = it.toString().toByteArray()
            checkersChannel.basicPublish("", "checkers_queue", MessageProperties.TEXT_PLAIN, message)
        }
    }

    fun sendSubmissionCheckRequest(id: Int) {
        val message = id.toString().toByteArray()
        submissionsChannel.basicPublish("", "submissions_queue", MessageProperties.TEXT_PLAIN, message)
    }
}
