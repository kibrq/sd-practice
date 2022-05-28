val springBootVersion: String by project
val jooqVersion: String by project
val rabbitMqVersion: String by project
val h2Version: String by project
val kotlinVersion: String by project

plugins {
    kotlin("jvm")
}

group = "ru.hse"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

dependencies {
    implementation("org.jooq:jooq:$jooqVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.rabbitmq:amqp-client:$rabbitMqVersion")
    implementation("io.arivera.oss:embedded-rabbitmq:1.5.0")
    implementation("org.springframework.boot:spring-boot-test:$springBootVersion")
    implementation("com.h2database:h2:$h2Version")
    implementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
    implementation("org.junit.jupiter:junit-jupiter:5.8.1")
}
