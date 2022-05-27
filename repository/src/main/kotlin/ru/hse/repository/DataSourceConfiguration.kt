package ru.hse.repository

import org.jooq.SQLDialect
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import javax.sql.DataSource

@Configuration
@PropertySource("classpath:database.properties")
open class DataSourceConfiguration(
    private val environment: Environment,
) {
    @Bean
    open fun dataSource(): DataSource {
        return PGSimpleDataSource().apply {
            setUrl(environment.getRequiredProperty("db.url"))
            user = environment.getRequiredProperty("db.user")
            password = environment.getRequiredProperty("db.password")
        }
    }

    @Bean
    open fun dialect(): SQLDialect = SQLDialect.POSTGRES
}
