package ru.hse.hwproj.testutils

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    classes = [
        TestRabbitMQConfiguration::class
    ]
)
open class EmbeddedRabbitMQTest {
    companion object {
        @BeforeAll
        @JvmStatic
        fun beforeAll() = LazyEmbeddedRabbitMq.rabbitMq.start()

        @AfterAll
        @JvmStatic
        fun afterAll() = LazyEmbeddedRabbitMq.rabbitMq.stop()
    }
}

