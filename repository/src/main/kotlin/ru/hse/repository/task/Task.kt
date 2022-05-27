package ru.hse.repository.task

import org.jooq.impl.DefaultDSLContext
import org.springframework.stereotype.Component
import ru.hse.repository.Tables
import ru.hse.repository.tables.records.TasksRecord
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicLong

object TaskIdHolder {
    var currentId = AtomicLong(0)
}

data class Task(
    val id: Long,
    val name: String,
    val publishedDate: LocalDateTime,
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
    fun task() = TasksRecord(
        TaskIdHolder.currentId.incrementAndGet().toInt(),
        name,
        LocalDateTime.now(),
        description,
        deadlineDate,
        checkerIdentifier
    )
}

data class TaskView(
    val id: Long,
    val name: String,
    val deadlineDateString: String, // "EEE MMM dd HH:mm:ss zzz yyyy"
)

@Component
class TaskRepository(
    private val dsl: DefaultDSLContext
) {
    fun upload(prototype: TaskPrototype): Int? {
        return try {
            val task = prototype.task()
            dsl.insertInto(Tables.TASKS)
                .columns(Tables.TASKS.fields().asList())
                .values(task)
                .execute()
            task.id
        } catch (e: Exception) {
            null
        }
    }

    fun getAll(): List<Task> {
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
