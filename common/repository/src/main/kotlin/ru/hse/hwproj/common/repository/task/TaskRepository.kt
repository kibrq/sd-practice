package ru.hse.hwproj.common.repository.task

/*
 * Repository for storing tasks and uploading them via TaskPrototype.
 */
interface TaskRepository {
    /*
     * Creates a task from `prototype` and saves it.
     */
    fun upload(prototype: TaskPrototype): Int?

    /*
     * Returns a list of all tasks with ids in `ids`.
     */
    fun getByIds(ids: List<Int>): List<Task>

    /*
     * Returns a task with a specified `id`.
     */
    fun getById(id: Int) = getByIds(listOf(id)).getOrNull(0)

    /*
     * Returns all available tasks.
     */
    fun getAll(): List<Task>
}
