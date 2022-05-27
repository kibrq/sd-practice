package ru.hse.repository

import org.h2.jdbcx.JdbcDataSource
import org.jooq.SQLDialect
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import javax.sql.DataSource

@TestConfiguration
open class TestDataSourceConfiguration {
    @Bean
    open fun dataSource(): DataSource {
        return JdbcDataSource().apply {
            setURL("jdbc:h2:~/test;INIT=RUNSCRIPT FROM 'src/main/sql/init.sql'")
            user = "sa"
            password = ""
        }
    }

    @Bean
    open fun dialect(): SQLDialect = SQLDialect.H2
}
