package ru.hse.hwproj

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ComponentScans
import org.springframework.context.annotation.Configuration

@Configuration
@EnableAutoConfiguration
@ComponentScan("ru.hse.hwproj")
@ComponentScan("ru.hse.core")
class HwprojApi

fun main(args: Array<String>) {
    runApplication<HwprojApi>(*args)
}
