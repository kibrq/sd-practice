package ru.hse.hwproj.service

import org.springframework.stereotype.Service
import ru.hse.repository.checker.CheckerPrototype
import ru.hse.repository.checker.CheckerRepository

@Service
class CheckerRequestsService(
    private val checkerRepository: CheckerRepository
) {
    fun sendCreateCheckerRequest(dockerfile: String): Boolean {
        return checkerRepository.uploadChecker(CheckerPrototype(dockerfile))
    }

    fun sendSubmissionCheckRequest(submissionId: Long) {
    }
}
