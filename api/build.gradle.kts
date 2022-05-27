val kotlinVersion: String by project
val springBootVersion: String by project
val openApiVersion: String by project

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot")
    application
}

application {
    mainClass.set("ru.hse.hwproj.HwprojApiKt")
}

group = "ru.hse"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

dependencies {
    implementation(project(":repository"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
    implementation("org.springdoc:springdoc-openapi-kotlin:$openApiVersion")
    implementation("org.springdoc:springdoc-openapi-ui:$openApiVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
}
