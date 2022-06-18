package ru.hse.hwproj.common.repository.checker

/*
 * Repository for storing checkers and uploading them via CheckerPrototype.
 */
interface CheckerRepository {
    /*
     * Creates a checker from `prototype` and saves it.
     */
    fun upload(prototype: CheckerPrototype): Int?

    /*
     * Returns a list of all checkers with ids in `ids`.
     */
    fun getByIds(ids: List<String>): List<Checker>

    /*
     * Returns a checker with a specified `id`.
     */
    fun getById(id: String) = getByIds(listOf(id)).getOrNull(0)

    /*
     * Returns all available checkers.
     */
    fun getAll(): List<Checker>
}
