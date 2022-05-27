import nu.studer.gradle.jooq.JooqEdition

val h2Version: String by project
val jooqVersion: String by project
val sqlRoot: String by project

plugins {
    id("nu.studer.jooq")
    java
}

repositories {
    mavenCentral()
}

dependencies {
    jooqGenerator("com.h2database:h2:$h2Version")
}

jooq {
    version.set(jooqVersion)
    edition.set(JooqEdition.OSS)

    configurations {
        create("main") {
            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.WARN
                jdbc.apply {
                    driver = "org.h2.Driver"
                    url = "jdbc:h2:~/test;AUTO_SERVER=TRUE;INIT=RUNSCRIPT FROM '${project.rootDir}/$sqlRoot/init.sql'"
                    user = "sa"
                    password = ""
                }
                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"
                    database.apply {
                        name = "org.jooq.meta.h2.H2Database"
                        inputSchema = "PUBLIC"
                    }
                    generate.apply {
                        isImmutablePojos = true
                        isRecords = true
                    }
                }
            }
        }
    }
}
