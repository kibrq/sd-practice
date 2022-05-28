package ru.hse.hwproj

import org.springframework.context.annotation.AnnotationConfigApplicationContext
import ru.hse.hwproj.common.rabbitmq.RabbitMQConfiguration
import ru.hse.hwproj.common.repository.DataSourceConfiguration
import ru.hse.hwproj.common.repository.JooqConfiguration
import ru.hse.hwproj.common.repository.RepositoryConfiguration
import ru.hse.hwproj.runner.CheckerService

const val DEFAULT_NUMBER_OF_RUNNERS = 4

fun main(args: Array<String>) {
    val runnersCount = args.getOrNull(0)?.toIntOrNull() ?: DEFAULT_NUMBER_OF_RUNNERS

    val context = AnnotationConfigApplicationContext(
        RabbitMQConfiguration::class.java,
        JooqConfiguration::class.java,
        DataSourceConfiguration::class.java,
        RepositoryConfiguration::class.java,
        RunnerConfiguration::class.java
    )

    repeat(runnersCount) {
        val checkerService = context.getBean(CheckerService::class.java)
        println("Created checker ${checkerService.id}")
        checkerService.receiveTasks()
    }
}
