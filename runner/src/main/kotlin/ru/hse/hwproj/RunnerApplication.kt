package ru.hse.hwproj

import org.springframework.context.annotation.AnnotationConfigApplicationContext
import ru.hse.hwproj.common.rabbitmq.RabbitMQConfiguration
import ru.hse.hwproj.common.repository.DataSourceConfiguration
import ru.hse.hwproj.common.repository.JooqConfiguration
import ru.hse.hwproj.common.repository.RepositoryConfiguration
import ru.hse.hwproj.runner.CheckerService

fun main() {
    System.setProperty("org.jooq.no-logo", "true")
    System.setProperty("org.jooq.no-tips", "true")

    val context = AnnotationConfigApplicationContext(
        RunnerConfiguration::class.java,
        RabbitMQConfiguration::class.java,
        JooqConfiguration::class.java,
        DataSourceConfiguration::class.java,
        RepositoryConfiguration::class.java
    )

    val checkerService = context.getBean(CheckerService::class.java)
    println("Created checker")
    checkerService.startReceivingTasks()
}
