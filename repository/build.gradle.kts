import org.jooq.meta.jaxb.Logging

plugins {
    kotlin("jvm")
    java
    id("nu.studer.jooq") version "7.1.1"
}

group = "ru.hse"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

dependencies {
    compileOnly("org.jooq:jooq-codegen:3.16.6")
    implementation("org.jooq:jooq:3.16.6")
    jooqGenerator("org.postgresql:postgresql:42.3.4")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-jdbc:2.7.0")
    implementation("org.postgresql:postgresql:42.3.4")

    testImplementation("com.h2database:h2:2.1.212")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.6.21")
}

buildscript {
    configurations["classpath"].resolutionStrategy.eachDependency {
        if (requested.group == "org.jooq") {
            useVersion("3.16.6")
        }
    }
}

jooq {
    version.set("3.16.6")
    edition.set(nu.studer.gradle.jooq.JooqEdition.OSS)

    configurations {
        create("main") {  // name of the jOOQ configuration
            generateSchemaSourceOnCompilation.set(false)

            jooqConfiguration.apply {
                logging = Logging.WARN
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://database:5432/hwproj"
                    user = "hwproj"
                    password = "hwproj"
                }
                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                    }
                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isImmutablePojos = false
                        isFluentSetters = false
                    }
                    target.apply {
                        packageName = "ru.hse.repository"
                        directory = "build/generated-src/jooq/main"  // default (can be omitted)
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}
