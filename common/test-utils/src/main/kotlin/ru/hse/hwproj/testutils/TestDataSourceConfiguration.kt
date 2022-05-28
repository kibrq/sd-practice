package ru.hse.hwproj.testutils

import org.h2.jdbcx.JdbcDataSource
import org.jooq.SQLDialect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import javax.sql.DataSource

@TestConfiguration
@PropertySource("classpath:database-test.properties")
open class TestDataSourceConfiguration(
    @Autowired private val environment: Environment,
) {
    @Bean
    open fun dataSource(): DataSource {
        return JdbcDataSource().apply {
            setURL(environment.getProperty("db.url"))
            user = environment.getProperty("db.user")
            password = environment.getProperty("db.password")
        }
    }

    @Bean
    open fun dialect(): SQLDialect = SQLDialect.H2
}
