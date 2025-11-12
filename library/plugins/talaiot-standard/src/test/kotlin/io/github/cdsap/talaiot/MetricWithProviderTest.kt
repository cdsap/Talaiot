package io.github.cdsap.talaiot

import com.google.gson.Gson
import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.utils.TemporaryFolder
import io.kotlintest.forAll
import io.kotlintest.matchers.collections.shouldNotBeOneOf
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import org.gradle.testkit.runner.GradleRunner
import java.io.File

class MetricWithProviderTest : StringSpec({
    "given a build with process metrics enabled" {
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
                      customBuildMetricsWithProviders(
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
            val b1 = GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments("assemble", "--configuration-cache")
                .withPluginClasspath()
                .withGradleVersion(version)
                .build()
            Thread.sleep(5000)
            val reportFile = File(testProjectDir.getRoot(), "build/reports/talaiot/json/data.json")
            val report = Gson().fromJson(reportFile.readText(), ExecutionReport::class.java)

            val b2 = GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments("assemble", "--configuration-cache")
                .withPluginClasspath()
                .withGradleVersion(version)
                .build()
            Thread.sleep(5000)
            val reportFile2 = File(testProjectDir.getRoot(), "build/reports/talaiot/json/data.json")
            val report2 = Gson().fromJson(reportFile2.readText(), ExecutionReport::class.java)

            val b3 = GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments("assemble", "--configuration-cache")
                .withPluginClasspath()
                .withGradleVersion(version)
                .build()
            val reportFile3 = File(testProjectDir.getRoot(), "build/reports/talaiot/json/data.json")
            val report3 = Gson().fromJson(reportFile3.readText(), ExecutionReport::class.java)

            val b4 = GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments("assemble", "--configuration-cache")
                .withPluginClasspath()
                .withGradleVersion(version)
                .build()
            val reportFile4 = File(testProjectDir.getRoot(), "build/reports/talaiot/json/data.json")
            val report4 = Gson().fromJson(reportFile4.readText(), ExecutionReport::class.java)

            testProjectDir.delete()

            val metric1_build1 = report.customProperties.buildProperties["metric1"]

            val metric1_build2 = report2.customProperties.buildProperties["metric1"]

            val metric1_build3 = report3.customProperties.buildProperties["metric1"]
            val metric2_build3 = report3.customProperties.buildProperties["metric2"]

            val metric1_build4 = report4.customProperties.buildProperties["metric1"]
            val metric2_build4 = report4.customProperties.buildProperties["metric2"]

            metric1_build4 shouldNotBeOneOf listOf(metric1_build1, metric1_build2, metric1_build3)
            // if we want to keep a evaluation of the provider every time we need to use ValueSource
            // if we are using providers like in this test for metric2:
            // providers.provider { System.nanoTime().toString() }
            // the expectation os that the value is not going to be evaluated.
            // that's why the previous assertion is used for value sources and the following one
            // only represents the providers.
            metric2_build4 shouldBe metric2_build3
        }
    }
})
