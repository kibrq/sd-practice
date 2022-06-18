val kotlinVersion: String by project
val springBootVersion: String by project
val rabbitMqVersion: String by project

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot")
    application
}

application {
    mainClass.set("ru.hse.hwproj.RunnerApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dspring.profiles.active=development")
}

group = "ru.hse"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

dependencies {
    implementation(project(":common:repository"))
    implementation(project(":common:rabbitmq"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.rabbitmq:amqp-client:$rabbitMqVersion")
    implementation("org.springframework.boot:spring-boot-starter:$springBootVersion")

    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
}
