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

/*
 * Service handling checker requests.
 */
@Service
class CheckerRequestsService(
    private val checkerRepository: CheckerRepository,
    private val publisher: HwprojPublisher
) {
    /*
     * Returns a checker with specified `id` or 404 response.
     */
    fun getChecker(id: String): ResponseEntity<Checker> {
        return checkerRepository.getById(id).orElseStatus(HttpStatus.NOT_FOUND)
    }

    /*
     * Returns all available checkers.
     */
    fun getAllCheckers(): List<Checker> = checkerRepository.getAll()

    /*
     * Uploads a checker. On success requests a message publish saying the checker image is ready to be built.
     */
    fun sendCreateCheckerRequest(prototype: CheckerPrototype): ResponseEntity<Int> {
        return checkerRepository.upload(prototype).runOrElseStatus(HttpStatus.BAD_REQUEST) {
            publisher.publishChecker(it)
        }
    }

    /*
     * Requests a message publish saying the submission with specified `id` is ready to be checked.
     */
    fun sendSubmissionCheckRequest(id: Int) {
        publisher.publishSubmission(id)
    }
}
