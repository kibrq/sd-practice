val springBootVersion: String by project
val rabbitMqVersion: String by project

plugins {
    kotlin("jvm")
}

group = "ru.hse"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.rabbitmq:amqp-client:$rabbitMqVersion")
    implementation("org.springframework.boot:spring-boot-starter:$springBootVersion")
}
