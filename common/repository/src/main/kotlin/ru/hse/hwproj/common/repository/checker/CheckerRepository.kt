package ru.hse.hwproj.common.repository.checker

interface CheckerRepository {
    fun upload(prototype: CheckerPrototype): Int?

    fun getByIds(ids: List<String>): List<Checker>

    fun getById(id: String) = getByIds(listOf(id)).getOrNull(0)

    fun getAll(): List<Checker>
}
