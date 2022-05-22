package ru.hse.core.checker

import org.springframework.stereotype.Component

@Component
class CheckerRepository {
    fun createChecker(dockerfile: String): Checker = TODO(dockerfile)

    fun getCheckerById(checkerId: Long): Checker = TODO(checkerId.toString())
}
