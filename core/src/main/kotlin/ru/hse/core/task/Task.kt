package ru.hse.core.task

import java.util.*

class Task private constructor(
    val id: Long,
    val publishedDate: Date,
    val name: String,
    val description: String,
    val deadlineDate: Date,
    val checkerIdentifier: String,
) {
    data class Prototype(
        val name: String,
        val description: String,
        val deadlineDate: Date,
        val checkerIdentifier: String,
    ) {
        private val calendar: Calendar = Calendar.getInstance()

        fun task() = Task(
            id = 1,
            publishedDate = calendar.time,
            name = name,
            description = description,
            deadlineDate = deadlineDate,
            checkerIdentifier = checkerIdentifier
        )
    }

    fun view() = TaskView(
        id = id,
        name = name,
        deadlineDateString = deadlineDate.toString()
    )
}

data class TaskView(
    val id: Long,
    val name: String,
    val deadlineDateString: String // "EEE MMM dd HH:mm:ss zzz yyyy"
)
