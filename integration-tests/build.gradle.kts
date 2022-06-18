val kotlinVersion: String by project
val springBootVersion: String by project
val mockKVersion: String by project
val rabbitMqVersion: String by project
val mockitoKotlinVersion: String by project

plugins {
    kotlin("jvm")
}

group = "ru.hse"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

dependencies {
    testImplementation(project(":common:test-utils"))
    testImplementation(project(":api"))
    testImplementation(project(":runner"))
    testImplementation(project(":common:repository"))
    testImplementation(project(":common:rabbitmq"))

    testImplementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("com.rabbitmq:amqp-client:$rabbitMqVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
    testImplementation("io.arivera.oss:embedded-rabbitmq:1.5.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:$mockitoKotlinVersion")
}
