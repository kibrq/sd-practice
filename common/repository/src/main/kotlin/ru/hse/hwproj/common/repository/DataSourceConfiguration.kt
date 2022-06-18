package ru.hse.hwproj.common.repository

import org.jooq.SQLDialect
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import javax.sql.DataSource

/*
 * Database connection configuration.
 */
@Configuration
@PropertySource("classpath:/database-\${spring.profiles.active}.properties")
open class DataSourceConfiguration(
    private val environment: Environment
) {
    /*
     * Creates a Postgres DataSource with `environment` configuration.
     */
    @Bean
    open fun dataSource(): DataSource {
        val url = "${environment.getProperty("db.protocol")}://" +
            "${environment.getProperty("db.host")}:${environment.getProperty("db.port")}/" +
            "${environment.getProperty("db.name")}"
        return PGSimpleDataSource().apply {
            setUrl(url)
            user = environment.getRequiredProperty("db.user")
            password = environment.getRequiredProperty("db.password")
        }
    }

    /*
     * Returns an SQLDialect as Postgres.
     */
    @Bean
    open fun dialect(): SQLDialect = SQLDialect.POSTGRES
}
