package ru.hse.hwproj.service

import org.springframework.stereotype.Service
import ru.hse.core.checker.CheckerRepository

@Service
class CheckerRequestsService(
    private val checkerRepository: CheckerRepository
) {
    fun sendCreateCheckerRequest(dockerfile: String): String {
        val checker = checkerRepository.createChecker(dockerfile)
        return checker.imageIdentifier
    }

    fun sendSubmissionCheckRequest(submissionId: Long) {
    }
}
