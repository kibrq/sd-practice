package ru.hse.core.task

import org.jooq.DSLContext
import ru.hse.core.Tables
import ru.hse.core.checker.Checker
import ru.hse.core.tables.records.TasksRecord
import java.time.LocalDateTime

data class TaskPrototype(
    val name: String,
    val description: String,
    val deadlineDate: LocalDateTime,
    val checkerId: String,
)

class TaskRepository(private val dsl: DSLContext) {

    private class Mapper {
        fun toRecord(prototype: TaskPrototype) = TasksRecord(
            0, prototype.name, LocalDateTime.now(), prototype.description, prototype.deadlineDate, prototype.checkerId
        )
    }

    private val mapper = Mapper()

    fun upload(prototype: TaskPrototype): Boolean {
        return dsl.insertInto(Tables.TASKS)
            .columns(Tables.TASKS.fields().asList())
            .values(mapper.toRecord(prototype))
            .execute().let { it == 0 }
    }

    fun getAll(): Collection<Task> {
        return dsl.select()
            .from(Tables.TASKS)
            .join(Tables.CHECKERS).on(Tables.CHECKERS.ID.eq(Tables.TASKS.CHECKER_ID))
            .fetch()
            .into(Task::class.java)
    }

    fun getById(id: String): Task? {
        return dsl.select()
            .from(Tables.TASKS)
            .join(Tables.CHECKERS).on(Tables.CHECKERS.ID.eq(Tables.TASKS.CHECKER_ID))
            .where(Tables.CHECKERS.ID.eq(id))
            .fetchOne()
            ?.into(Task::class.java)
    }

}

data class Task(
    val id: Int,
    val name: String,
    val publishedDate: LocalDateTime,
    val description: String,
    val deadlineDate: LocalDateTime,
    val checker: Checker,
)
