import groovy.time.TimeCategory
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.*

plugins {
    kotlin("jvm") version "1.6.10"
    application
}

group = "ru.hse"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.h0tk3y.betterParse:better-parse:0.4.3")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.6.0")
    implementation("commons-io:commons-io:2.11.0")
    implementation("com.github.ajalt.clikt:clikt:3.4.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

application {
    mainClass.set("ru.hse.shell.MainKt")
}

// https://gist.github.com/ethanmdavidson/a73147ce5bdcde4a87554c7303bae8f4
var testResults by extra(mutableListOf<TestOutcome>()) // Container for tests summaries

tasks.withType<Test>().configureEach {
    val testTask = this

    testLogging {
        events = setOf(
            TestLogEvent.FAILED,
            TestLogEvent.SKIPPED,
            TestLogEvent.STANDARD_OUT,
            TestLogEvent.STANDARD_ERROR
        )

        exceptionFormat = TestExceptionFormat.FULL
        showExceptions = true
        showCauses = true
        showStackTraces = true
    }

    //addTestListener is a workaround https://github.com/gradle/kotlin-dsl-samples/issues/836
    addTestListener(object : TestListener {
        override fun beforeSuite(suite: TestDescriptor) {}
        override fun beforeTest(testDescriptor: TestDescriptor) {}
        override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {}
        override fun afterSuite(desc: TestDescriptor, result: TestResult) {
            if (desc.parent != null) return // Only summarize results for whole modules

            val summary = TestOutcome().apply {
                add(
                    "${testTask.project.name}:${testTask.name} results: ${result.resultType} " +
                            "(" +
                            "${result.testCount} tests, " +
                            "${result.successfulTestCount} successes, " +
                            "${result.failedTestCount} failures, " +
                            "${result.skippedTestCount} skipped" +
                            ") " +
                            "in ${TimeCategory.minus(Date(result.endTime), Date(result.startTime))}"
                )
                add("Report file: ${testTask.reports.html.entryPoint}")
            }

            // Add reports in `testsResults`, keep failed suites at the end
            if (result.resultType == TestResult.ResultType.SUCCESS) {
                testResults.add(0, summary)
            } else {
                testResults.add(summary)
            }
        }
    })
}

gradle.buildFinished {
    if (testResults.isNotEmpty()) {
        printResults(testResults)
    }
}

fun printResults(allResults: List<TestOutcome>) {
    val maxLength = allResults.map { it.maxWidth() }
        .max() ?: 0

    println("┌${"─".repeat(maxLength)}┐")

    println(allResults.joinToString("├${"─".repeat(maxLength)}┤\n") { testOutcome ->
        testOutcome.lines.joinToString("│\n│", "│", "│") {
            it + " ".repeat(maxLength - it.length)
        }
    })

    println("└${"─".repeat(maxLength)}┘")
}

data class TestOutcome(val lines: MutableList<String> = mutableListOf()) {
    fun add(line: String) {
        lines.add(line)
    }

    fun maxWidth(): Int {
        return lines.maxBy { it.length }?.length ?: 0
    }
}
