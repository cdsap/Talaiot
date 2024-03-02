package io.github.cdsap.talaiot

import com.google.gson.Gson
import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.utils.TemporaryFolder
import io.kotlintest.forAll
import io.kotlintest.matchers.string.shouldNotStartWith
import io.kotlintest.matchers.string.shouldStartWith
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import org.gradle.testkit.runner.GradleRunner
import java.io.File

class ConfigurationCacheHit : StringSpec({
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
                    }

                }

                """.trimIndent()
            )
            GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments("assemble", "--info", "--configuration-cache")
                .withPluginClasspath()
                .withGradleVersion(version)
                .build()

            Thread.sleep(5000)
            val reportFile = File(testProjectDir.getRoot(), "build/reports/talaiot/json/data.json")
            val report = Gson().fromJson(reportFile.readText(), ExecutionReport::class.java)

            report.configurationCacheHit shouldBe false
            report.configurationDurationMs shouldNotStartWith "0"

            GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments("assemble", "--info", "--configuration-cache")
                .withPluginClasspath()
                .withGradleVersion(version)
                .build()
            Thread.sleep(5000)
            val reportFileHit = File(testProjectDir.getRoot(), "build/reports/talaiot/json/data.json")
            val reportHit = Gson().fromJson(reportFileHit.readText(), ExecutionReport::class.java)

            testProjectDir.delete()
            reportHit.configurationCacheHit shouldBe true
            reportHit.configurationDurationMs shouldStartWith "0"
        }
    }
})
