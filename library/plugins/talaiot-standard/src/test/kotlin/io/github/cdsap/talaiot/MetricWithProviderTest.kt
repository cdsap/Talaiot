package io.github.cdsap.talaiot

import com.google.gson.Gson
import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.utils.TemporaryFolder
import io.kotlintest.forAll
import io.kotlintest.matchers.collections.shouldNotBeOneOf
import io.kotlintest.matchers.numerics.shouldBeGreaterThan
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import org.gradle.testkit.runner.GradleRunner
import java.io.File

class MetricWithProviderTest : StringSpec({
    "given a build with provider metrics, ValueSources are correctly processed when configuration cache hits and regular provider not" {
        forAll(
            listOf(
                "9.2.0"
            )
        ) { version: String ->
            val testProjectDir = TemporaryFolder()

            testProjectDir.create()
            val buildFile = testProjectDir.newFile("build.gradle.kts")
            buildFile.appendText(
                """
                import io.github.cdsap.talaiot.publisher.JsonPublisher
                plugins {
                    id ("java")
                    id ("io.github.cdsap.talaiot")
                }

                talaiot {
                    logger = io.github.cdsap.talaiot.logger.LogTracker.Mode.INFO
                    publishers {
                         jsonPublisher = true
                    }
                    metrics {
                      initialProviderMetrics(
                         "metric1" to  providers.of(A::class) {},
                         "metric2" to  providers.provider { System.nanoTime().toString() }
                      )
                    }
                }

                abstract class A : ValueSource<String, ValueSourceParameters.None> {
                    override fun obtain(): String? {
                        return System.nanoTime().toString()
                    }
                }



                """.trimIndent()
            )
            GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments("assemble", "--configuration-cache")
                .withPluginClasspath()
                .withGradleVersion(version)
                .build()
            Thread.sleep(5000)
            val reportFile = File(testProjectDir.getRoot(), "build/reports/talaiot/json/data.json")
            val report = Gson().fromJson(reportFile.readText(), ExecutionReport::class.java)

            GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments("assemble", "--configuration-cache")
                .withPluginClasspath()
                .withGradleVersion(version)
                .build()
            Thread.sleep(5000)
            val reportFile2 = File(testProjectDir.getRoot(), "build/reports/talaiot/json/data.json")
            val report2 = Gson().fromJson(reportFile2.readText(), ExecutionReport::class.java)

            GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments("assemble", "--configuration-cache")
                .withPluginClasspath()
                .withGradleVersion(version)
                .build()
            val reportFile3 = File(testProjectDir.getRoot(), "build/reports/talaiot/json/data.json")
            val report3 = Gson().fromJson(reportFile3.readText(), ExecutionReport::class.java)

            GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments("assemble", "--configuration-cache")
                .withPluginClasspath()
                .withGradleVersion(version)
                .build()
            val reportFile4 = File(testProjectDir.getRoot(), "build/reports/talaiot/json/data.json")
            val report4 = Gson().fromJson(reportFile4.readText(), ExecutionReport::class.java)

            testProjectDir.delete()

            val metric1Build1 = report.customProperties.buildProperties["metric1"]
            val metric1Build2 = report2.customProperties.buildProperties["metric1"]
            val metric1Build3 = report3.customProperties.buildProperties["metric1"]
            val metric2Build3 = report3.customProperties.buildProperties["metric2"]
            val metric1Build4 = report4.customProperties.buildProperties["metric1"]
            val metric2Build4 = report4.customProperties.buildProperties["metric2"]

            metric1Build4 shouldNotBeOneOf listOf(metric1Build1, metric1Build2, metric1Build3)
            // if we want to keep a evaluation of the provider every time we need to use ValueSource
            // if we are using providers like in this test for metric2:
            // providers.provider { System.nanoTime().toString() }
            // the expectation is that the value is not going to be evaluated.
            // that's why the previous assertion is used for value sources and the following one
            // only represents the providers.
            metric2Build4 shouldBe metric2Build3
        }
    }

    "given a build with init and final provider metrics, metrics are correctly assigned" {
        forAll(
            listOf(
                "9.2.0"
            )
        ) { version: String ->
            val testProjectDir = TemporaryFolder()

            testProjectDir.create()
            val buildFile = testProjectDir.newFile("build.gradle.kts")
            buildFile.appendText(
                """
                import io.github.cdsap.talaiot.publisher.JsonPublisher
                plugins {
                    id ("java")
                    id ("io.github.cdsap.talaiot")
                }

                tasks.register<DefaultTask>("commonTask") {
                    doLast {
                     println("1 "+System.nanoTime())
                     println("2"+System.nanoTime())
                    }
                }

                talaiot {
                    logger = io.github.cdsap.talaiot.logger.LogTracker.Mode.INFO
                    publishers {
                         jsonPublisher = true
                    }
                    metrics {
                      initialProviderMetrics(
                         "init_metric" to  providers.of(A::class) {}
                      )
                      finalProviderMetrics(
                         "end_metric" to  providers.of(A::class) {}

                      )
                    }
                }

                abstract class A : ValueSource<Long, ValueSourceParameters.None> {
                    override fun obtain(): Long? {
                       Thread.sleep(10000)
                        return System.nanoTime()
                    }
                }
                """.trimIndent()
            )
            GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments("commonTask", "--configuration-cache")
                .withPluginClasspath()
                .withGradleVersion(version)
                .build()
            Thread.sleep(5000)
            val reportFile = File(testProjectDir.getRoot(), "build/reports/talaiot/json/data.json")
            val report = Gson().fromJson(reportFile.readText(), ExecutionReport::class.java)
            val raw = report.customProperties.buildProperties["init_metric"]
            val raw2 = report.customProperties.buildProperties["end_metric"]
            val initMetric: Long? = lng(raw)
            val endMetric: Long? = lng(raw2)
            val diff = endMetric!! - initMetric!!
            // we have forced a thread sleep of 10 seconds in the ValueSource obtain method
            // so the difference between the first and second metric should be at least 10 seconds in nanoseconds
            diff shouldBeGreaterThan 10000000000L
        }
    }
})

private fun lng(value: Any?): Long? = when (value) {
    is Long -> value
    is Int -> value.toLong()
    is Double -> value.toLong()
    is Number -> value.toLong()
    else -> null // or throw
}
