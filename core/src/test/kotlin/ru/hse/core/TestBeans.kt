package ru.hse.core

import org.springframework.context.annotation.AnnotationConfigApplicationContext
import kotlin.test.BeforeTest
import kotlin.test.Test

class TestBeans {
    lateinit var context: AnnotationConfigApplicationContext

    @BeforeTest
    fun setUp() {
        context = AnnotationConfigApplicationContext()
        context.scan("ru.hse.core")
        context.refresh()
    }

    @Test
    fun testDSL() {
        val dsl1 = context.getBean("getTestDSLContext")
        val dsl2 = context.getBean("getDevelopmentDSLContext")
    }
}
