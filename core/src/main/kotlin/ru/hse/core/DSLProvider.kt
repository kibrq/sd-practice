package ru.hse.core

import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.conf.RenderNameStyle
import org.jooq.conf.Settings
import org.jooq.impl.DSL
import org.springframework.context.annotation.Bean
import java.sql.DriverManager

@Bean(name = ["dsl-test"])
fun getDevelopmentDSLContext(): DSLContext? = DriverManager.getConnection("jdbc:h2:~/test;INIT=RUNSCRIPT FROM 'sql/init.sql'")?.let {
    DSL.using(it, SQLDialect.H2, Settings().withRenderNameStyle(RenderNameStyle.AS_IS))
}

@Bean(name = ["dsl-dev"])
fun getTestDSLContext() : DSLContext? = DriverManager.getConnection("")?.let {
    DSL.using(it, SQLDialect.POSTGRES)
}
