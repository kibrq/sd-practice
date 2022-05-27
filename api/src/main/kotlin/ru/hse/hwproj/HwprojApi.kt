package ru.hse.hwproj

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan("ru.hse.repository")
class HwprojApi

fun main(args: Array<String>) {
    runApplication<HwprojApi>(*args)
}
