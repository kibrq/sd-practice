package ru.hse.hwproj.common.rabbitmq

import com.rabbitmq.client.ConnectionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment

/*
 * RabbitMQ connection configuration.
 */
@Configuration
@PropertySource("classpath:rabbitmq-\${spring.profiles.active}.properties")
open class RabbitMQConfiguration(
    @Autowired private val environment: Environment
) {
    /*
     * Creates a RabbitMQ ConnectionFactory.
     */
    @Bean
    open fun rabbitConnection(): ConnectionFactory {
        return ConnectionFactory().apply {
            host = environment.getProperty("rabbitmq.host")
            environment.getProperty("rabbitmq.port")?.toIntOrNull()?.let {
                port = it
            }
            username = environment.getProperty("rabbitmq.username")
            password = environment.getProperty("rabbitmq.password")
        }
    }
}
