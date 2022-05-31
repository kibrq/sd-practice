package ru.hse.hwproj.testutils

import com.rabbitmq.client.ConnectionFactory
import io.arivera.oss.embedded.rabbitmq.BaseVersion
import io.arivera.oss.embedded.rabbitmq.EmbeddedRabbitMq
import io.arivera.oss.embedded.rabbitmq.EmbeddedRabbitMqConfig
import io.arivera.oss.embedded.rabbitmq.OfficialArtifactRepository
import io.arivera.oss.embedded.rabbitmq.util.ArchiveType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.Scope
import org.springframework.core.env.Environment

object LazyEmbeddedRabbitMq {
    private val rabbitMqVersion = BaseVersion("3.10.2", "25.0", ArchiveType.TAR_XZ)

    val rabbitMq by lazy {
        EmbeddedRabbitMq(
            EmbeddedRabbitMqConfig.Builder()
                .version(rabbitMqVersion)
                .downloadFrom(OfficialArtifactRepository.GITHUB)
                .defaultRabbitMqCtlTimeoutInMillis(10000)
                .rabbitMqServerInitializationTimeoutInMillis(30000)
                .build()
        )
    }
}

@TestConfiguration
@PropertySource("classpath:rabbitmq-test.properties")
open class TestRabbitMQConfiguration(
    @Autowired private val environment: Environment
) {
    @Bean
    @Scope("prototype")
    open fun rabbitConnection(): ConnectionFactory {
        return ConnectionFactory().apply {
            host = environment.getProperty("rabbitmq.host");
            virtualHost = environment.getProperty("rabbitmq.virtual-host");
            username = environment.getProperty("rabbitmq.username");
            password = environment.getProperty("rabbitmq.password");
        }
    }
}
