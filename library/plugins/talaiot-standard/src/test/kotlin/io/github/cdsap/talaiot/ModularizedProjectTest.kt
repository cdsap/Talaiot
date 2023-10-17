package io.github.cdsap.talaiot

import com.google.gson.Gson
import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.utils.TemporaryFolder
import io.kotlintest.forAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import org.gradle.testkit.runner.GradleRunner
import java.io.File

class ModularizedProjectTest : StringSpec({
    "given default config" {
        forAll(
            listOf(
                "8.3"
            )
        ) { version: String ->
            val testProjectDir = TemporaryFolder()

            testProjectDir.create()
            val buildFile = testProjectDir.newFile("build.gradle.kts")
            buildFile.appendText(
                """
                import io.github.cdsap.talaiot.publisher.JsonPublisher
                plugins {
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
            val settings = testProjectDir.newFile("settings.gradle.kts")
            settings.appendText(
                """
                include (":app")
                """.trimIndent()
            )
            testProjectDir.newFolder("app")
            val buildGradleApp = testProjectDir.newFile("app/build.gradle.kts")
            buildGradleApp.appendText(
                """
                plugins {
                    java
                }
                """.trimIndent()
            )
            GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments(":app:assemble", "--info", "--configuration-cache")
                .withPluginClasspath()
                .withGradleVersion(version)
                .build()
            Thread.sleep(5000)
            val reportFile = File(testProjectDir.getRoot(), "build/reports/talaiot/json/data.json")
            val report = Gson().fromJson(reportFile.readText(), ExecutionReport::class.java)
            println(report)
            report.tasks?.filter { it.taskName == "assemble" }?.first()?.module shouldBe ":app"

            testProjectDir.delete()
        }
    }
})
