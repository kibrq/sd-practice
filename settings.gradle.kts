rootProject.name = "hwproj"
include("api", "repository", "runner")

pluginManagement {
    val kotlinVersion: String by settings
    val springBootVersion: String by settings
    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
        id("org.springframework.boot") version springBootVersion
    }
}
