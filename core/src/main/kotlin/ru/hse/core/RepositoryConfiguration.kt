package ru.hse.core

import org.jooq.impl.DefaultDSLContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.hse.core.checker.CheckerRepository
import ru.hse.core.submission.SubmissionRepository
import ru.hse.core.task.TaskRepository

@Configuration
open class RepositoryConfiguration {

    @Bean
    open fun taskRepository(defaultDSLContext: DefaultDSLContext): TaskRepository {
        return TaskRepository(defaultDSLContext)
    }

    @Bean
    open fun checkerRepository(defaultDSLContext: DefaultDSLContext): CheckerRepository {
        return CheckerRepository(defaultDSLContext)
    }

    @Bean
    open fun submissionRepository(defaultDSLContext: DefaultDSLContext): SubmissionRepository {
        return SubmissionRepository(defaultDSLContext)
    }
}
