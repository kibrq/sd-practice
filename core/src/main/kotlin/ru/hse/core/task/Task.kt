package ru.hse.core.task

import org.jooq.DSLContext
import ru.hse.core.Tables
import ru.hse.core.tables.records.TasksRecord
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicLong

object TaskIdHolder {
    var currentId = AtomicLong(0)
}
class Task(
    val id: Long,
    val publishedDate: LocalDateTime,
    val name: String,
    val description: String,
    val deadlineDate: LocalDateTime,
    val checkerIdentifier: String
) {
    fun view() = TaskView(
        id = id,
        name = name,
        deadlineDateString = deadlineDate.toString()
    )
}

data class TaskPrototype(
    val name: String,
    val description: String,
    val deadlineDate: LocalDateTime,
    val checkerIdentifier: String
) {
    fun task() = Task(
        id = TaskIdHolder.currentId.incrementAndGet(),
        publishedDate = LocalDateTime.now(),
        name = name,
        description = description,
        deadlineDate = deadlineDate,
        checkerIdentifier = checkerIdentifier
    )
}

data class TaskView(
    val id: Long,
    val name: String,
    val deadlineDateString: String, // "EEE MMM dd HH:mm:ss zzz yyyy"
)

class TaskRepository(private val dsl: DSLContext) {

    fun upload(prototype: TaskPrototype): Boolean {
        return dsl.insertInto(Tables.TASKS)
            .columns(Tables.TASKS.fields().asList())
            .values(prototype.task())
            .execute().let { it == 0 }
    }

    fun getAll(): Collection<Task> {
        return dsl.select()
            .from(Tables.TASKS)
            .fetch()
            .into(Task::class.java)
    }

    fun getTaskById(id: Long): Task? {
        return dsl.select()
            .from(Tables.TASKS)
            .where(Tables.TASKS.ID.eq(id.toInt()))
            .fetchOne()
            ?.into(Task::class.java)
    }

}
