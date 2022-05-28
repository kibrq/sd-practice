package ru.hse.hwproj

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
class HwprojApi

fun main(args: Array<String>) {
    System.setProperty("org.jooq.no-logo", "true")
    runApplication<HwprojApi>(*args)
}
