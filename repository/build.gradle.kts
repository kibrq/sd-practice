val springBootVersion: String by project
val postgresqlVersion: String by project
val h2Version:         String by project
val kotlinVersion:     String by project
val jooqVersion:       String by project

plugins {
    java
    id("kotlin")
    id("jooq-convention")
}

group = "ru.hse"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-jdbc:${springBootVersion}")
    implementation("org.postgresql:postgresql:${postgresqlVersion}")
    implementation("org.jooq:jooq:${jooqVersion}")

    testImplementation("com.h2database:h2:${h2Version}")
    testImplementation("org.springframework.boot:spring-boot-starter-test:${springBootVersion}")
    testImplementation("org.jetbrains.kotlin:kotlin-test:${kotlinVersion}")
}


jooq {
    configurations {
        getAt("main").apply {  // name of the jOOQ configuration
            generateSchemaSourceOnCompilation.set(true)  // default (can be omitted)
            jooqConfiguration.apply {
                generator.apply {
                    target.apply {
                        packageName = "ru.hse.repository"
                        directory = "src/main/generated/"  // default (can be omitted)
                    }
                }
            }
        }
    }
}
