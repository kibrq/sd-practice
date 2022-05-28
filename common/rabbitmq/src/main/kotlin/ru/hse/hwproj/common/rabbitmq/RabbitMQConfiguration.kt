package ru.hse.hwproj.common.rabbitmq

import com.rabbitmq.client.ConnectionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
open class RabbitMQConfiguration(
    @Autowired private val environment: Environment
) {
    @Bean
    open fun rabbitConnection() : ConnectionFactory {
        return ConnectionFactory().apply {
            host = "rabbitmq-container"
            port = 5672
            username = "maxim"
            password = "maxim"
        }
    }
}
