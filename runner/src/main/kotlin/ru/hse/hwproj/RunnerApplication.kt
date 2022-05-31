package ru.hse.hwproj

import org.springframework.context.annotation.AnnotationConfigApplicationContext
import ru.hse.hwproj.common.rabbitmq.RabbitMQConfiguration
import ru.hse.hwproj.common.repository.DataSourceConfiguration
import ru.hse.hwproj.common.repository.JooqConfiguration
import ru.hse.hwproj.common.repository.RepositoryConfiguration
import ru.hse.hwproj.runner.CheckerService
import ru.hse.hwproj.runner.ImageCreationService

const val DEFAULT_NUMBER_OF_RUNNERS = 4

fun main(args: Array<String>) {
    System.setProperty("org.jooq.no-logo", "true")
    System.setProperty("org.jooq.no-tips", "true")
    val runnersCount = args.getOrNull(0)?.toIntOrNull() ?: DEFAULT_NUMBER_OF_RUNNERS

    val context = AnnotationConfigApplicationContext(
        RunnerConfiguration::class.java,
        RabbitMQConfiguration::class.java,
        JooqConfiguration::class.java,
        DataSourceConfiguration::class.java,
        RepositoryConfiguration::class.java
    )

    val imageCreationService = context.getBean(ImageCreationService::class.java)
    println("Created image creation")
    imageCreationService.receiveCheckers()

    repeat(runnersCount) {
        val checkerService = context.getBean(CheckerService::class.java)
        println("Created checker ${checkerService.id}")
        checkerService.receiveTasks()
    }
}
