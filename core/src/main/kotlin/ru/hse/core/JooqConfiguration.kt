package ru.hse.core

import org.jooq.DSLContext
import org.jooq.ExecuteContext
import org.jooq.SQLDialect
import org.jooq.conf.RenderNameStyle
import org.jooq.conf.Settings
import org.jooq.impl.*
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator
import org.springframework.jdbc.support.SQLExceptionTranslator
import javax.sql.DataSource


class ExceptionTranslator : DefaultExecuteListener() {
    override fun exception(context: ExecuteContext) {
        val dialect: SQLDialect = context.configuration().dialect()
        val translator: SQLExceptionTranslator = SQLErrorCodeSQLExceptionTranslator(dialect.name)
        context.exception(
            translator
                .translate("Access database using Jooq", context.sql(), context.sqlException()!!)
        )
    }
}



@Configuration
@PropertySource("classpath:database.properties")
open class JooqConfiguration(
    private val environment: Environment,
    @Value("\${spring.profiles.active}") private val activeProfile: String,
) {

    @Bean
    open fun dataSource(): DataSource? {
        val dataSource = PGSimpleDataSource()
        dataSource.setUrl(environment.getRequiredProperty("db.url"))
        dataSource.user = environment.getRequiredProperty("db.user")
        dataSource.password = environment.getRequiredProperty("db.password")
        return dataSource
    }

    @Bean
    open fun transactionAwareDataSource(): TransactionAwareDataSourceProxy? {
        return TransactionAwareDataSourceProxy(dataSource()!!)
    }

    @Bean
    open fun transactionManager(): DataSourceTransactionManager? {
        return DataSourceTransactionManager(dataSource()!!)
    }

    @Bean
    open fun connectionProvider(): DataSourceConnectionProvider? {
        return DataSourceConnectionProvider(transactionAwareDataSource())
    }

    @Bean
    open fun exceptionTransformer(): ExceptionTranslator? {
        return ExceptionTranslator()
    }

    @Bean
    open fun configuration(): DefaultConfiguration? {
        val jooqConfiguration = DefaultConfiguration()
        jooqConfiguration.set(connectionProvider())
        jooqConfiguration.set(DefaultExecuteListenerProvider(exceptionTransformer()))
        val sqlDialectName = environment.getRequiredProperty("db.dialect")
        val dialect = SQLDialect.valueOf(sqlDialectName)
        jooqConfiguration.set(dialect)
        return jooqConfiguration
    }

    @Bean
    open fun dsl(): DefaultDSLContext? {
        return DefaultDSLContext(configuration())
    }
}
