package ru.hse.hwproj.common.repository

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ComponentScans
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScans(
    ComponentScan("ru.hse.hwproj.common.repository.checker"),
    ComponentScan("ru.hse.hwproj.common.repository.submission"),
    ComponentScan("ru.hse.hwproj.common.repository.task"),
)
open class RepositoryConfiguration
