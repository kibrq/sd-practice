package ru.hse.core

import ru.hse.core.checker.CheckerPrototype
import ru.hse.core.checker.CheckerRepository
import ru.hse.core.utils.testDSL
import kotlin.test.Test

class Test {
    @Test
    fun testDatabase() {
        val repository = CheckerRepository(testDSL())
        repository.upload(CheckerPrototype("as"))
        repository.getAll().forEach{ println(it.dockerfile) }
    }
}
