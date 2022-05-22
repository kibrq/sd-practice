package ru.hse.core.checker

import org.springframework.stereotype.Repository

@Repository
class CheckerRepository {
    fun createChecker(dockerfile: String): Checker = TODO()

    fun getCheckerById(checkerId: Long): Checker = TODO()
}
