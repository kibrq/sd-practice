package ru.hse.hwproj.common.repository

import org.jooq.ExecuteContext
import org.jooq.SQLDialect
import org.jooq.conf.RenderQuotedNames
import org.jooq.conf.Settings
import org.jooq.impl.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator
import javax.sql.DataSource

/*
 * Exception translator for jooq exceptions.
 */
object ExceptionTranslator : DefaultExecuteListener() {
    override fun exception(context: ExecuteContext) {
        val dialect = context.configuration().dialect()
        val translator = SQLErrorCodeSQLExceptionTranslator(dialect.name)

        context.sqlException()?.let {
            context.exception(translator.translate("Access database using Jooq", context.sql(), it))
        }
    }
}

/*
 * Jooq connection configuration.
 */
@Configuration
open class JooqConfiguration(
    private val dataSource: DataSource
) {
    /*
     * Creates a  TransactionAwareDataSourceProxy from `dataSource`.
     */
    @Bean
    open fun transactionAwareDataSource() = TransactionAwareDataSourceProxy(dataSource)

    /*
     * Creates a configuration with a specified SQL dialect.
     */
    @Bean
    open fun configuration(dialect: SQLDialect) = DefaultConfiguration().apply {
        set(DataSourceConnectionProvider(transactionAwareDataSource()))
        set(DefaultExecuteListenerProvider(ExceptionTranslator))
        set(dialect)
        set(Settings().withRenderQuotedNames(RenderQuotedNames.NEVER))
    }

    /*
     * Creates a DSL context for a specified configuration.
     */
    @Bean
    open fun dsl(configuration: DefaultConfiguration) = DefaultDSLContext(configuration)
}
