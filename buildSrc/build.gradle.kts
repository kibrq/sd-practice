plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("nu.studer:gradle-jooq-plugin:7.1.1")
    implementation("org.jooq:jooq-codegen:3.16.6")
}
