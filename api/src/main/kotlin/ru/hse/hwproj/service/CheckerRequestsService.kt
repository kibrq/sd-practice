package ru.hse.hwproj.service

import org.springframework.stereotype.Service
import ru.hse.core.checker.CheckerPrototype
import ru.hse.core.checker.CheckerRepository

@Service
class CheckerRequestsService(
    private val checkerRepository: CheckerRepository
) {
    fun sendCreateCheckerRequest(dockerfile: String) {
        val checker = checkerRepository.uploadChecker(CheckerPrototype(dockerfile))
    }

    fun sendSubmissionCheckRequest(submissionId: Long) {
    }
}
