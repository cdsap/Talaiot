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
                "7.4.1",
                "7.2",
                "7.1.1",
                "7.1",
                "7.0.2",
                "7.0",
                "6.8.1",
                "6.7.1",
                "6.5.1"
            )
        ) { version: String ->
            val testProjectDir = TemporaryFolder()

            testProjectDir.create()
            val buildFile = testProjectDir.newFile("build.gradle")
            buildFile.appendText(
                """
                import io.github.cdsap.talaiot.publisher.JsonPublisher
                plugins {
                    id 'java'
                    id 'io.github.cdsap.talaiot'
                }

                talaiot {
                    logger = io.github.cdsap.talaiot.logger.LogTracker.Mode.INFO
                    publishers {
                        jsonPublisher = true
                    }
                }

                """.trimIndent()
            )
            val result = GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments("assemble", "--info", "--stacktrace")
                .withPluginClasspath()
                .withGradleVersion(version)
                .build()

            Thread.sleep(2000)
            val reportFile = File(testProjectDir.getRoot(), "build/reports/talaiot/json/data.json")
            val report = Gson().fromJson(reportFile.readText(), ExecutionReport::class.java)

            println("versssssion")
            println(result.output)
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
            println("19")

            report.requestedTasks shouldBe "assemble"
            report.rootProject shouldNotBe null
            report.success shouldBe true
            println("20")

            tasks.forEach {
                it.ms shouldNotBe null
                it.taskName shouldNotBe null
                it.taskPath shouldNotBe null
                it.state shouldNotBe null
                it.module shouldNotBe null
                it.startMs shouldNotBe null
                it.stopMs shouldNotBe null
            }
            println("22")
        }
    }
})
