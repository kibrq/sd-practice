package ru.hse.core.utils

import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.conf.RenderNameStyle
import org.jooq.conf.Settings
import org.jooq.impl.DSL
import java.sql.DriverManager


fun testDSL(): DSLContext {
    val url = "jdbc:h2:~/test;INIT=RUNSCRIPT FROM 'sql/init.sql'"
    val settings = Settings().withRenderNameStyle(RenderNameStyle.AS_IS) // Without this, it doesn't work...
    return DriverManager.getConnection(url, "sa", "").let {
        DSL.using(it, SQLDialect.H2, settings)
    }
}
