package ru.hse.core.task

import java.util.*

class Task private constructor(
    val id: Long,
    val publishedDate: Date,
    val name: String,
    val description: String,
    val deadlineDate: Date,
    val checkerIdentifier: String
) {
    data class TaskPrototype(
        val name: String,
        val description: String,
        val deadlineDate: Date,
        val checkerIdentifier: String
    ) {
        fun task() = Task(
            id = idCounter++,
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

    companion object {
        private val calendar: Calendar = Calendar.getInstance()
        private var idCounter = 0L
    }
}

data class TaskView(
    val id: Long,
    val name: String,
    val deadlineDateString: String // "EEE MMM dd HH:mm:ss zzz yyyy"
)
