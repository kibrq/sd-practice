package ru.hse.hwproj.common.repository.task

import org.jooq.impl.DefaultDSLContext
import org.springframework.stereotype.Component
import ru.hse.hwproj.common.repository.Sequences
import ru.hse.hwproj.common.repository.Tables
import ru.hse.hwproj.common.repository.utils.withinTry
import java.time.LocalDateTime

data class Task(
    val id: Int,
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
)

data class TaskView(
    val id: Int,
    val name: String,
    val deadlineDateString: String, // "EEE MMM dd HH:mm:ss zzz yyyy"
)

@Component
class TaskRepository(
    private val dsl: DefaultDSLContext
) {
    fun upload(prototype: TaskPrototype): Int? {
        return withinTry {
            dsl.insertInto(Tables.TASKS)
                .columns(Tables.TASKS.fields().asList())
                .values(
                    Sequences.TASK_ID_SEQ.nextval(),
                    prototype.name,
                    LocalDateTime.now(),
                    prototype.description,
                    prototype.deadlineDate,
                    prototype.checkerIdentifier
                )
                .returningResult(Tables.TASKS.ID)
                .fetchOne()
                ?.value1()
        }
    }

    fun getByIds(ids: List<Int>): List<Task> {
        return dsl.select()
            .from(Tables.TASKS)
            .where(Tables.TASKS.ID.`in`(ids))
            .fetch()
            .into(Task::class.java)
    }

    fun getById(id: Int): Task? {
        return getByIds(listOf(id)).getOrNull(0)
    }

    fun getAll(): List<Task> {
        return dsl.select()
            .from(Tables.TASKS)
            .fetch()
            .into(Task::class.java)
    }
}
