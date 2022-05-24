package ru.hse.core

import org.jooq.ExecuteContext
import org.jooq.SQLDialect
import org.jooq.conf.RenderNameStyle
import org.jooq.conf.Settings
import org.jooq.impl.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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
open class JooqConfiguration(
    private val environment: Environment,
) {

    @Bean
    open fun transactionAwareDataSource(dataSource: DataSource): TransactionAwareDataSourceProxy {
        return TransactionAwareDataSourceProxy(dataSource)
    }

    @Bean
    open fun transactionManager(@Qualifier("dataSource") dataSource: DataSource): DataSourceTransactionManager {
        return DataSourceTransactionManager(dataSource)
    }

    @Bean
    open fun connectionProvider(transactionAwareDataSourceProxy: TransactionAwareDataSourceProxy): DataSourceConnectionProvider {
        return DataSourceConnectionProvider(transactionAwareDataSourceProxy)
    }

    @Bean
    open fun exceptionTransformer(): ExceptionTranslator {
        return ExceptionTranslator()
    }

    @Bean
    open fun configuration(
        connectionProvider: DataSourceConnectionProvider,
        exceptionTranslator: ExceptionTranslator,
        dialect: SQLDialect,
    ): DefaultConfiguration {
        return DefaultConfiguration().apply {
            set(connectionProvider)
            set(DefaultExecuteListenerProvider(exceptionTranslator))
            set(dialect)
            set(Settings().withRenderNameStyle(RenderNameStyle.AS_IS))
        }
    }

    @Bean
    open fun dsl(configuration: DefaultConfiguration): DefaultDSLContext {
        return DefaultDSLContext(configuration)
    }
}
