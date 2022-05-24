package ru.hse.core

import org.h2.jdbcx.JdbcDataSource
import org.jooq.SQLDialect
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import javax.sql.DataSource

@Configuration
@Profile("test")
open class TestDataSourceConfiguration {

    @Bean
    open fun dataSource(): DataSource {
        return JdbcDataSource().apply {
            url = "jdbc:h2:~/test;INIT=RUNSCRIPT FROM 'sql/init.sql'"
            user = "sa"
            password = ""
        }
    }

    @Bean
    open fun dialect(): SQLDialect = SQLDialect.H2
}
