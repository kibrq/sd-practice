import groovy.time.TimeCategory
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.*

plugins {
    kotlin("jvm")
}

tasks.register<GradleBuild>("buildApiAndRunner") {
    tasks = listOf("clean", ":api:build", ":runner:build")
}

allprojects {
    repositories {
        mavenCentral()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    // https://gist.github.com/ethanmdavidson/a73147ce5bdcde4a87554c7303bae8f4
    data class TestOutcome(val lines: MutableList<String> = mutableListOf()) {
        fun add(line: String) {
            lines.add(line)
        }

        fun maxWidth(): Int {
            return lines.maxByOrNull { it.length }?.length ?: 0
        }
    }

    fun printResults(allResults: List<TestOutcome>) {
        val maxLength = allResults.maxOfOrNull { it.maxWidth() } ?: 0

        println("┌${"─".repeat(maxLength)}┐")

        println(allResults.joinToString("├${"─".repeat(maxLength)}┤\n") { testOutcome ->
            testOutcome.lines.joinToString("│\n│", "│", "│") {
                it + " ".repeat(maxLength - it.length)
            }
        })

        println("└${"─".repeat(maxLength)}┘")
    }

    val testResults by extra(mutableListOf<TestOutcome>())

    tasks.withType<Test>().configureEach {
        val testTask = this

        // addTestListener is a workaround https://github.com/gradle/kotlin-dsl-samples/issues/836
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
}
