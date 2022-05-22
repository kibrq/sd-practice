package ru.hse.core.checker

class CheckerRepository {
    fun uploadChecker(prototype: CheckerPrototype) = true

    fun getAllCheckers() = listOf<Checker>()

    fun getCheckerById(checkerId: Long) = Checker()
}
