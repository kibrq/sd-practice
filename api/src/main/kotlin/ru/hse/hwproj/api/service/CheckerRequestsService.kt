package ru.hse.hwproj.api.service

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import ru.hse.hwproj.api.ipc.HwprojPublisher
import ru.hse.hwproj.api.utils.orElseStatus
import ru.hse.hwproj.api.utils.runOrElseStatus
import ru.hse.hwproj.common.repository.checker.Checker
import ru.hse.hwproj.common.repository.checker.CheckerPrototype
import ru.hse.hwproj.common.repository.checker.CheckerRepository

@Service
class CheckerRequestsService(
    private val checkerRepository: CheckerRepository,
    private val publisher: HwprojPublisher
) {
    fun getChecker(id: String): ResponseEntity<Checker> {
        return checkerRepository.getById(id).orElseStatus(HttpStatus.NOT_FOUND)
    }

    fun getAllCheckers(): List<Checker> = checkerRepository.getAll()

    fun sendCreateCheckerRequest(prototype: CheckerPrototype): ResponseEntity<Int> {
        return checkerRepository.upload(prototype).runOrElseStatus(HttpStatus.BAD_REQUEST) {
            publisher.publishChecker(it)
        }
    }

    fun sendSubmissionCheckRequest(id: Int) {
        publisher.publishSubmission(id)
    }
}
