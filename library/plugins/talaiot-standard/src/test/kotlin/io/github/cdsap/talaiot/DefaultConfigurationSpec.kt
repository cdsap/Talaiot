package io.github.cdsap.talaiot

import com.google.gson.Gson
import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.utils.TemporaryFolder
import io.kotlintest.forAll
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.StringSpec
import org.gradle.testkit.runner.GradleRunner
import java.io.File

class DefaultConfigurationSpec : StringSpec({
    "given default config" {
        forAll(
            listOf(
                "8.3",
                "8.2.1",
                "7.6.2"
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
                         customPublishers.add(CustomPublisher())
                    }

                }
                class CustomPublisher : io.github.cdsap.talaiot.publisher.Publisher {

                    override fun publish(report: io.github.cdsap.talaiot.entities.ExecutionReport) {
                        println("[CustomPublisher] : Number of tasks = ")

                    }
                }

                """.trimIndent()
            )
            GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments("assemble", "--info", "--stacktrace")
                .withPluginClasspath()
                .withGradleVersion(version)
                .build()
            Thread.sleep(5000)
            val reportFile = File(testProjectDir.getRoot(), "build/reports/talaiot/json/data.json")
            val report = Gson().fromJson(reportFile.readText(), ExecutionReport::class.java)

            testProjectDir.delete()
            report.environment.gradleVersion shouldBe version
            report.beginMs shouldNotBe null
            report.endMs shouldNotBe null
            report.durationMs shouldNotBe null

            report.configurationDurationMs shouldNotBe null

            val tasks = report.tasks!!
            tasks.size shouldBe 5
            tasks.count { it.rootNode } shouldBe 1

            tasks.find { it.rootNode }!!.taskName shouldBe "assemble"

            report.requestedTasks shouldBe "assemble"
            report.rootProject shouldNotBe null
            report.success shouldBe true

            tasks.forEach {
                it.ms shouldNotBe null
                it.taskName shouldNotBe null
                it.taskPath shouldNotBe null
                it.state shouldNotBe null
                it.module shouldNotBe null
                it.startMs shouldNotBe null
                it.stopMs shouldNotBe null
            }
        }
    }
})
