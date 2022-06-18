rootProject.name = "hwproj"
include("api", "common:repository", "common:rabbitmq", "common:test-utils", "runner", "integration-tests")

pluginManagement {
    val kotlinVersion: String by settings
    val springBootVersion: String by settings
    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
        id("org.springframework.boot") version springBootVersion
    }
}
