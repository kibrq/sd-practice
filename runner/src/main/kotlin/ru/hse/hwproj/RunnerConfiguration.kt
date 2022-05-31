package ru.hse.hwproj

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@ComponentScan("ru.hse.hwproj.runner")
@PropertySource("classpath:/application.properties")
class RunnerConfiguration
